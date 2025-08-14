package com.btp.controller;

import com.btp.dto.UserDTO;
import com.btp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO created = userService.save(userDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/id/{id}")
    public ResponseEntity<Boolean> checkUserExistsById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.existsById(id));
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUserExistsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkUserExistsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}
