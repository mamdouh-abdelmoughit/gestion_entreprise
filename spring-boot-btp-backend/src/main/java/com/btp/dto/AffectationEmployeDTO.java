package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationEmployeDTO {
    private Long id;
    private Long employeId;
    private Long projetId;
    private String role;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String statut;
}
