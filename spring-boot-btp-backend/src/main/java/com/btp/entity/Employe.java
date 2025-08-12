package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String cin;

    private String telephone;

    private String email;

    @Column(nullable = false)
    private String poste;

    @Column(nullable = false)
    private LocalDateTime dateEmbauche;

    @Column(nullable = false)
    private Double salaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmploye statut;

    private String adresse;

    @ElementCollection
    @CollectionTable(name = "employe_competences", joinColumns = @JoinColumn(name = "employe_id"))
    @Column(name = "competence")
    private List<String> competences;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "employe")
    @JsonIgnoreProperties("employe")
    private List<AffectationEmploye> affectations;

    public enum StatutEmploye {
        ACTIF, CONGE, SUSPENDU, DEMISSIONNE
    }
}
