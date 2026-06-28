package com.btp.repository;

import com.btp.entity.Attachement;
import com.btp.entity.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachementRepository extends JpaRepository<Attachement, Long> {
    Page<Attachement> findByProjet(Projet projet, Pageable pageable);
    Page<Attachement> findByStatut(Attachement.StatutAttachement statut, Pageable pageable);
    Page<Attachement> findByOrganizationId(Long organizationId, Pageable pageable);
}
