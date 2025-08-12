package com.btp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private String nom;
    private String type;
    private String chemin;
    private Long taille;
    private LocalDateTime dateUpload;
    private String description;
    private Long projetId;
    private Long employeId;
}
