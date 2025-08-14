package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument type;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "appel_offre_id")
    private AppelOffre appelOffre;

    // FIX: Added ManyToOne relationship to Employe
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @Column(nullable = false)
    private String fichier;

    @Column(nullable = false)
    private Long tailleFichier;

    @Column(nullable = false)
    private LocalDateTime dateUpload;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public enum TypeDocument {
        DAO, CONTRAT, OS, PV_RECEPTION, FACTURE, AUTRE
    }
}