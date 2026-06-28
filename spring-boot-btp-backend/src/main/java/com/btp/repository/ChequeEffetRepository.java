package com.btp.repository;

import com.btp.entity.ChequeEffet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChequeEffetRepository extends JpaRepository<ChequeEffet, Long> {
    Page<ChequeEffet> findByStatut(ChequeEffet.StatutChequeEffet statut, Pageable pageable);
    Page<ChequeEffet> findByType(ChequeEffet.TypeChequeEffet type, Pageable pageable);
    Page<ChequeEffet> findByProjetId(Long projetId, Pageable pageable);
    Page<ChequeEffet> findByOrganizationId(Long organizationId, Pageable pageable);
}
