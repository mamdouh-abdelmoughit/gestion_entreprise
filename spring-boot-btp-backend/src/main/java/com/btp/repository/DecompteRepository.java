package com.btp.repository;

import com.btp.entity.Decompte;
import com.btp.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DecompteRepository extends JpaRepository<Decompte, Long> {
    List<Decompte> findByProjet(Projet projet);
    
    List<Decompte> findByStatut(Decompte.StatutDecompte statut);
    
    List<Decompte> findByCreatedById(Long userId);
    
    @Query("SELECT d FROM Decompte d WHERE d.dateDecompte BETWEEN :startDate AND :endDate")
    List<Decompte> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d FROM Decompte d WHERE d.projet.id = :projetId AND d.statut = :statut")
    List<Decompte> findByProjetAndStatut(@Param("projetId") Long projetId, @Param("statut") Decompte.StatutDecompte statut);
    
    @Query("SELECT d FROM Decompte d WHERE d.montantTotal > :amount")
    List<Decompte> findByAmountGreaterThan(@Param("amount") Double amount);
}
