package com.btp.service;

import com.btp.dto.AppelOffreDTO;
import com.btp.entity.AppelOffre;
import com.btp.entity.Fournisseur;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AppelOffreRepository;
import com.btp.repository.FournisseurRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class AppelOffreService {

    @Autowired
    private AppelOffreRepository appelOffreRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findAll(Pageable pageable) {
        return appelOffreRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public AppelOffreDTO findById(Long id) {
        return appelOffreRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + id));
    }

    public AppelOffreDTO save(AppelOffreDTO appelOffreDTO) {
        AppelOffre appelOffre = entityMapper.toEntity(appelOffreDTO);

        updateRelationships(appelOffre, appelOffreDTO);

        AppelOffre savedAppelOffre = appelOffreRepository.save(appelOffre);
        return entityMapper.toDTO(savedAppelOffre);
    }

    // INSIDE AppelOffreService.java

    public AppelOffreDTO update(Long id, AppelOffreDTO appelOffreDTO) {
        return appelOffreRepository.findById(id).map(existing -> {
            // FIX: Corrected setter methods to match the entity fields
            existing.setIntitule(appelOffreDTO.getTitre());
            existing.setDescription(appelOffreDTO.getDescription());
            if (appelOffreDTO.getBudgetEstimatif() != null) {
                existing.setMontantEstime(appelOffreDTO.getBudgetEstimatif().doubleValue());
            }
            existing.setDatePublication(appelOffreDTO.getDatePublication());
            existing.setDateLimiteDepot(appelOffreDTO.getDateLimite()); // FIX: Corrected setter
            if (appelOffreDTO.getStatut() != null) {
                existing.setStatut(AppelOffre.StatutAppelOffre.valueOf(appelOffreDTO.getStatut()));
            }

            updateRelationships(existing, appelOffreDTO);

            AppelOffre updatedAppelOffre = appelOffreRepository.save(existing);
            return entityMapper.toDTO(updatedAppelOffre);
        }).orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + id));
    }

    public void deleteById(Long id) {
        appelOffreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByStatut(String statut, Pageable pageable) {
        AppelOffre.StatutAppelOffre statutEnum = AppelOffre.StatutAppelOffre.valueOf(statut.toUpperCase());
        return appelOffreRepository.findByStatut(statutEnum, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByCreatedById(Long userId, Pageable pageable) {
        return appelOffreRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findActiveAppelsOffres(Pageable pageable) {
        return appelOffreRepository.findActiveAppelsOffres(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByStatutAndCreatedBy(String statut, Long userId, Pageable pageable) {
        AppelOffre.StatutAppelOffre statutEnum = AppelOffre.StatutAppelOffre.valueOf(statut.toUpperCase());
        return appelOffreRepository.findByStatutAndCreatedBy(statutEnum, userId, pageable)
                .map(entityMapper::toDTO);
    }

    private void updateRelationships(AppelOffre appelOffre, AppelOffreDTO appelOffreDTO) {
        if (appelOffreDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(appelOffreDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + appelOffreDTO.getProjetId()));
            appelOffre.setProjet(projet);
        }

        if (appelOffreDTO.getFournisseurIds() != null) {
            List<Fournisseur> fournisseurs = fournisseurRepository.findAllById(appelOffreDTO.getFournisseurIds());
            appelOffre.setFournisseurs(new HashSet<>(fournisseurs));
        }
    }
}
