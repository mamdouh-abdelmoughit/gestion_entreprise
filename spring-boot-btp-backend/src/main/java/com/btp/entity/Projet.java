package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String nom;

    @OneToOne
    @JoinColumn(name = "appel_offre_id")
    private AppelOffre appelOffre;

    @Column(nullable = false)
    private String maitreDOuvrage;

    @Column(nullable = false)
    private Double montantContrat;

    @Column(nullable = false)
    private Double budgetEstime;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFinPrevue;

    private LocalDateTime dateFinReelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutProjet statut;

    @Column(nullable = false)
    private Double avancement; // 0-100

    @ManyToOne
    @JoinColumn(name = "chef_projet", nullable = false)
    private User chefProjet;

    @Column(nullable = false)
    private String adresseChantier;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties("projet")
    private List<Caution> cautions;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties("projet")
    private List<Document> documents;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties("projet")
    private List<Decompte> decomptes;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties("projet")
    private List<Depense> depenses;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties("projet")
    private List<AffectationEmploye> affectationsEmployes;

    public enum StatutProjet {
        EN_PREPARATION, EN_COURS, SUSPENDU, TERMINE, LIVRE
    }
}
