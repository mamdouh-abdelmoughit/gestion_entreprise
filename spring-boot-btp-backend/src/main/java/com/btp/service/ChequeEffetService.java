package com.btp.service;

import com.btp.dto.ChequeEffetDTO;
import com.btp.entity.ChequeEffet;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.ChequeEffetRepository;
import com.btp.repository.FactureRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChequeEffetService {

    @Autowired private ChequeEffetRepository chequeEffetRepository;
    @Autowired private ProjetRepository projetRepository;
    @Autowired private FactureRepository factureRepository;
    @Autowired private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ChequeEffetDTO> findAll(Pageable pageable) {
        return chequeEffetRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ChequeEffetDTO findById(Long id) {
        return chequeEffetRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("ChequeEffet not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ChequeEffetDTO> findByProjet(Long projetId, Pageable pageable) {
        return chequeEffetRepository.findByProjetId(projetId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ChequeEffetDTO> findByStatut(String statut, Pageable pageable) {
        return chequeEffetRepository.findByStatut(ChequeEffet.StatutChequeEffet.valueOf(statut), pageable).map(this::toDTO);
    }

    public ChequeEffetDTO save(ChequeEffetDTO dto) {
        ChequeEffet entity = new ChequeEffet();
        mapDtoToEntity(dto, entity);
        entity.setCreatedBy(currentUser());
        return toDTO(chequeEffetRepository.save(entity));
    }

    public ChequeEffetDTO update(Long id, ChequeEffetDTO dto) {
        ChequeEffet entity = chequeEffetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChequeEffet not found: " + id));
        mapDtoToEntity(dto, entity);
        return toDTO(chequeEffetRepository.save(entity));
    }

    public ChequeEffetDTO updateStatut(Long id, String statut) {
        ChequeEffet entity = chequeEffetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChequeEffet not found: " + id));
        entity.setStatut(ChequeEffet.StatutChequeEffet.valueOf(statut));
        return toDTO(chequeEffetRepository.save(entity));
    }

    public void delete(Long id) {
        chequeEffetRepository.deleteById(id);
    }

    private void mapDtoToEntity(ChequeEffetDTO dto, ChequeEffet entity) {
        if (dto.getType() != null) entity.setType(ChequeEffet.TypeChequeEffet.valueOf(dto.getType()));
        entity.setNumero(dto.getNumero());
        entity.setBanque(dto.getBanque());
        entity.setMontant(dto.getMontant());
        entity.setDateEmission(dto.getDateEmission());
        entity.setDateEcheance(dto.getDateEcheance());
        entity.setBeneficiaire(dto.getBeneficiaire());
        entity.setTireur(dto.getTireur());
        entity.setNotes(dto.getNotes());
        if (dto.getStatut() != null) entity.setStatut(ChequeEffet.StatutChequeEffet.valueOf(dto.getStatut()));
        if (dto.getProjetId() != null) {
            projetRepository.findById(dto.getProjetId()).ifPresent(entity::setProjet);
        }
        if (dto.getFactureId() != null) {
            factureRepository.findById(dto.getFactureId()).ifPresent(entity::setFacture);
        }
    }

    private ChequeEffetDTO toDTO(ChequeEffet e) {
        ChequeEffetDTO dto = new ChequeEffetDTO();
        dto.setId(e.getId());
        dto.setType(e.getType() != null ? e.getType().name() : null);
        dto.setNumero(e.getNumero());
        dto.setBanque(e.getBanque());
        dto.setMontant(e.getMontant());
        dto.setDateEmission(e.getDateEmission());
        dto.setDateEcheance(e.getDateEcheance());
        dto.setBeneficiaire(e.getBeneficiaire());
        dto.setTireur(e.getTireur());
        dto.setStatut(e.getStatut() != null ? e.getStatut().name() : null);
        dto.setNotes(e.getNotes());
        if (e.getProjet() != null) { dto.setProjetId(e.getProjet().getId()); dto.setProjetNom(e.getProjet().getNom()); }
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
