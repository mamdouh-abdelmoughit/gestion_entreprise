package com.btp.repository;

import com.btp.entity.Caution;
import com.btp.entity.Projet;
import com.btp.entity.AppelOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CautionRepository extends JpaRepository<Caution, Long> {
    List<Caution> findByType(Caution.TypeCaution type);
    
    List<Caution> findByStatut(Caution.StatutCaution statut);
    
    List<Caution> findByProjet(Projet projet);
    
    List<Caution> findByAppelOffre(AppelOffre appelOffre);
    
    List<Caution> findByCreatedById(Long userId);
    
    @Query("SELECT c FROM Caution c WHERE c.dateExpiration < CURRENT_DATE")
    List<Caution> findExpiredCautions();
    
    @Query("SELECT c FROM Caution c WHERE c.dateExpiration BETWEEN CURRENT_DATE AND :date")
    List<Caution> findExpiringSoon(@Param("date") LocalDateTime date);
    
    @Query("SELECT c FROM Caution c WHERE c.banque LIKE %:banque%")
    List<Caution> findByBanque(@Param("banque") String banque);
}
