
package com.btp.controller;

import com.btp.dto.UserDTO;
import com.btp.security.JwtService;
import com.btp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Admin invites a user (creates account in PENDING state and sends activation link)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/invite")
    public ResponseEntity<UserDTO> invite(@RequestBody InviteRequest req, java.security.Principal principal) {
        // Get the current admin's username from the security context
        String adminUsername = principal.getName();
        UserDTO dto = userService.inviteUser(req.getEmail(), req.getUsername(), req.getRoles(), adminUsername);
        return ResponseEntity.ok(dto);
    }

    // User activates account by setting a password - NO auto-login, user must login manually
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestBody ActivateRequest req) {
        // Just activate the account, don't return token
        userService.activateAccount(req.getToken(), req.getPassword());
        return ResponseEntity.ok().build();
    }

    // Public: request password reset (response is always 200 to avoid user enumeration)
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgot(@RequestBody ForgotRequest req) {
        userService.requestPasswordReset(req.getEmail());
        return ResponseEntity.ok().build();
    }

    // Public: reset password with token
    @PostMapping("/reset-password")
    public ResponseEntity<Void> reset(@RequestBody ResetRequest req) {
        userService.resetPassword(req.getToken(), req.getPassword());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        // Extract roles for frontend
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new LoginResponse(jwt, roles));
    }
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        // Register the new user
        userService.registerNewUser(req);

        // After registration, we immediately log them in.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        // Extract roles for frontend
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(jwt, roles));
    }

    // You will need a DTO for this request
    @Data
    public static class RegisterRequest {
        @NotBlank private String username;
        @NotBlank private String email;
        @NotBlank private String password;
        @NotBlank private String firstName;
        @NotBlank
        private String lastName;
    }
    // --- END OF THE FINAL FIX ---

    @Data
    public static class InviteRequest {
        private String email;
        private String username;
        private Set<String> roles;
    }

    @Data
    public static class ActivateRequest {
        private String token;
        private String password;
    }

    @Data
    public static class ForgotRequest {
        private String email;
    }

    @Data
    public static class ResetRequest {
        private String token;
        private String password;
    }

    // Add the DTOs
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
    
    @Data @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private List<String> roles;
    }
    
    @Data @AllArgsConstructor
    public static class ActivationResponse {
        private String token;
        private List<String> roles;
    }
}
