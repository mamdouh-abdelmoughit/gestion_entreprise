package com.btp.repository;

import com.btp.entity.Paiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Page<Paiement> findByFactureId(Long factureId, Pageable pageable);
    Page<Paiement> findByOrganizationId(Long organizationId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.facture.id = :factureId")
    Double sumMontantByFactureId(Long factureId);
}
