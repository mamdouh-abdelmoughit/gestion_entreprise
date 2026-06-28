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
@Table(name = "attachements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachement {

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
    private LocalDate dateAttachement;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAttachement statut = StatutAttachement.BROUILLON;

    @OneToMany(mappedBy = "attachement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("attachement")
    private List<LigneAttachement> lignes = new ArrayList<>();

    @Column(name = "document_pdf")
    private String documentPdf;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public enum StatutAttachement {
        BROUILLON, SOUMIS, VALIDE
    }
}
