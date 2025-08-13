package com.btp.repository;

import com.btp.entity.AppelOffre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppelOffreRepository extends JpaRepository<AppelOffre, Long> {
    Page<AppelOffre> findByStatut(AppelOffre.StatutAppelOffre statut, Pageable pageable);
    
    Page<AppelOffre> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT a FROM AppelOffre a WHERE a.dateLimiteDepot > CURRENT_DATE")
    Page<AppelOffre> findActiveAppelsOffres(Pageable pageable);
    
    @Query("SELECT a FROM AppelOffre a WHERE a.statut = :statut AND a.createdBy.id = :userId")
    Page<AppelOffre> findByStatutAndCreatedBy(@Param("statut") AppelOffre.StatutAppelOffre statut, @Param("userId") Long userId, Pageable pageable);
}
