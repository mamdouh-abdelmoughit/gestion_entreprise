package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
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
    private String numero = "N/A"; // FIX: Provide a default value

    @Column(nullable = false)
    private String nom;

    @OneToOne
    @JoinColumn(name = "appel_offre_id")
    private AppelOffre appelOffre;

    @Column(nullable = false)
    private String maitreDOuvrage = "N/A";

    @Column(nullable = false)
    private Double montantContrat = 0.0;

    @Column(nullable = true)
    private Double budgetEstime;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFinPrevue;

    private LocalDate dateFinReelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutProjet statut;

    @Column(nullable = false)
    private Double avancement= 0.0; // 0-100

    @ManyToOne
    @JoinColumn(name = "chef_projet", nullable = false)
    private User chefProjet;

    // --- START OF FIX ---
    @ManyToOne
    @JoinColumn(name = "client_id") // This will be the foreign key column
    private Client client;
    // --- END OF FIX ---

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
