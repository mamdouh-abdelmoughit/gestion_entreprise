package com.btp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaiementDTO {
    private Long id;
    private Long factureId;
    private String factureNumero;
    private Double montant;
    private LocalDate datePaiement;
    private String modePaiement;   // VIREMENT | CHEQUE | ESPECES | EFFET
    private String reference;
    private String notes;
    private Long createdById;
    private String createdByUsername;
}
