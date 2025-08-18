package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppelOffreDTO {
    private Long id;
    private String titre;
    private String numero; // Add the missing field
    private String maitreDOuvrage;
    private String description;
    private BigDecimal budgetEstimatif;
    private LocalDate datePublication;
    private LocalDate dateLimite;
    private String statut;
    private Long projetId;
    private List<Long> fournisseurIds;
}
