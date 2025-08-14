package com.btp.service;

import com.btp.dto.AffectationEmployeDTO;
import com.btp.entity.AffectationEmploye;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AffectationEmployeRepository;
import com.btp.repository.EmployeRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.Optional;

@Service
@Transactional
public class AffectationEmployeService {

    @Autowired
    private AffectationEmployeRepository affectationEmployeRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<AffectationEmployeDTO> findAll(Pageable pageable) {
        return affectationEmployeRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public AffectationEmployeDTO findById(Long id) {
        return affectationEmployeRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("AffectationEmploye not found with id: " + id));
    }

    public AffectationEmployeDTO save(@Valid AffectationEmployeDTO affectationEmployeDTO) {
        AffectationEmploye affectationEmploye = entityMapper.toEntity(affectationEmployeDTO);
        updateRelationships(affectationEmploye, affectationEmployeDTO);
        AffectationEmploye savedAffectation = affectationEmployeRepository.save(affectationEmploye);
        return entityMapper.toDTO(savedAffectation);
    }

    public AffectationEmployeDTO update(Long id, @Valid AffectationEmployeDTO affectationEmployeDTO) {
        return affectationEmployeRepository.findById(id)
                .map(existingAffectation -> {
                    existingAffectation.setRole(affectationEmployeDTO.getRole());
                    existingAffectation.setDateDebut(affectationEmployeDTO.getDateDebut());
                    existingAffectation.setDateFin(affectationEmployeDTO.getDateFin());
                    existingAffectation.setStatut(AffectationEmploye.StatutAffectation.valueOf(affectationEmployeDTO.getStatut()));

                    updateRelationships(existingAffectation, affectationEmployeDTO);

                    AffectationEmploye updatedAffectation = affectationEmployeRepository.save(existingAffectation);
                    return entityMapper.toDTO(updatedAffectation);
                })
                .orElseThrow(() -> new ResourceNotFoundException("AffectationEmploye not found with id: " + id));
    }

    public void deleteById(Long id) {
        affectationEmployeRepository.deleteById(id);
    }

    private void updateRelationships(AffectationEmploye affectationEmploye, AffectationEmployeDTO affectationEmployeDTO) {
        if (affectationEmployeDTO.getEmployeId() != null) {
            Employe employe = employeRepository.findById(affectationEmployeDTO.getEmployeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + affectationEmployeDTO.getEmployeId()));
            affectationEmploye.setEmploye(employe);
        }
        if (affectationEmployeDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(affectationEmployeDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + affectationEmployeDTO.getProjetId()));
            affectationEmploye.setProjet(projet);
        }
    }
}
