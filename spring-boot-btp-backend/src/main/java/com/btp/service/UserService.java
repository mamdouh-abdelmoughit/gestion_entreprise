
// src/main/java/com/btp/service/UserService.java
package com.btp.service;

import com.btp.controller.AuthController;
import com.btp.dto.UserDTO;
import com.btp.entity.*;
import com.btp.entity.User.Status;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;
    private final FournisseurRepository fournisseurRepository;
    private final OrganizationRepository organizationRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO inviteUser(String email, String username, Set<String> roles, String adminUsername) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }

        var roleEntities = roleRepository.findByNomIn(roles);
        if (roleEntities.isEmpty()) {
            throw new BadRequestException("At least one valid role is required");
        }

        // Find the admin who is inviting this user
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        // Get the admin's organization for multi-tenancy
        Organization organization = adminUser.getOrganization();

        User user = User.builder()
                .email(email)
                .username(username)
                .status(Status.PENDING)
                .roles(roleEntities.stream().collect(Collectors.toSet()))
                .createdByAdmin(adminUser) // Link to parent admin
                .organization(organization) // Set same organization as admin
                .build();

        String token = tokenService.generateToken();
        user.setActivationToken(token);
        user.setActivationTokenExpiry(Instant.now().plus(24, ChronoUnit.HOURS));

        userRepository.save(user);

        // Auto-create corresponding entity based on role
        createRoleBasedEntity(user, roles, adminUser, organization);

        String activationLink = frontendBaseUrl + "/activate?token=" + token;
        emailService.sendInvitationEmail(email, activationLink);

        return entityMapper.toDTO(user);
    }

    /**
     * Creates a Client, Employe, or Fournisseur record based on the user's role.
     * This links the User account with the business entity and sets the organization.
     */
    private void createRoleBasedEntity(User user, Set<String> roles, User adminUser, Organization organization) {
        if (roles.contains("ROLE_CLIENT")) {
            Client client = new Client();
            client.setNom(user.getUsername());
            client.setEmail(user.getEmail());
            client.setCreatedBy(adminUser);
            client.setUserAccount(user);
            client.setOrganization(organization); // Set organization for multi-tenancy
            clientRepository.save(client);
        }
        
        if (roles.contains("ROLE_EMPLOYEE")) {
            Employe employe = new Employe();
            employe.setNom(user.getUsername());
            employe.setPrenom(""); // To be updated by admin
            employe.setCin(null); // Optional — admin fills it in later via the employee form
            employe.setPoste("À définir");
            employe.setDateEmbauche(LocalDate.now());
            employe.setSalaire(0.0);
            employe.setStatut(Employe.StatutEmploye.ACTIF);
            employe.setEmail(user.getEmail());
            employe.setCreatedBy(adminUser);
            employe.setUserAccount(user);
            employe.setOrganization(organization); // Set organization for multi-tenancy
            employeRepository.save(employe);
        }
        
        if (roles.contains("ROLE_FOURNISSEUR")) {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setNom(user.getUsername());
            fournisseur.setType(Fournisseur.TypeFournisseur.FOURNISSEUR);
            fournisseur.setContact(user.getUsername());
            fournisseur.setEmail(user.getEmail());
            fournisseur.setStatut(Fournisseur.StatutFournisseur.ACTIF);
            fournisseur.setCreatedBy(adminUser);
            fournisseur.setUserAccount(user);
            fournisseur.setOrganization(organization); // Set organization for multi-tenancy
            fournisseurRepository.save(fournisseur);
        }
    }

    @Transactional
    public void activateAccount(String token, String rawPassword) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid activation token"));

        if (user.getActivationTokenExpiry() == null || user.getActivationTokenExpiry().isBefore(Instant.now())) {
            throw new BadRequestException("Activation token expired");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(Status.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);
    }

    /**
     * Activate account and return the User entity for JWT generation (auto-login).
     * This allows the newly activated user to be logged in automatically.
     */
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails activateAccountAndGetUser(String token, String rawPassword) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid activation token"));

        if (user.getActivationTokenExpiry() == null || user.getActivationTokenExpiry().isBefore(Instant.now())) {
            throw new BadRequestException("Activation token expired");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(Status.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);

        // Return as UserDetails for JWT generation
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getNom()))
                        .toList())
                .build();
    }

// UserService.java
// Corrected logic for the requestPasswordReset method
@Transactional
public void requestPasswordReset(String email) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
        return;
    }
    User user = userOpt.get();
    String token = tokenService.generateToken();
    user.setResetToken(token);
    user.setResetTokenExpiry(Instant.now().plus(1, ChronoUnit.HOURS));
    userRepository.save(user);

    // Change the link to use the correct reset-password endpoint
    String resetLink = frontendBaseUrl + "/reset-password?token=" + token;
    emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
}

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new BadRequestException("Reset token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        if (user.getStatus() == Status.PENDING) {
            user.setStatus(Status.ACTIVE);
        }
        userRepository.save(user);
    }
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    // This method is required by the GET /users/me endpoint
    public UserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    // This method is required by the PUT /users/{id} endpoint
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update basic fields
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        // Update roles
        if (userDTO.getRoles() != null) {
            Set<Role> roles = roleRepository.findByNomIn(userDTO.getRoles()).stream().collect(Collectors.toSet());
            user.setRoles(roles);
        }

        userRepository.save(user);
        return entityMapper.toDTO(user);
    }

    // This method is required by the DELETE /users/{id} endpoint
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // First, clear userAccount references from linked entities to avoid FK violations
        // Check and clear Client reference
        clientRepository.findByUserAccount(user).ifPresent(client -> {
            client.setUserAccount(null);
            clientRepository.save(client);
        });
        
        // Check and clear Employe reference
        employeRepository.findByUserAccount(user).ifPresent(employe -> {
            employe.setUserAccount(null);
            employeRepository.save(employe);
        });
        
        // Check and clear Fournisseur reference
        fournisseurRepository.findByUserAccount(user).ifPresent(fournisseur -> {
            fournisseur.setUserAccount(null);
            fournisseurRepository.save(fournisseur);
        });
        
        userRepository.delete(user);
    }
    @Transactional
    public UserDTO registerNewUser(AuthController.RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Find the default role for new users.
        // This assumes your DataInitializer creates a "ROLE_USER" or "ROLE_ADMIN".
        // Let's assign ROLE_ADMIN to the first user who registers.
        Set<Role> roles = roleRepository.findByNomIn(Set.of("ROLE_ADMIN")).stream().collect(Collectors.toSet());
        if (roles.isEmpty()) {
            // This is a safeguard in case the roles haven't been created yet.
            throw new IllegalStateException("Default role ROLE_ADMIN not found in database.");
        }

        // Create a new Organization for this admin (multi-tenancy)
        Organization organization = Organization.builder()
                .name(req.getFirstName() + " " + req.getLastName() + "'s Company")
                .email(req.getEmail())
                .active(true)
                .build();
        organizationRepository.save(organization);

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .status(User.Status.ACTIVE) // The user is active immediately
                .roles(roles)
                .organization(organization) // Link to the new organization
                .build();

        userRepository.save(user);

        return entityMapper.toDTO(user);
    }
}
