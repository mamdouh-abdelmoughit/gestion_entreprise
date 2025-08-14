package com.btp.service;

import com.btp.dto.CautionDTO;
import com.btp.entity.Caution;
import com.btp.entity.AppelOffre;
import com.btp.entity.Fournisseur;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.CautionRepository;
import com.btp.repository.AppelOffreRepository;
import com.btp.repository.FournisseurRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Service
@Transactional
public class CautionService {

    @Autowired
    private CautionRepository cautionRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private AppelOffreRepository appelOffreRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<CautionDTO> findAll(Pageable pageable) {
        return cautionRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public CautionDTO findById(Long id) {
        return cautionRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Caution not found with id: " + id));
    }

    @Transactional
    public CautionDTO save(@Valid CautionDTO cautionDTO) {
        Caution caution = entityMapper.toEntity(cautionDTO);
        updateRelationships(caution, cautionDTO);
        Caution savedCaution = cautionRepository.save(caution);
        return entityMapper.toDTO(savedCaution);
    }

// INSIDE CautionService.java

    public CautionDTO update(Long id, @Valid CautionDTO cautionDTO) {
        return cautionRepository.findById(id)
                .map(existingCaution -> {
                    if (cautionDTO.getType() != null) {
                        existingCaution.setType(Caution.TypeCaution.valueOf(cautionDTO.getType()));
                    }
                    if (cautionDTO.getMontant() != null) {
                        existingCaution.setMontant(cautionDTO.getMontant().doubleValue());
                    }
                    existingCaution.setDateEmission(cautionDTO.getDateEmission());
                    // FIX: Corrected setter method to match the entity field
                    existingCaution.setDateExpiration(cautionDTO.getDateEcheance());
                    // FIX: Removed setBeneficiaire as it doesn't exist in the entity
                    // existingCaution.setBeneficiaire(cautionDTO.getBeneficiaire());
                    if (cautionDTO.getStatut() != null) {
                        existingCaution.setStatut(Caution.StatutCaution.valueOf(cautionDTO.getStatut()));
                    }

                    updateRelationships(existingCaution, cautionDTO);

                    Caution updatedCaution = cautionRepository.save(existingCaution);
                    return entityMapper.toDTO(updatedCaution);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Caution not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        cautionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByType(String type, Pageable pageable) {
        Caution.TypeCaution typeEnum = Caution.TypeCaution.valueOf(type.toUpperCase());
        return cautionRepository.findByType(typeEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByStatut(String statut, Pageable pageable) {
        Caution.StatutCaution statutEnum = Caution.StatutCaution.valueOf(statut.toUpperCase());
        return cautionRepository.findByStatut(statutEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByProjet(Long projetId, Pageable pageable) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + projetId));
        return cautionRepository.findByProjet(projet, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByAppelOffre(Long appelOffreId, Pageable pageable) {
        AppelOffre appelOffre = appelOffreRepository.findById(appelOffreId)
                .orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + appelOffreId));
        return cautionRepository.findByAppelOffre(appelOffre, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByCreatedById(Long userId, Pageable pageable) {
        return cautionRepository.findByCreatedById(userId, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findExpiredCautions(Pageable pageable) {
        return cautionRepository.findExpiredCautions(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findExpiringSoon(LocalDateTime date, Pageable pageable) {
        return cautionRepository.findExpiringSoon(date, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<CautionDTO> findByBanque(String banque, Pageable pageable) {
        return cautionRepository.findByBanque(banque, pageable).map(entityMapper::toDTO);
    }

    private void updateRelationships(Caution caution, CautionDTO cautionDTO) {
        if (cautionDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(cautionDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + cautionDTO.getProjetId()));
            caution.setProjet(projet);
        }

        if (cautionDTO.getFournisseurId() != null) {
            Fournisseur fournisseur = fournisseurRepository.findById(cautionDTO.getFournisseurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + cautionDTO.getFournisseurId()));
            caution.setFournisseur(fournisseur);
        }

        if (cautionDTO.getAppelOffreId() != null) {
            AppelOffre appelOffre = appelOffreRepository.findById(cautionDTO.getAppelOffreId())
                    .orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + cautionDTO.getAppelOffreId()));
            caution.setAppelOffre(appelOffre);
        }
    }
}
