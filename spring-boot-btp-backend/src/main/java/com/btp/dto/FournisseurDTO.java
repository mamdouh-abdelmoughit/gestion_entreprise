package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private Set<String> specialites;
    private String statut;
}
