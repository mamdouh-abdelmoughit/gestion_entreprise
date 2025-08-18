package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationEmployeDTO {
    private Long id;
    private Long employeId;
    private Long projetId;
    private String role;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
}
