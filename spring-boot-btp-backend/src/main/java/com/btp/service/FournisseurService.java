package com.btp.service;

import com.btp.dto.FournisseurDTO;
import com.btp.entity.Fournisseur;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.FournisseurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
@Transactional
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findAll(Pageable pageable) {
        return fournisseurRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public FournisseurDTO findById(Long id) {
        return fournisseurRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + id));
    }

    public FournisseurDTO save(@Valid FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = entityMapper.toEntity(fournisseurDTO);
        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        return entityMapper.toDTO(savedFournisseur);
    }

// INSIDE FournisseurService.java

    public FournisseurDTO update(Long id, @Valid FournisseurDTO fournisseurDTO) {
        return fournisseurRepository.findById(id)
                .map(existing -> {
                    existing.setNom(fournisseurDTO.getNom());
                    existing.setTelephone(fournisseurDTO.getTelephone());
                    existing.setEmail(fournisseurDTO.getEmail());
                    existing.setAdresse(fournisseurDTO.getAdresse());
                    // FIX: Convert Set from DTO to List for the entity
                    if (fournisseurDTO.getSpecialites() != null) {
                        existing.setSpecialites(new java.util.ArrayList<>(fournisseurDTO.getSpecialites()));
                    }
                    if (fournisseurDTO.getStatut() != null) {
                        existing.setStatut(Fournisseur.StatutFournisseur.valueOf(fournisseurDTO.getStatut()));
                    }
                    Fournisseur updatedFournisseur = fournisseurRepository.save(existing);
                    return entityMapper.toDTO(updatedFournisseur);
                }).orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + id));
    }

    public void deleteById(Long id) {
        fournisseurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByType(String type, Pageable pageable) {
        Fournisseur.TypeFournisseur typeEnum = Fournisseur.TypeFournisseur.valueOf(type.toUpperCase());
        return fournisseurRepository.findByType(typeEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByStatut(String statut, Pageable pageable) {
        Fournisseur.StatutFournisseur statutEnum = Fournisseur.StatutFournisseur.valueOf(statut.toUpperCase());
        return fournisseurRepository.findByStatut(statutEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByCreatedById(Long userId, Pageable pageable) {
        return fournisseurRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByIce(String ice, Pageable pageable) {
        return fournisseurRepository.findByIce(ice, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByRc(String rc, Pageable pageable) {
        return fournisseurRepository.findByRc(rc, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> searchByName(String keyword, Pageable pageable) {
        return fournisseurRepository.searchByName(keyword, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findBySpecialite(String specialite, Pageable pageable) {
        return fournisseurRepository.findBySpecialite(specialite, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByStatutAndCreatedBy(String statut, Long userId, Pageable pageable) {
        Fournisseur.StatutFournisseur statutEnum = Fournisseur.StatutFournisseur.valueOf(statut.toUpperCase());
        return fournisseurRepository.findByStatutAndCreatedBy(statutEnum, userId, pageable)
                .map(entityMapper::toDTO);
    }
}
