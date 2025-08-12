package com.btp.service;

import com.btp.dto.ProjetDTO;
import com.btp.entity.Projet;
import com.btp.mapper.EntityMapper;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<ProjetDTO> findAll() {
        return projetRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProjetDTO> findById(Long id) {
        return projetRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public ProjetDTO save(ProjetDTO projetDTO) {
        Projet projet = entityMapper.toEntity(projetDTO);
        Projet savedProjet = projetRepository.save(projet);
        return entityMapper.toDTO(savedProjet);
    }

    public ProjetDTO update(Long id, ProjetDTO projetDTO) {
        return projetRepository.findById(id)
                .map(existingProjet -> {
                    existingProjet.setNom(projetDTO.getNom());
                    existingProjet.setDescription(projetDTO.getDescription());
                    existingProjet.setDateDebut(projetDTO.getDateDebut());
                    existingProjet.setDateFin(projetDTO.getDateFin());
                    existingProjet.setBudget(projetDTO.getBudget());
                    existingProjet.setStatut(projetDTO.getStatut());
                    existingProjet.setAdresse(projetDTO.getAdresse());
                    existingProjet.setVille(projetDTO.getVille());
                    existingProjet.setCodePostal(projetDTO.getCodePostal());
                    existingProjet.setPays(projetDTO.getPays());
                    
                    Projet updatedProjet = projetRepository.save(existingProjet);
                    return entityMapper.toDTO(updatedProjet);
                })
                .orElseThrow(() -> new RuntimeException("Projet not found with id: " + id));
    }

    public void deleteById(Long id) {
        projetRepository.deleteById(id);
    }
}
