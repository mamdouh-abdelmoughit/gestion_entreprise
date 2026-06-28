package com.btp.repository;

import com.btp.entity.Facture;
import com.btp.entity.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    Page<Facture> findByProjet(Projet projet, Pageable pageable);
    Page<Facture> findByStatut(Facture.StatutFacture statut, Pageable pageable);
    Page<Facture> findByType(Facture.TypeFacture type, Pageable pageable);
    Page<Facture> findByOrganizationId(Long organizationId, Pageable pageable);
    Optional<Facture> findByNumero(String numero);
    boolean existsByNumero(String numero);
}
