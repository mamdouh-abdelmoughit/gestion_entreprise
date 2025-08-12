package com.btp.service;

import com.btp.dto.AppelOffreDTO;
import com.btp.entity.AppelOffre;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AppelOffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppelOffreService {

    @Autowired
    private AppelOffreRepository appelOffreRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<AppelOffreDTO> findAll() {
        return appelOffreRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AppelOffreDTO> findById(Long id) {
        return appelOffreRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public AppelOffreDTO save(AppelOffreDTO appelOffreDTO) {
        AppelOffre appelOffre = new AppelOffre();
        // Map DTO to entity
        appelOffre.setTitre(appelOffreDTO.getTitre());
        appelOffre.setDescription(appelOffreDTO.getDescription());
        appelOffre.setBudgetEstimatif(appelOffreDTO.getBudgetEstimatif());
        appelOffre.setDatePublication(appelOffreDTO.getDatePublication());
        appelOffre.setDateLimite(appelOffreDTO.getDateLimite());
        appelOffre.setStatut(appelOffreDTO.getStatut());
        
        return entityMapper.toDTO(appelOffreRepository.save(appelOffre));
    }

    public Optional<AppelOffreDTO> update(Long id, AppelOffreDTO appelOffreDTO) {
        return appelOffreRepository.findById(id).map(existing -> {
            existing.setTitre(appelOffreDTO.getTitre());
            existing.setDescription(appelOffreDTO.getDescription());
            existing.setBudgetEstimatif(appelOffreDTO.getBudgetEstimatif());
            existing.setDatePublication(appelOffreDTO.getDatePublication());
            existing.setDateLimite(appelOffreDTO.getDateLimite());
            existing.setStatut(appelOffreDTO.getStatut());
            return entityMapper.toDTO(appelOffreRepository.save(existing));
        });
    }

    public boolean delete(Long id) {
        return appelOffreRepository.findById(id).map(appelOffre -> {
            appelOffreRepository.delete(appelOffre);
            return true;
        }).orElse(false);
    }
}
