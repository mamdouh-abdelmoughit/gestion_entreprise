package com.btp.service;

import com.btp.dto.ProjetDTO;
import com.btp.entity.Projet;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UserRepository userRepository;
    // Assuming a ClientRepository exists for the client relationship
    // @Autowired
    // private ClientRepository clientRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<ProjetDTO> findAll(Pageable pageable) {
        return projetRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProjetDTO findById(Long id) {
        return projetRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + id));
    }

    @Transactional
    public ProjetDTO save(@Valid ProjetDTO projetDTO) {
        Projet projet = entityMapper.toEntity(projetDTO);
        updateRelationships(projet, projetDTO);
        Projet savedProjet = projetRepository.save(projet);
        return entityMapper.toDTO(savedProjet);
    }

    @Transactional
    public ProjetDTO update(Long id, @Valid ProjetDTO projetDTO) {
        return projetRepository.findById(id)
                .map(existingProjet -> {
                    existingProjet.setNom(projetDTO.getNom());
                    existingProjet.setDescription(projetDTO.getDescription());
                    existingProjet.setDateDebut(projetDTO.getDateDebut());
                    existingProjet.setDateFin(projetDTO.getDateFin());
                    existingProjet.setBudget(projetDTO.getBudget());
                    existingProjet.setStatut(projetDTO.getStatut());
                    existingProjet.setAdresse(projetDTO.getAdresse());

                    updateRelationships(existingProjet, projetDTO);
                    
                    Projet updatedProjet = projetRepository.save(existingProjet);
                    return entityMapper.toDTO(updatedProjet);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        projetRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findByChefProjet(Long chefProjetId, Pageable pageable) {
        User chefProjet = userRepository.findById(chefProjetId)
                .orElseThrow(() -> new ResourceNotFoundException("User (ChefProjet) not found with id: " + chefProjetId));
        return projetRepository.findByChefProjet(chefProjet, pageable)
            .map(entityMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findByStatut(String statut, Pageable pageable) {
        Projet.StatutProjet statutEnum = Projet.StatutProjet.valueOf(statut.toUpperCase());
        return projetRepository.findByStatut(statutEnum, pageable)
            .map(entityMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findByStatutAndChefProjet(String statut, Long chefProjetId, Pageable pageable){
        Projet.StatutProjet statutEnum = Projet.StatutProjet.valueOf(statut.toUpperCase());
        User chefProjet = userRepository.findById(chefProjetId)
                .orElseThrow(() -> new ResourceNotFoundException("User (ChefProjet) not found with id: " + chefProjetId));
        return projetRepository.findByStatutAndChefProjet(statutEnum, chefProjet, pageable)
                .map(entityMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findActiveProjects(Pageable pageable) {
        return projetRepository.findActiveProjects(pageable)
                .map(entityMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findByCreatedById(Long userId, Pageable pageable) {
        return projetRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    private void updateRelationships(Projet projet, ProjetDTO projetDTO) {
        if (projetDTO.getResponsableId() != null) {
            User responsable = userRepository.findById(projetDTO.getResponsableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsable (User) not found with id: " + projetDTO.getResponsableId()));
            projet.setChefProjet(responsable);
        }
        // Assuming a Client entity and repository
        // if (projetDTO.getClientId() != null) {
        //     Client client = clientRepository.findById(projetDTO.getClientId())
        //             .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + projetDTO.getClientId()));
        //     projet.setClient(client);
        // }
    }
}