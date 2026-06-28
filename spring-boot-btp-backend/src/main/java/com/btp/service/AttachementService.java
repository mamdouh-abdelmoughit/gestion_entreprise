package com.btp.service;

import com.btp.dto.AttachementDTO;
import com.btp.dto.LigneAttachementDTO;
import com.btp.entity.*;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttachementService {

    @Autowired private AttachementRepository attachementRepository;
    @Autowired private ProjetRepository projetRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BpdeLigneRepository bpdeLigneRepository;

    @Transactional(readOnly = true)
    public Page<AttachementDTO> findAll(Pageable pageable) {
        return attachementRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public AttachementDTO findById(Long id) {
        return attachementRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Attachement not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<AttachementDTO> findByProjet(Long projetId, Pageable pageable) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + projetId));
        return attachementRepository.findByProjet(projet, pageable).map(this::toDTO);
    }

    public AttachementDTO save(AttachementDTO dto) {
        Attachement entity = new Attachement();
        mapDtoToEntity(dto, entity);
        entity.setCreatedBy(currentUser());
        Attachement saved = attachementRepository.save(entity);
        return toDTO(saved);
    }

    public AttachementDTO update(Long id, AttachementDTO dto) {
        Attachement entity = attachementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachement not found: " + id));
        mapDtoToEntity(dto, entity);
        return toDTO(attachementRepository.save(entity));
    }

    public void delete(Long id) {
        attachementRepository.deleteById(id);
    }

    private void mapDtoToEntity(AttachementDTO dto, Attachement entity) {
        entity.setNumero(dto.getNumero());
        entity.setPeriode(dto.getPeriode());
        entity.setDateAttachement(dto.getDateAttachement());
        entity.setDescription(dto.getDescription());
        entity.setDocumentPdf(dto.getDocumentPdf());
        if (dto.getStatut() != null) {
            entity.setStatut(Attachement.StatutAttachement.valueOf(dto.getStatut()));
        }
        if (dto.getProjetId() != null) {
            Projet p = projetRepository.findById(dto.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + dto.getProjetId()));
            entity.setProjet(p);
        }
        // Rebuild lignes
        if (dto.getLignes() != null) {
            entity.getLignes().clear();
            List<LigneAttachement> lignes = dto.getLignes().stream().map(l -> {
                LigneAttachement ligne = new LigneAttachement();
                ligne.setAttachement(entity);
                ligne.setDesignation(l.getDesignation());
                ligne.setUnite(l.getUnite());
                ligne.setQuantitePrevue(l.getQuantitePrevue());
                ligne.setQuantiteRealisee(l.getQuantiteRealisee());
                if (l.getBpdeLigneId() != null) {
                    bpdeLigneRepository.findById(l.getBpdeLigneId())
                            .ifPresent(ligne::setBpdeLigne);
                }
                return ligne;
            }).collect(Collectors.toList());
            entity.getLignes().addAll(lignes);
        }
    }

    private AttachementDTO toDTO(Attachement e) {
        AttachementDTO dto = new AttachementDTO();
        dto.setId(e.getId());
        dto.setNumero(e.getNumero());
        dto.setPeriode(e.getPeriode());
        dto.setDateAttachement(e.getDateAttachement());
        dto.setDescription(e.getDescription());
        dto.setStatut(e.getStatut() != null ? e.getStatut().name() : null);
        dto.setDocumentPdf(e.getDocumentPdf());
        if (e.getProjet() != null) {
            dto.setProjetId(e.getProjet().getId());
            dto.setProjetNom(e.getProjet().getNom());
        }
        if (e.getCreatedBy() != null) {
            dto.setCreatedById(e.getCreatedBy().getId());
            dto.setCreatedByUsername(e.getCreatedBy().getUsername());
        }
        if (e.getLignes() != null) {
            dto.setLignes(e.getLignes().stream().map(l -> {
                LigneAttachementDTO ld = new LigneAttachementDTO();
                ld.setId(l.getId());
                ld.setAttachementId(e.getId());
                ld.setDesignation(l.getDesignation());
                ld.setUnite(l.getUnite());
                ld.setQuantitePrevue(l.getQuantitePrevue());
                ld.setQuantiteRealisee(l.getQuantiteRealisee());
                ld.setAlerte(l.getAlerte());
                if (l.getBpdeLigne() != null) ld.setBpdeLigneId(l.getBpdeLigne().getId());
                return ld;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
