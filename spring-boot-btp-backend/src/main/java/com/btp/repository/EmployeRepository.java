package com.btp.repository;

import com.btp.entity.Employe;
import com.btp.entity.Organization;
import com.btp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByUserAccount(User userAccount);
    Page<Employe> findByStatut(Employe.StatutEmploye statut, Pageable pageable);
    
    Page<Employe> findByPoste(String poste, Pageable pageable);
    
    Page<Employe> findByCreatedById(Long userId, Pageable pageable);
    
    @Query("SELECT e FROM Employe e WHERE e.nom LIKE %:keyword% OR e.prenom LIKE %:keyword%")
    Page<Employe> searchByName(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT e FROM Employe e WHERE e.statut = :statut AND e.createdBy.id = :userId")
    Page<Employe> findByStatutAndCreatedBy(@Param("statut") Employe.StatutEmploye statut, @Param("userId") Long userId, Pageable pageable);
    
    // Multi-tenancy queries
    Page<Employe> findByOrganization(Organization organization, Pageable pageable);
    Page<Employe> findByOrganizationId(Long organizationId, Pageable pageable);
    Optional<Employe> findByIdAndOrganizationId(Long id, Long organizationId);
    
    @Query("SELECT e FROM Employe e WHERE e.organization.id = :orgId AND e.statut = :statut")
    Page<Employe> findByOrganizationIdAndStatut(@Param("orgId") Long orgId, @Param("statut") Employe.StatutEmploye statut, Pageable pageable);
}
