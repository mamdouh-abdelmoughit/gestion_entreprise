package com.btp.service;

import com.btp.dto.EvenementDTO;
import com.btp.entity.Evenement;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.EvenementRepository;
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
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<EvenementDTO> findAll(Pageable pageable) {
        return evenementRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EvenementDTO findById(Long id) {
        return evenementRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Evenement not found with id: " + id));
    }

    @Transactional
    public EvenementDTO save(@Valid EvenementDTO evenementDTO) {
        Evenement evenement = entityMapper.toEntity(evenementDTO);
        updateRelationships(evenement, evenementDTO);
        Evenement savedEvenement = evenementRepository.save(evenement);
        return entityMapper.toDTO(savedEvenement);
    }

    @Transactional
    public EvenementDTO update(Long id, @Valid EvenementDTO evenementDTO) {
        return evenementRepository.findById(id)
                .map(existingEvenement -> {
                    existingEvenement.setTitre(evenementDTO.getTitre());
                    existingEvenement.setDescription(evenementDTO.getDescription());
                    existingEvenement.setDateEvenement(evenementDTO.getDateEvenement());
                    existingEvenement.setLieu(evenementDTO.getLieu());
                    existingEvenement.setType(evenementDTO.getType());
                    existingEvenement.setStatut(evenementDTO.getStatut());
                    
                    updateRelationships(existingEvenement, evenementDTO);

                    Evenement updatedEvenement = evenementRepository.save(existingEvenement);
                    return entityMapper.toDTO(updatedEvenement);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Evenement not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        evenementRepository.deleteById(id);
    }

    private void updateRelationships(Evenement evenement, EvenementDTO evenementDTO) {
        if (evenementDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(evenementDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + evenementDTO.getProjetId()));
            evenement.setProjet(projet);
        }
    }
}
