package com.btp.repository;

import com.btp.entity.AppelOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppelOffreRepository extends JpaRepository<AppelOffre, Long> {
    List<AppelOffre> findByStatut(AppelOffre.StatutAppelOffre statut);
    
    List<AppelOffre> findByCreatedById(Long userId);
    
    @Query("SELECT a FROM AppelOffre a WHERE a.dateLimiteDepot > CURRENT_DATE")
    List<AppelOffre> findActiveAppelsOffres();
    
    @Query("SELECT a FROM AppelOffre a WHERE a.statut = :statut AND a.createdBy.id = :userId")
    List<AppelOffre> findByStatutAndCreatedBy(@Param("statut") AppelOffre.StatutAppelOffre statut, @Param("userId") Long userId);
}
