package com.btp.service;

import com.btp.dto.RegisterRequest;
import com.btp.dto.UserDTO;
import com.btp.entity.User;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.entity.Role;
import com.btp.mapper.EntityMapper;
import com.btp.repository.RoleRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.validation.Valid;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO save(@Valid UserDTO userDTO) {
        User user = entityMapper.toEntity(userDTO);
        if (userDTO.getRoles() != null) {
            List<Role> roles = roleRepository.findByNomIn(userDTO.getRoles());
            user.setRoles(new HashSet<>(roles));
        }
        User savedUser = userRepository.save(user);
        return entityMapper.toDTO(savedUser);
    }

    @Transactional
    public UserDTO update(Long id, @Valid UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setFirstName(userDTO.getFirstName());
                    existingUser.setLastName(userDTO.getLastName());
                    existingUser.setTelephone(userDTO.getTelephone());
                    existingUser.setEnabled(userDTO.isEnabled());
                    existingUser.setLastLogin(userDTO.getLastLogin());

                    if (userDTO.getRoles() != null) {
                        List<Role> roles = roleRepository.findByNomIn(userDTO.getRoles());
                        existingUser.setRoles(new HashSet<>(roles));
                    }

                    User updatedUser = userRepository.save(existingUser);
                    return entityMapper.toDTO(updatedUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDTO register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Set the first and last name from the request
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEnabled(true);

        Set<String> strRoles = new HashSet<>();
        String role = registerRequest.getRole();
        if (role == null || role.isEmpty()) {
            strRoles.add("ROLE_USER"); // Default role
        } else {
            strRoles.add(role);
        }
        List<Role> roles = roleRepository.findByNomIn(strRoles);
        user.setRoles(new HashSet<>(roles));

        User savedUser = userRepository.save(user); // FIX: capture the saved entity
        return entityMapper.toDTO(savedUser); // FIX: map the saved entity to DTO
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
