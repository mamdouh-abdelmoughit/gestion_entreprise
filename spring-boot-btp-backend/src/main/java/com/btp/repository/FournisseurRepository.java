package com.btp.repository;

import com.btp.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    List<Fournisseur> findByType(Fournisseur.TypeFournisseur type);
    
    List<Fournisseur> findByStatut(Fournisseur.StatutFournisseur statut);
    
    List<Fournisseur> findByCreatedById(Long userId);
    
    List<Fournisseur> findByIce(String ice);
    
    List<Fournisseur> findByRc(String rc);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.nom LIKE %:keyword%")
    List<Fournisseur> searchByName(@Param("keyword") String keyword);
    
    @Query("SELECT f FROM Fournisseur f WHERE :specialite MEMBER OF f.specialites")
    List<Fournisseur> findBySpecialite(@Param("specialite") String specialite);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.statut = :statut AND f.createdBy.id = :userId")
    List<Fournisseur> findByStatutAndCreatedBy(@Param("statut") Fournisseur.StatutFournisseur statut, @Param("userId") Long userId);
}
