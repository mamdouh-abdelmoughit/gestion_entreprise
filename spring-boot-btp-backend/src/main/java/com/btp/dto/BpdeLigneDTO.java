package com.btp.dto;

import lombok.Data;

@Data
public class BpdeLigneDTO {
    private Long id;
    private Long projetId;
    private String projetNom;
    private String designation;
    private String unite;
    private Double prixUnitaire;
    private Integer ordre;
}
