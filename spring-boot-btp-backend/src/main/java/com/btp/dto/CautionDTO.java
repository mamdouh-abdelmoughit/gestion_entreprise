package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CautionDTO {
    private Long id;
    private String numero; // Add the missing field
    private String type;
    private BigDecimal montant;
    private String banque; // Add the missing field
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private String beneficiaire;
    private String statut;
    private Long projetId;
    private Long fournisseurId;
    private Long appelOffreId;
}
