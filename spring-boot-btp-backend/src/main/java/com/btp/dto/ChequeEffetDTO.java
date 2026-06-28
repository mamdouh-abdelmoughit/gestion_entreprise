package com.btp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ChequeEffetDTO {
    private Long id;
    private String type;           // CHEQUE | EFFET
    private String numero;
    private String banque;
    private Double montant;
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private String beneficiaire;
    private String tireur;
    private String statut;         // EN_ATTENTE | ENCAISSE | REJETE | RETOURNE | ANNULE
    private Long projetId;
    private String projetNom;
    private Long factureId;
    private String factureNumero;
    private String notes;
    private Long createdById;
    private String createdByUsername;
}
