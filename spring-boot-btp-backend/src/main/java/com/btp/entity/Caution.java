package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "cautions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCaution type;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String banque;

    @Column(nullable = false)
    private LocalDateTime dateEmission;

    @Column(nullable = false)
    private LocalDateTime dateExpiration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCaution statut;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @OneToOne
    @JoinColumn(name = "appel_offre_id")
    private AppelOffre appelOffre;

    // FIX: Added ManyToOne relationship to Fournisseur
    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @Column(name = "document_scan")
    private String documentScan;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public enum TypeCaution {
        PROVISOIRE, DEFINITIVE, BONNE_EXECUTION, RETENUE_GARANTIE
    }

    public enum StatutCaution {
        ACTIVE, RESTITUEE, CONFISQUEE, EXPIREE
    }
}