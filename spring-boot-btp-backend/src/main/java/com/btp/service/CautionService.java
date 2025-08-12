package com.btp.service;

import com.btp.dto.CautionDTO;
import com.btp.entity.Caution;
import com.btp.mapper.EntityMapper;
import com.btp.repository.CautionRepository;
import com.btp.repository.AppelOffreRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CautionService {

    @Autowired
    private CautionRepository cautionRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private AppelOffreRepository appelOffreRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<CautionDTO> findAll() {
        return cautionRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CautionDTO> findById(Long id) {
        return cautionRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public CautionDTO save(CautionDTO cautionDTO) {
        Caution caution = entityMapper.toEntity(cautionDTO);
        
        // Set relationships
        if (cautionDTO.getProjetId() != null) {
            projetRepository.findById(cautionDTO.getProjetId())
                    .ifPresent(caution::setProjet);
        }
        if (cautionDTO.getAppelOffreId() != null) {
            appelOffreRepository.findById(cautionDTO.getAppelOffreId())
                    .ifPresent(caution::setAppelOffre);
        }
        
        Caution savedCaution = cautionRepository.save(caution);
        return entityMapper.toDTO(savedCaution);
    }

    public CautionDTO update(Long id, CautionDTO cautionDTO) {
        return cautionRepository.findById(id)
                .map(existingCaution -> {
                    existingCaution.setNumero(cautionDTO.getNumero());
                    existingCaution.setType(cautionDTO.getType());
                    existingCaution.setMontant(cautionDTO.getMontant());
                    existingCaution.setBanque(cautionDTO.getBanque());
                    existingCaution.setDateEmission(cautionDTO.getDateEmission());
                    existingCaution.setDateExpiration(cautionDTO.getDateExpiration());
                    existingCaution.setStatut(cautionDTO.getStatut());
                    
                    // Update relationships
                    if (cautionDTO.getProjetId() != null) {
                        projetRepository.findById(cautionDTO.getProjetId())
                                .ifPresent(existingCaution::setProjet);
                    }
                    if (cautionDTO.getAppelOffreId() != null) {
                        appelOffreRepository.findById(cautionDTO.getAppelOffreId())
                                .ifPresent(existingCaution::setAppelOffre);
                    }
                    
                    Caution updatedCaution = cautionRepository.save(existingCaution);
                    return entityMapper.toDTO(updatedCaution);
                })
                .orElseThrow(() -> new RuntimeException("Caution not found with id: " + id));
    }

    public void deleteById(Long id) {
        cautionRepository.deleteById(id);
    }
}
