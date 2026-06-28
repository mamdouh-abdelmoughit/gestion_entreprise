package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Entity
@Table(name = "fournisseurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeFournisseur type;

    @Column(nullable = false)
    private String contact;

    private String telephone;

    private String email;

    private String adresse;

    private String ice; // Identifiant Commun de l'Entreprise

    private String rc; // Registre de Commerce

    @ElementCollection
    @CollectionTable(name = "fournisseur_specialites", joinColumns = @JoinColumn(name = "fournisseur_id"))
    @Column(name = "specialite")
    private List<String> specialites;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFournisseur statut;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Multi-tenancy: Link to the organization this fournisseur belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    // Link to User account (for fournisseurs who have login access)
    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    private User userAccount;

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties("fournisseur")
    private List<Depense> depenses;

    public enum TypeFournisseur {
        FOURNISSEUR, SOUS_TRAITANT
    }

    public enum StatutFournisseur {
        ACTIF, SUSPENDU, BLACKLISTE
    }
}
