// src/main/java/com/btp/config/DataInitializer.java
package com.btp.config;

import com.btp.entity.Role;
import com.btp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            // Check for and create "ROLE_ADMIN"
            if (roleRepository.findByNomIn(java.util.Collections.singleton("ROLE_ADMIN")).isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_ADMIN", "Administrator role with full access.", null, null, null));
                log.info("Created role: ROLE_ADMIN");
            }

            // Check for and create "ROLE_EMPLOYEE"
            if (roleRepository.findByNomIn(java.util.Collections.singleton("ROLE_EMPLOYEE")).isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_EMPLOYEE", "Employee role with standard access.", null, null, null));
                log.info("Created role: ROLE_EMPLOYEE");
            }

            // Check for and create "ROLE_CLIENT"
            if (roleRepository.findByNomIn(java.util.Collections.singleton("ROLE_CLIENT")).isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_CLIENT", "Client role with limited access.", null, null, null));
                log.info("Created role: ROLE_CLIENT");
            }

            // Check for and create "ROLE_FOURNISSEUR"
            if (roleRepository.findByNomIn(java.util.Collections.singleton("ROLE_FOURNISSEUR")).isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_FOURNISSEUR", "Supplier role with specific permissions for managing their products.", null, null, null));
                log.info("Created role: ROLE_FOURNISSEUR");
            }

            // Ensure "ROLE_USER" (default for registration) also exists
            if (roleRepository.findByNomIn(java.util.Collections.singleton("ROLE_USER")).isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_USER", "Default user role.", null, null, null));
                log.info("Created role: ROLE_USER");
            }
        };
    }
}