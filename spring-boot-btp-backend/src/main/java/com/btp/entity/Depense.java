package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "depenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    // FIX: Added ManyToOne relationship to Employe
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieDepense categorie;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double montant;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @Column(nullable = false)
    private LocalDateTime dateDepense;

    @Column(name = "justificatif")
    private String justificatif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDepense statut;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public enum CategorieDepense {
        MAIN_OEUVRE, MATERIAUX, EQUIPEMENT, TRANSPORT, AUTRE
    }

    public enum StatutDepense {
        PREVUE, ENGEAGEE, PAYEE
    }
}