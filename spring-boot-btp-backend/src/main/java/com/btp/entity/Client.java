package com.btp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String email;

    private String telephone;

    private String adresse;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Multi-tenancy: Link to the organization this client belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    // Link to User account (for clients who have login access)
    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    private User userAccount;
}