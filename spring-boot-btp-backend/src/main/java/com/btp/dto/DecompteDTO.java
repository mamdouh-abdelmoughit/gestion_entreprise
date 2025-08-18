package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecompteDTO {
    private Long id;
    private String numero;
    private String periode; // Add the missing field
    private BigDecimal montantTotal;
    private BigDecimal montantPaye;
    private BigDecimal montantRestant;
    private LocalDate dateDecompte;
    private String description;
    private String statut;
    private Long projetId;
}
