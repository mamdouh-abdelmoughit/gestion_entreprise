package com.btp.repository;

import com.btp.entity.Projet;
import com.btp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    Page<Projet> findByStatut(Projet.StatutProjet statut, Pageable pageable);
    
    Page<Projet> findByChefProjet(User chefProjet, Pageable pageable);
    
    Page<Projet> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.avancement < 100")
    Page<Projet> findActiveProjects(Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.statut = :statut AND p.chefProjet = :chefProjet")
    Page<Projet> findByStatutAndChefProjet(@Param("statut") Projet.StatutProjet statut, @Param("chefProjet") User chefProjet, Pageable pageable);
}
