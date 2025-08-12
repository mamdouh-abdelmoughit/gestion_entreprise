package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepenseDTO {
    private Long id;
    private String description;
    private BigDecimal montant;
    private LocalDateTime dateDepense;
    private String categorie;
    private String statut;
    private Long projetId;
    private Long employeId;
}
