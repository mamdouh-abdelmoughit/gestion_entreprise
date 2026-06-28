package com.btp.service;

import com.btp.dto.PaiementDTO;
import com.btp.entity.Facture;
import com.btp.entity.Paiement;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.FactureRepository;
import com.btp.repository.PaiementRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaiementService {

    @Autowired private PaiementRepository paiementRepository;
    @Autowired private FactureRepository factureRepository;
    @Autowired private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<PaiementDTO> findAll(Pageable pageable) {
        return paiementRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<PaiementDTO> findByFacture(Long factureId, Pageable pageable) {
        return paiementRepository.findByFactureId(factureId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public PaiementDTO findById(Long id) {
        return paiementRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement not found: " + id));
    }

    public PaiementDTO save(PaiementDTO dto) {
        Paiement entity = new Paiement();
        mapDtoToEntity(dto, entity);
        entity.setCreatedBy(currentUser());
        Paiement saved = paiementRepository.save(entity);
        updateFactureStatut(saved.getFacture());
        return toDTO(saved);
    }

    public PaiementDTO update(Long id, PaiementDTO dto) {
        Paiement entity = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement not found: " + id));
        mapDtoToEntity(dto, entity);
        Paiement saved = paiementRepository.save(entity);
        updateFactureStatut(saved.getFacture());
        return toDTO(saved);
    }

    public void delete(Long id) {
        Paiement entity = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement not found: " + id));
        Facture facture = entity.getFacture();
        paiementRepository.deleteById(id);
        updateFactureStatut(facture);
    }

    private void updateFactureStatut(Facture facture) {
        if (facture == null) return;
        Double totalPaye = paiementRepository.sumMontantByFactureId(facture.getId());
        if (totalPaye >= facture.getMontantNet()) {
            facture.setStatut(Facture.StatutFacture.PAYEE);
        } else if (totalPaye > 0) {
            facture.setStatut(Facture.StatutFacture.PARTIELLEMENT_PAYEE);
        }
        factureRepository.save(facture);
    }

    private void mapDtoToEntity(PaiementDTO dto, Paiement entity) {
        entity.setMontant(dto.getMontant());
        entity.setDatePaiement(dto.getDatePaiement());
        entity.setReference(dto.getReference());
        entity.setNotes(dto.getNotes());
        if (dto.getModePaiement() != null) {
            entity.setModePaiement(Paiement.ModePaiement.valueOf(dto.getModePaiement()));
        }
        if (dto.getFactureId() != null) {
            entity.setFacture(factureRepository.findById(dto.getFactureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Facture not found: " + dto.getFactureId())));
        }
    }

    private PaiementDTO toDTO(Paiement e) {
        PaiementDTO dto = new PaiementDTO();
        dto.setId(e.getId());
        dto.setMontant(e.getMontant());
        dto.setDatePaiement(e.getDatePaiement());
        dto.setModePaiement(e.getModePaiement() != null ? e.getModePaiement().name() : null);
        dto.setReference(e.getReference());
        dto.setNotes(e.getNotes());
        if (e.getFacture() != null) { dto.setFactureId(e.getFacture().getId()); dto.setFactureNumero(e.getFacture().getNumero()); }
        if (e.getCreatedBy() != null) { dto.setCreatedById(e.getCreatedBy().getId()); dto.setCreatedByUsername(e.getCreatedBy().getUsername()); }
        return dto;
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
