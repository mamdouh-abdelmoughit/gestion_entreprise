package com.btp.service;

import com.btp.dto.AppelOffreDTO;
import com.btp.entity.AppelOffre;
import com.btp.entity.Fournisseur;
import com.btp.entity.Projet;
import com.btp.entity.User; // 1. Import User
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AppelOffreRepository;
import com.btp.repository.FournisseurRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository; // 2. Import UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder; // 3. Import SecurityContextHolder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class AppelOffreService {

    @Autowired
    private AppelOffreRepository appelOffreRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private UserRepository userRepository; // 4. Inject UserRepository

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findAll(Pageable pageable) {
        return appelOffreRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public AppelOffreDTO findById(Long id) {
        return appelOffreRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + id));
    }

    public AppelOffreDTO save(AppelOffreDTO appelOffreDTO) {
        System.out.println("--- INSIDE SERVICE (BEFORE MAP) ---");
        System.out.println("DTO to be mapped: " + appelOffreDTO.toString());

        AppelOffre appelOffre = entityMapper.toEntity(appelOffreDTO);
        System.out.println("--- INSIDE SERVICE (AFTER MAP) ---");
        System.out.println("Mapped Entity: " + appelOffre.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        appelOffre.setCreatedBy(currentUser);
        updateRelationships(appelOffre, appelOffreDTO);
        System.out.println("--- INSIDE SERVICE (BEFORE SAVE) ---");
        System.out.println("Entity to be saved: " + appelOffre.toString());
        AppelOffre savedAppelOffre = appelOffreRepository.save(appelOffre);
        return entityMapper.toDTO(savedAppelOffre);
    }

    public AppelOffreDTO update(Long id, AppelOffreDTO appelOffreDTO) {
        // 1. Find the existing entity in the database
        return appelOffreRepository.findById(id).map(existing -> {

            // --- START OF THE FINAL FIX ---
            // 2. Manually copy every field from the DTO to the existing entity.
            // This must be done for ALL fields that the user is allowed to change.
            existing.setNumero(appelOffreDTO.getNumero());
            existing.setIntitule(appelOffreDTO.getTitre());
            existing.setMaitreDOuvrage(appelOffreDTO.getMaitreDOuvrage());
            existing.setDescription(appelOffreDTO.getDescription());
            existing.setDatePublication(appelOffreDTO.getDatePublication());
            existing.setDateLimite(appelOffreDTO.getDateLimite());

            if (appelOffreDTO.getBudgetEstimatif() != null) {
                existing.setMontantEstime(appelOffreDTO.getBudgetEstimatif().doubleValue());
            }

            if (appelOffreDTO.getStatut() != null) {
                existing.setStatut(AppelOffre.StatutAppelOffre.valueOf(appelOffreDTO.getStatut().toUpperCase()));
            }
            // --- END OF THE FINAL FIX ---

            // 3. Update the relationships (this was already correct)
            updateRelationships(existing, appelOffreDTO);

            // 4. Save the updated entity
            AppelOffre updatedAppelOffre = appelOffreRepository.save(existing);
            return entityMapper.toDTO(updatedAppelOffre);

        }).orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + id));
    }

    public void deleteById(Long id) {
        appelOffreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByStatut(String statut, Pageable pageable) {
        AppelOffre.StatutAppelOffre statutEnum = AppelOffre.StatutAppelOffre.valueOf(statut.toUpperCase());
        return appelOffreRepository.findByStatut(statutEnum, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByCreatedById(Long userId, Pageable pageable) {
        return appelOffreRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    // Note: This custom query might need to be updated or removed if it's not working with LocalDate
    // For now, let's assume it's okay.
    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findActiveAppelsOffres(Pageable pageable) {
        return appelOffreRepository.findActiveAppelsOffres(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppelOffreDTO> findByStatutAndCreatedBy(String statut, Long userId, Pageable pageable) {
        AppelOffre.StatutAppelOffre statutEnum = AppelOffre.StatutAppelOffre.valueOf(statut.toUpperCase());
        return appelOffreRepository.findByStatutAndCreatedBy(statutEnum, userId, pageable)
                .map(entityMapper::toDTO);
    }

    private void updateRelationships(AppelOffre appelOffre, AppelOffreDTO appelOffreDTO) {
        if (appelOffreDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(appelOffreDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + appelOffreDTO.getProjetId()));
            appelOffre.setProjet(projet);
        }

        if (appelOffreDTO.getFournisseurIds() != null) {
            List<Fournisseur> fournisseurs = fournisseurRepository.findAllById(appelOffreDTO.getFournisseurIds());
            appelOffre.setFournisseurs(new HashSet<>(fournisseurs));
        }
    }
}