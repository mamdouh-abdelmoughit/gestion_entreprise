package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvenementDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateEvenement;
    private String lieu;
    private String type;
    private String statut;
    private Long projetId;
}
