package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String nom;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String statut;
    private Set<String> roles;
}
