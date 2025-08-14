package com.btp.repository;

import com.btp.entity.Decompte;
import com.btp.entity.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DecompteRepository extends JpaRepository<Decompte, Long> {
    Page<Decompte> findByProjet(Projet projet, Pageable pageable);

    Page<Decompte> findByStatut(Decompte.StatutDecompte statut, Pageable pageable);

    Page<Decompte> findByCreatedById(Long userId, Pageable pageable);

    @Query("SELECT d FROM Decompte d WHERE d.dateCreation BETWEEN :startDate AND :endDate") // FIX: Use dateCreation field
    Page<Decompte> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT d FROM Decompte d WHERE d.projet.id = :projetId AND d.statut = :statut")
    Page<Decompte> findByProjetAndStatut(@Param("projetId") Long projetId, @Param("statut") Decompte.StatutDecompte statut, Pageable pageable);

    // --- START OF FIX ---
    // Corrected the field name from 'montantTotal' to 'montantTTC' to match the Decompte entity
    @Query("SELECT d FROM Decompte d WHERE d.montantTTC > :amount")
    // --- END OF FIX ---
    Page<Decompte> findByAmountGreaterThan(@Param("amount") Double amount, Pageable pageable);
}