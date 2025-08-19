package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private String nom;
    private String type;
    private String fichier;
    private Long taille;
    private LocalDate dateUpload;
    private String description;
    private Long projetId;
    private Long employeId;
}
