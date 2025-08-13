package com.btp.controller;

import com.btp.dto.LoginRequest;
import com.btp.dto.LoginResponse;
import com.btp.dto.RegisterRequest;
import com.btp.dto.RegisterResponse;
import com.btp.dto.UserDTO;
import com.btp.security.JwtService;
import com.btp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        
        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO registeredUser = userService.register(registerRequest);
        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getUsername());
        String jwt = jwtService.generateToken(userDetails);
        
        return new ResponseEntity<>(new RegisterResponse(jwt, "Bearer"), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            String newToken = jwtService.generateRefreshToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(newToken, "Bearer"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
