package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cheques_effets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChequeEffet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeChequeEffet type;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String banque;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDate dateEmission;

    private LocalDate dateEcheance;

    @Column(nullable = false)
    private String beneficiaire;

    private String tireur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutChequeEffet statut = StatutChequeEffet.EN_ATTENTE;

    @ManyToOne(optional = true)
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne(optional = true)
    @JoinColumn(name = "facture_id")
    private Facture facture;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public enum TypeChequeEffet {
        CHEQUE, EFFET
    }

    public enum StatutChequeEffet {
        EN_ATTENTE, ENCAISSE, REJETE, RETOURNE, ANNULE
    }
}
