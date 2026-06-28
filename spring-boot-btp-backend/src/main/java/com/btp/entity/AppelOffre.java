package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate; // 1. Import LocalDate instead of LocalDateTime

import java.util.List;
import java.util.Set; // Import Set

@Entity
@Table(name = "appel_offres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppelOffre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String intitule;

    @Column(nullable = false)
    private String maitreDOuvrage;

    @Column(nullable = false)
    private LocalDate datePublication;

    @Column(nullable = false)
    private LocalDate dateLimite;

    private Double montantEstime;

    // URL to the BPDE ZIP file on marchespublics.gov.ma
    @Column(name = "bpde_url", columnDefinition = "TEXT")
    private String bpdeUrl;

    private String categorie; // TRAVAUX, FOURNITURES, SERVICES

    private String lieuExecution;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAppelOffre statut;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "appelOffre")
    @JsonIgnoreProperties("appelOffre")
    private Caution cautionProvisoire;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Multi-tenancy: Link to the organization this appel d'offre belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    @OneToMany(mappedBy = "appelOffre")
    @JsonIgnoreProperties("appelOffre")
    private List<Document> documents;

    @OneToOne(mappedBy = "appelOffre")
    @JsonIgnoreProperties("appelOffre")
    private Projet projet;

    // FIX: Added ManyToMany relationship to Fournisseur
    @ManyToMany
    @JoinTable(
            name = "appel_offre_fournisseurs",
            joinColumns = @JoinColumn(name = "appel_offre_id"),
            inverseJoinColumns = @JoinColumn(name = "fournisseur_id")
    )
    private Set<Fournisseur> fournisseurs;


    public enum StatutAppelOffre {
        EN_COURS, DEPOSE, GAGNE, PERDU, ANNULE
    }
}