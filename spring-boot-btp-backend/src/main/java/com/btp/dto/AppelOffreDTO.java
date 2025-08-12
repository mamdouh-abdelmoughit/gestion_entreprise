package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppelOffreDTO {
    private Long id;
    private String titre;
    private String description;
    private BigDecimal budgetEstimatif;
    private LocalDateTime datePublication;
    private LocalDateTime dateLimite;
    private String statut;
    private Long projetId;
    private List<Long> fournisseurIds;
}
