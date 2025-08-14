package com.btp.entity;

import jakarta.persistence.*;
import lombok.*; // Import specific annotations

import java.util.Objects; // Import Objects for equals/hashCode
import java.util.Set;

@Entity
@Table(name = "roles")
// FIX: Replace @Data with more specific, safer annotations for JPA
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission")
    private Set<String> permissions;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;

    @ManyToMany(mappedBy = "roles")
    // FIX: Exclude collections from toString() to prevent StackOverflowError
    @ToString.Exclude
    private Set<User> users;

    // FIX: Implement equals() and hashCode() based ONLY on the ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}