package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "lignes_attachement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneAttachement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attachement_id", nullable = false)
    @JsonIgnoreProperties("lignes")
    private Attachement attachement;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String unite;

    @Column(nullable = false)
    private Double quantitePrevue;

    @Column(nullable = false)
    private Double quantiteRealisee;

    // true when quantiteRealisee > quantitePrevue — warn but never block save
    @Column(nullable = false)
    private Boolean alerte = false;

    // Optional link to a BPDE line for price lookup when generating a Facture
    @ManyToOne(optional = true)
    @JoinColumn(name = "bpde_ligne_id")
    private BpdeLigne bpdeLigne;

    @PrePersist
    @PreUpdate
    private void computeAlerte() {
        this.alerte = (quantiteRealisee != null && quantitePrevue != null)
                && quantiteRealisee > quantitePrevue;
    }
}
