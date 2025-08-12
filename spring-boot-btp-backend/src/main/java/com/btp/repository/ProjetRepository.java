package com.btp.repository;

import com.btp.entity.Projet;
import com.btp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    List<Projet> findByStatut(Projet.StatutProjet statut);
    
    List<Projet> findByChefProjet(User chefProjet);
    
    List<Projet> findByCreatedById(Long userId);
    
    @Query("SELECT p FROM Projet p WHERE p.avancement < 100")
    List<Projet> findActiveProjects();
    
    @Query("SELECT p FROM Projet p WHERE p.statut = :statut AND p.chefProjet = :chefProjet")
    List<Projet> findByStatutAndChefProjet(@Param("statut") Projet.StatutProjet statut, @Param("chefProjet") User chefProjet);
}
