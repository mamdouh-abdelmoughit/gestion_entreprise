package com.btp.repository;

import com.btp.entity.Employe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Page<Employe> findByStatut(Employe.StatutEmploye statut, Pageable pageable);
    
    Page<Employe> findByPoste(String poste, Pageable pageable);
    
    Page<Employe> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT e FROM Employe e WHERE e.nom LIKE %:keyword% OR e.prenom LIKE %:keyword%")
    Page<Employe> searchByName(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT e FROM Employe e WHERE e.statut = :statut AND e.createdBy.id = :userId")
    Page<Employe> findByStatutAndCreatedBy(@Param("statut") Employe.StatutEmploye statut, @Param("userId") Long userId, Pageable pageable);
}
