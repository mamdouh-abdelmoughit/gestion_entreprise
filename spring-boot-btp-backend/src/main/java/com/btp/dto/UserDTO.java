package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String telephone;
    private boolean enabled;
    private LocalDate lastLogin;
    private Set<String> roles;
    
    // Reference to parent admin (for sub-accounts)
    private Long createdByAdminId;
    private String createdByAdminUsername;
    
    // Multi-tenancy: organization info
    private Long organizationId;
    private String organizationName;
}
