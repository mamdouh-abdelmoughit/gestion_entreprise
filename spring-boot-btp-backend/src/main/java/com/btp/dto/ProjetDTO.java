package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {
    private Long id;
    private String nom;
    private String numero;
    private String maitreDOuvrage; // FIX: Add field
    private String description;
    private String statut;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montantContrat; // FIX: Rename 'budget' for clarity
    private String adresse;
    private Long clientId;

    // FIX: Renamed 'responsableId' to 'chefProjetId' for consistency
    private Long chefProjetId;
}