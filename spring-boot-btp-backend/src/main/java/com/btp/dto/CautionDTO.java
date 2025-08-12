package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CautionDTO {
    private Long id;
    private String type;
    private BigDecimal montant;
    private LocalDateTime dateEmission;
    private LocalDateTime dateEcheance;
    private String beneficiaire;
    private String statut;
    private Long projetId;
    private Long fournisseurId;
}
