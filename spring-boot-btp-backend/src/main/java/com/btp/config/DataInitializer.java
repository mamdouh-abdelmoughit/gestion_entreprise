package com.btp.config;

import com.btp.entity.Role;
import com.btp.entity.User;
import com.btp.repository.RoleRepository;
import com.btp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository,
                                          UserRepository userRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            // ── Seed roles ────────────────────────────────────────────────
            seedRole(roleRepository, "ROLE_ADMIN",       "DG — Directeur Général, full access.");
            seedRole(roleRepository, "ROLE_CP",          "Conducteur de Projet — manages multiple projects.");
            seedRole(roleRepository, "ROLE_CC",          "Chef de Chantier — manages site operations.");
            seedRole(roleRepository, "ROLE_EMPLOYEE",    "Employee role with standard access.");
            seedRole(roleRepository, "ROLE_CLIENT",      "Client role — limited visibility on their projects.");
            seedRole(roleRepository, "ROLE_FOURNISSEUR", "Supplier role with specific permissions.");
            seedRole(roleRepository, "ROLE_USER",        "Default user role.");

            // ── Seed default admin user ───────────────────────────────────
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByNomIn(Collections.singleton("ROLE_ADMIN"))
                        .stream().findFirst()
                        .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found after seeding"));

                User admin = User.builder()
                        .username("admin")
                        .email("admin@btp.com")
                        .password(passwordEncoder.encode("admin123"))
                        .status(User.Status.ACTIVE)
                        .firstName("Admin")
                        .lastName("BTP")
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);
                log.info("Created default admin user: admin / admin123");
            }
        };
    }

    private void seedRole(RoleRepository repo, String name, String description) {
        if (repo.findByNomIn(Collections.singleton(name)).isEmpty()) {
            repo.save(new Role(null, name, description, null, null, null));
            log.info("Created role: {}", name);
        }
    }
}
