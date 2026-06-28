package com.btp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FactureDTO {
    private Long id;
    private String numero;
    private Long projetId;
    private String projetNom;
    private Long attachementId;
    private String attachementNumero;
    private String type;           // CLIENT | FOURNISSEUR
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private Double montantHT;
    private Double tva;
    private Double montantTTC;
    private Double retenuGarantie;
    private Double avance;
    private Double montantNet;
    private String statut;
    private String observations;
    private Long createdById;
    private String createdByUsername;
    // computed from paiements
    private Double totalPaye;
    private Double resteAPayer;
}
