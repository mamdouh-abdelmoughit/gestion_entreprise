package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "bpde_lignes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpdeLigne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String unite;

    @Column(nullable = false)
    private Double prixUnitaire;

    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
