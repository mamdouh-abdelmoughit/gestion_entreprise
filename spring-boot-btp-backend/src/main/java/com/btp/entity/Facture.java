package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    // Optional: which Attachement generated this Facture
    @ManyToOne(optional = true)
    @JoinColumn(name = "attachement_id")
    private Attachement attachement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeFacture type = TypeFacture.CLIENT;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @Column(nullable = false)
    private LocalDate dateEcheance;

    @Column(nullable = false)
    private Double montantHT = 0.0;

    @Column(nullable = false)
    private Double tva = 20.0; // percentage

    @Column(nullable = false)
    private Double montantTTC = 0.0;

    @Column(nullable = false)
    private Double retenuGarantie = 0.0;

    @Column(nullable = false)
    private Double avance = 0.0;

    @Column(nullable = false)
    private Double montantNet = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut = StatutFacture.BROUILLON;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("facture")
    private List<Paiement> paiements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public enum TypeFacture {
        CLIENT, FOURNISSEUR
    }

    public enum StatutFacture {
        BROUILLON, EMISE, PARTIELLEMENT_PAYEE, PAYEE, EN_RETARD, ANNULEE
    }
}
