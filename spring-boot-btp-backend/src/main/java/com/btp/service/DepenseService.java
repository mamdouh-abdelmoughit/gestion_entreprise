package com.btp.service;

import com.btp.dto.DepenseDTO;
import com.btp.entity.Depense;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DepenseRepository;
import com.btp.repository.EmployeRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Transactional
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<DepenseDTO> findAll(Pageable pageable) {
        return depenseRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public DepenseDTO findById(Long id) {
        return depenseRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Depense not found with id: " + id));
    }

    @Transactional
    public DepenseDTO save(@Valid DepenseDTO depenseDTO) {
        Depense depense = entityMapper.toEntity(depenseDTO);
        updateRelationships(depense, depenseDTO);
        Depense savedDepense = depenseRepository.save(depense);
        return entityMapper.toDTO(savedDepense);
    }

    @Transactional
    public DepenseDTO update(Long id, @Valid DepenseDTO depenseDTO) {
        return depenseRepository.findById(id)
                .map(existingDepense -> {
                    existingDepense.setDescription(depenseDTO.getDescription());
                    existingDepense.setMontant(depenseDTO.getMontant());
                    existingDepense.setDateDepense(depenseDTO.getDateDepense());
                    existingDepense.setCategorie(depenseDTO.getCategorie());
                    existingDepense.setStatut(depenseDTO.getStatut());
                    
                    updateRelationships(existingDepense, depenseDTO);

                    Depense updatedDepense = depenseRepository.save(existingDepense);
                    return entityMapper.toDTO(updatedDepense);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Depense not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        depenseRepository.deleteById(id);
    }

    private void updateRelationships(Depense depense, DepenseDTO depenseDTO) {
        if (depenseDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(depenseDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + depenseDTO.getProjetId()));
            depense.setProjet(projet);
        }

        if (depenseDTO.getEmployeId() != null) {
            Employe employe = employeRepository.findById(depenseDTO.getEmployeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + depenseDTO.getEmployeId()));
            depense.setEmploye(employe);
        }
    }
}
