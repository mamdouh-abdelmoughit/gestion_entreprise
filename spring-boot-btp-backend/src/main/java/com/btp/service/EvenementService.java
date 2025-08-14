package com.btp.service;

import com.btp.dto.EvenementDTO;
import com.btp.entity.Evenement;
import com.btp.entity.Projet;
import com.btp.entity.User; // FIX: Added import for User
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.EvenementRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository; // FIX: Added import for UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder; // FIX: Added import for SecurityContextHolder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
@Transactional
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private ProjetRepository projetRepository;

    // FIX: Injected UserRepository to fetch the current user
    @Autowired
    private UserRepository userRepository;

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
        // Get the username of the currently authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Fetch the full User entity
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user '" + username + "' not found in database."));
        // Set the user who triggered the event
        evenement.setUser(currentUser);
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
                    if (evenementDTO.getType() != null) {
                        existingEvenement.setType(Evenement.TypeEvenement.valueOf(evenementDTO.getType()));
                    }

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