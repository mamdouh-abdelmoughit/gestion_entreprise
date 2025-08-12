package com.btp.repository;

import com.btp.entity.Employe;
import com.btp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    List<Employe> findByStatut(Employe.StatutEmploye statut);
    
    List<Employe> findByPoste(String poste);
    
    List<Employe> findByCreatedById(Long userId);
    
    @Query("SELECT e FROM Employe e WHERE e.nom LIKE %:keyword% OR e.prenom LIKE %:keyword%")
    List<Employe> searchByName(@Param("keyword") String keyword);
    
    @Query("SELECT e FROM Employe e WHERE e.statut = :statut AND e.createdBy.id = :userId")
    List<Employe> findByStatutAndCreatedBy(@Param("statut") Employe.StatutEmploye statut, @Param("userId") Long userId);
}
