package com.btp.service;

import com.btp.dto.AffectationEmployeDTO;
import com.btp.entity.AffectationEmploye;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AffectationEmployeRepository;
import com.btp.repository.EmployeRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AffectationEmployeService {

    @Autowired
    private AffectationEmployeRepository affectationEmployeRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<AffectationEmployeDTO> findAll() {
        return affectationEmployeRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AffectationEmployeDTO> findById(Long id) {
        return affectationEmployeRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public AffectationEmployeDTO save(AffectationEmployeDTO affectationEmployeDTO) {
        AffectationEmploye affectationEmploye = entityMapper.toEntity(affectationEmployeDTO);
        
        // Set relationships
        if (affectationEmployeDTO.getEmployeId() != null) {
            employeRepository.findById(affectationEmployeDTO.getEmployeId())
                    .ifPresent(affectationEmploye::setEmploye);
        }
        if (affectationEmployeDTO.getProjetId() != null) {
            projetRepository.findById(affectationEmployeDTO.getProjetId())
                    .ifPresent(affectationEmploye::setProjet);
        }
        
        AffectationEmploye savedAffectation = affectationEmployeRepository.save(affectationEmploye);
        return entityMapper.toDTO(savedAffectation);
    }

    public AffectationEmployeDTO update(Long id, AffectationEmployeDTO affectationEmployeDTO) {
        return affectationEmployeRepository.findById(id)
                .map(existingAffectation -> {
                    existingAffectation.setRole(affectationEmployeDTO.getRole());
                    existingAffectation.setDateDebut(affectationEmployeDTO.getDateDebut());
                    existingAffectation.setDateFin(affectationEmployeDTO.getDateFin());
                    existingAffectation.setStatut(affectationEmployeDTO.getStatut());
                    
                    // Update relationships
                    if (affectationEmployeDTO.getEmployeId() != null) {
                        employeRepository.findById(affectationEmployeDTO.getEmployeId())
                                .ifPresent(existingAffectation::setEmploye);
                    }
                    if (affectationEmployeDTO.getProjetId() != null) {
                        projetRepository.findById(affectationEmployeDTO.getProjetId())
                                .ifPresent(existingAffectation::setProjet);
                    }
                    
                    AffectationEmploye updatedAffectation = affectationEmployeRepository.save(existingAffectation);
                    return entityMapper.toDTO(updatedAffectation);
                })
                .orElseThrow(() -> new RuntimeException("AffectationEmploye not found with id: " + id));
    }

    public void deleteById(Long id) {
        affectationEmployeRepository.deleteById(id);
    }
}
