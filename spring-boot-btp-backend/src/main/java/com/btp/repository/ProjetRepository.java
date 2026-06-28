package com.btp.repository;

import com.btp.entity.Client;
import com.btp.entity.Organization;
import com.btp.entity.Projet;
import com.btp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    Page<Projet> findByStatut(Projet.StatutProjet statut, Pageable pageable);
    
    Page<Projet> findByChefProjet(User chefProjet, Pageable pageable);
    
    Page<Projet> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.avancement < 100")
    Page<Projet> findActiveProjects(Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.statut = :statut AND p.chefProjet = :chefProjet")
    Page<Projet> findByStatutAndChefProjet(@Param("statut") Projet.StatutProjet statut, @Param("chefProjet") User chefProjet, Pageable pageable);
    
    // Count projects by client to check before deletion
    long countByClient(Client client);
    
    // Multi-tenancy queries
    Page<Projet> findByOrganization(Organization organization, Pageable pageable);
    Page<Projet> findByOrganizationId(Long organizationId, Pageable pageable);
    Optional<Projet> findByIdAndOrganizationId(Long id, Long organizationId);
    
    // Client-specific query (for client users to see only their projects)
    Page<Projet> findByClient(Client client, Pageable pageable);
    Page<Projet> findByClientId(Long clientId, Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.organization.id = :orgId AND p.statut = :statut")
    Page<Projet> findByOrganizationIdAndStatut(@Param("orgId") Long orgId, @Param("statut") Projet.StatutProjet statut, Pageable pageable);
    
    @Query("SELECT p FROM Projet p WHERE p.organization.id = :orgId AND p.avancement < 100")
    Page<Projet> findActiveProjectsByOrganization(@Param("orgId") Long orgId, Pageable pageable);
}
