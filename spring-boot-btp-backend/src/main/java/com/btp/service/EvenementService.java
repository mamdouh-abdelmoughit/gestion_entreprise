package com.btp.service;

import com.btp.dto.EvenementDTO;
import com.btp.entity.Evenement;
import com.btp.mapper.EntityMapper;
import com.btp.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<EvenementDTO> findAll() {
        return evenementRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EvenementDTO> findById(Long id) {
        return evenementRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public EvenementDTO save(EvenementDTO evenementDTO) {
        Evenement evenement = entityMapper.toEntity(evenementDTO);
        Evenement savedEvenement = evenementRepository.save(evenement);
        return entityMapper.toDTO(savedEvenement);
    }

    public EvenementDTO update(Long id, EvenementDTO evenementDTO) {
        return evenementRepository.findById(id)
                .map(existingEvenement -> {
                    existingEvenement.setTitre(evenementDTO.getTitre());
                    existingEvenement.setDescription(evenementDTO.getDescription());
                    existingEvenement.setDateEvenement(evenementDTO.getDateEvenement());
                    existingEvenement.setLieu(evenementDTO.getLieu());
                    existingEvenement.setType(evenementDTO.getType());
                    existingEvenement.setStatut(evenementDTO.getStatut());
                    
                    Evenement updatedEvenement = evenementRepository.save(existingEvenement);
                    return entityMapper.toDTO(updatedEvenement);
                })
                .orElseThrow(() -> new RuntimeException("Evenement not found with id: " + id));
    }

    public void deleteById(Long id) {
        evenementRepository.deleteById(id);
    }
}
