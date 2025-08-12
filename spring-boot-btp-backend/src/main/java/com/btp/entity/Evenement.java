package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evenements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEvenement type;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "appel_offre_id")
    private AppelOffre appelOffre;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime dateEvenement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioriteEvenement priorite;

    @Column(nullable = false)
    private Boolean lu = false;

    public enum TypeEvenement {
        CREATION, MODIFICATION, ALERTE, NOTIFICATION
    }

    public enum PrioriteEvenement {
        BASSE, NORMALE, HAUTE, CRITIQUE
    }
}
