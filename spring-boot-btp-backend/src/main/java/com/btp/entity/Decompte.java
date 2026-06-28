package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "decomptes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Decompte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @Column(nullable = false)
    private String periode; // Format YYYY-MM

    @Column(nullable = false)
    private Double montantHT;

    @Column(nullable = false)
    private Double tva;

    @Column(nullable = false)
    private Double montantTTC;

    @Column(nullable = false)
    private Double retenuGarantie;

    @Column(nullable = false)
    private Double avance;

    @Column(nullable = false)
    private Double montantNet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDecompte statut;

    @Column(nullable = false)
    private LocalDate dateDecompte;

    private LocalDate dateEnvoi;

    private LocalDate datePaiement;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "document_pdf")
    private String documentPDF;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Multi-tenancy: Link to the organization this decompte belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    public enum StatutDecompte {
        BROUILLON, ENVOYE, VALIDE, PAYE
    }
}
