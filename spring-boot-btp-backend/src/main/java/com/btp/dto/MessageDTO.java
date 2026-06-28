package com.btp.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class MessageDTO {
    private Long id;
    private String contenu;
    private Long expediteurId;
    private String expediteurUsername;
    private String expediteurNomComplet;
    private String roleDisplay;    // DG | CP | CC | CLIENT | FOURNISSEUR
    private Long projetId;
    private String projetNom;
    private Instant timestamp;
}
