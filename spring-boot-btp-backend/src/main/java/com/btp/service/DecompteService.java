package com.btp.service;

import com.btp.dto.DecompteDTO;
import com.btp.entity.Decompte;
import com.btp.exception.ResourceNotFoundException;
import com.btp.entity.Projet;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DecompteRepository;
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
public class DecompteService {

    @Autowired
    private DecompteRepository decompteRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findAll(Pageable pageable) {
        return decompteRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public DecompteDTO findById(Long id) {
        return decompteRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Decompte not found with id: " + id));
    }

    @Transactional
    public DecompteDTO save(@Valid DecompteDTO decompteDTO) {
        Decompte decompte = entityMapper.toEntity(decompteDTO);
        updateRelationships(decompte, decompteDTO);
        Decompte savedDecompte = decompteRepository.save(decompte);
        return entityMapper.toDTO(savedDecompte);
    }

    @Transactional
    public DecompteDTO update(Long id, @Valid DecompteDTO decompteDTO) {
        return decompteRepository.findById(id)
                .map(existingDecompte -> {
                    existingDecompte.setNumero(decompteDTO.getNumero());
                    // FIX: Map DTO fields to appropriate entity fields
                    existingDecompte.setDateCreation(decompteDTO.getDateDecompte());
                    if(decompteDTO.getMontantTotal() != null) {
                        existingDecompte.setMontantTTC(decompteDTO.getMontantTotal().doubleValue());
                    }
                    // NOTE: MontantPaye and MontantRestant from DTO are not mapped as they don't exist in entity.
                    // You might need to add logic to calculate them based on other fields.
                    existingDecompte.setObservations(decompteDTO.getDescription());
                    if(decompteDTO.getStatut() != null) {
                        existingDecompte.setStatut(Decompte.StatutDecompte.valueOf(decompteDTO.getStatut()));
                    }

                    updateRelationships(existingDecompte, decompteDTO);

                    Decompte updatedDecompte = decompteRepository.save(existingDecompte);
                    return entityMapper.toDTO(updatedDecompte);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Decompte not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        decompteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByProjet(Long projetId, Pageable pageable) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + projetId));
        return decompteRepository.findByProjet(projet, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByStatut(String statut, Pageable pageable) {
        Decompte.StatutDecompte statutEnum = Decompte.StatutDecompte.valueOf(statut.toUpperCase());
        return decompteRepository.findByStatut(statutEnum, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByCreatedById(Long userId, Pageable pageable) {
        return decompteRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return decompteRepository.findByDateRange(startDate, endDate, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByProjetAndStatut(Long projetId, String statut, Pageable pageable) {
        Decompte.StatutDecompte statutEnum = Decompte.StatutDecompte.valueOf(statut.toUpperCase());
        return decompteRepository.findByProjetAndStatut(projetId, statutEnum, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DecompteDTO> findByAmountGreaterThan(Double amount, Pageable pageable) {
        return decompteRepository.findByAmountGreaterThan(amount, pageable)
                .map(entityMapper::toDTO);
    }

    private void updateRelationships(Decompte decompte, DecompteDTO decompteDTO) {
        if (decompteDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(decompteDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + decompteDTO.getProjetId()));
            decompte.setProjet(projet);
        }
    }
}
