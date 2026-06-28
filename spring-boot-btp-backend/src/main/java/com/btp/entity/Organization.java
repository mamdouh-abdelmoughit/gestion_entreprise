package com.btp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Objects;

/**
 * Organization entity for multi-tenancy support.
 * Each Admin creates/belongs to one Organization.
 * All data (Projects, Clients, Employees, etc.) is scoped to an Organization.
 */
@Entity
@Table(name = "organizations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String code; // Unique identifier for the organization (e.g., company registration code)

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
