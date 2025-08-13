package com.btp.repository;

import com.btp.entity.Caution;
import com.btp.entity.Projet;
import com.btp.entity.AppelOffre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CautionRepository extends JpaRepository<Caution, Long> {
    Page<Caution> findByType(Caution.TypeCaution type, Pageable pageable);
    
    Page<Caution> findByStatut(Caution.StatutCaution statut, Pageable pageable);
    
    Page<Caution> findByProjet(Projet projet, Pageable pageable);
    
    Page<Caution> findByAppelOffre(AppelOffre appelOffre, Pageable pageable);
    
    Page<Caution> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT c FROM Caution c WHERE c.dateExpiration < CURRENT_DATE")
    Page<Caution> findExpiredCautions(Pageable pageable);
    
    @Query("SELECT c FROM Caution c WHERE c.dateExpiration BETWEEN CURRENT_DATE AND :date")
    Page<Caution> findExpiringSoon(@Param("date") LocalDateTime date, Pageable pageable);
    
    @Query("SELECT c FROM Caution c WHERE c.banque LIKE %:banque%")
    Page<Caution> findByBanque(@Param("banque") String banque, Pageable pageable);
}
