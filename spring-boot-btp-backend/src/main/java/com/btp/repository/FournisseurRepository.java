package com.btp.repository;

import com.btp.entity.Fournisseur;
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
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    Optional<Fournisseur> findByUserAccount(User userAccount);
    Page<Fournisseur> findByType(Fournisseur.TypeFournisseur type, Pageable pageable);
    
    Page<Fournisseur> findByStatut(Fournisseur.StatutFournisseur statut, Pageable pageable);
    
    Page<Fournisseur> findByCreatedById(Long userId, Pageable pageable);
    
    Page<Fournisseur> findByIce(String ice, Pageable pageable);
    
    Page<Fournisseur> findByRc(String rc, Pageable pageable);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.nom LIKE %:keyword%")
    Page<Fournisseur> searchByName(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT f FROM Fournisseur f WHERE :specialite MEMBER OF f.specialites")
    Page<Fournisseur> findBySpecialite(@Param("specialite") String specialite, Pageable pageable);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.statut = :statut AND f.createdBy.id = :userId")
    Page<Fournisseur> findByStatutAndCreatedBy(@Param("statut") Fournisseur.StatutFournisseur statut, @Param("userId") Long userId, Pageable pageable);
    
    // Multi-tenancy queries
    Page<Fournisseur> findByOrganization(Organization organization, Pageable pageable);
    Page<Fournisseur> findByOrganizationId(Long organizationId, Pageable pageable);
    Optional<Fournisseur> findByIdAndOrganizationId(Long id, Long organizationId);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.organization.id = :orgId AND f.statut = :statut")
    Page<Fournisseur> findByOrganizationIdAndStatut(@Param("orgId") Long orgId, @Param("statut") Fournisseur.StatutFournisseur statut, Pageable pageable);
}
