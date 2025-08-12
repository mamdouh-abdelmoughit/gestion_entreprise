package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvenementDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateEvenement;
    private String lieu;
    private String type;
    private String statut;
    private Long projetId;
}
