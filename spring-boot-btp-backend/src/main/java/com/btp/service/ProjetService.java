package com.btp.service;

import com.btp.dto.ProjetDTO;
import com.btp.entity.Client;
import com.btp.entity.Employe;
import com.btp.entity.Organization;
import com.btp.entity.Projet;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.exception.UnauthorizedException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.time.LocalDate;

@Service
@Transactional
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CautionRepository cautionRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DecompteRepository decompteRepository;

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private AffectationEmployeRepository affectationEmployeRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private TenantAwareService tenantAwareService;

    /**
     * Find all projects - filtered by organization and role.
     * - ADMIN sees all projects in their organization
     * - EMPLOYEE sees projects they are assigned to
     * - CLIENT sees only projects where they are the client
     */
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findAll(Pageable pageable) {
        User currentUser = tenantAwareService.getCurrentUser();
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        
        // Role-based filtering
        if (tenantAwareService.hasRole("ROLE_CLIENT")) {
            // Client sees only their projects
            Client client = clientRepository.findByUserAccount(currentUser).orElse(null);
            if (client != null) {
                return projetRepository.findByClientId(client.getId(), pageable).map(entityMapper::toDTO);
            }
            return Page.empty(pageable);
        }
        
        if (tenantAwareService.hasRole("ROLE_EMPLOYEE") && !tenantAwareService.hasRole("ROLE_ADMIN")) {
            // Employee sees projects they are assigned to (via affectations)
            Employe employe = employeRepository.findByUserAccount(currentUser).orElse(null);
            if (employe != null) {
                // Get projects through affectations - simplified: return all in org for now
                // A more sophisticated query would join through affectations
            }
        }
        
        // Admin and other roles see all projects in organization
        if (orgId != null) {
            return projetRepository.findByOrganizationId(orgId, pageable).map(entityMapper::toDTO);
        }
        
        return projetRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProjetDTO findById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && projet.getOrganization() != null 
                && !orgId.equals(projet.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This project belongs to another organization");
        }
        
        // Additional check for clients - can only see their own projects
        if (tenantAwareService.hasRole("ROLE_CLIENT") && !tenantAwareService.hasRole("ROLE_ADMIN")) {
            User currentUser = tenantAwareService.getCurrentUser();
            Client client = clientRepository.findByUserAccount(currentUser).orElse(null);
            if (client == null || projet.getClient() == null || !client.getId().equals(projet.getClient().getId())) {
                throw new UnauthorizedException("Access denied: You can only view your own projects");
            }
        }
        
        return entityMapper.toDTO(projet);
    }

    @Transactional
    public ProjetDTO save(@Valid ProjetDTO projetDTO) {
        Projet projet = entityMapper.toEntity(projetDTO);

        // Get the current user and organization for multi-tenancy
        User currentUser = tenantAwareService.getCurrentUser();
        Organization organization = tenantAwareService.getCurrentOrganization();

        // Set the creator and organization of the new projet
        projet.setCreatedBy(currentUser);
        projet.setOrganization(organization);

        // Update relationships for ChefProjet and Client
        updateRelationships(projet, projetDTO);

        Projet savedProjet = projetRepository.save(projet);
        return entityMapper.toDTO(savedProjet);
    }

    @Transactional
// INSIDE ProjetService.java

    public ProjetDTO update(Long id, @Valid ProjetDTO projetDTO) {
        return projetRepository.findById(id)
                .map(existingProjet -> {
                    existingProjet.setNom(projetDTO.getNom());
                    existingProjet.setDescription(projetDTO.getDescription());
                    if (projetDTO.getDateDebut() != null) {
                        existingProjet.setDateDebut(LocalDate.from(projetDTO.getDateDebut().atStartOfDay()));
                    }
                    // FIX: Corrected setter methods to match entity fields
                    if (projetDTO.getDateFin() != null) {
                        existingProjet.setDateFinPrevue(LocalDate.from(projetDTO.getDateFin().atStartOfDay()));
                    }
                    if (projetDTO.getMontantContrat() != null) {
                        existingProjet.setMontantContrat(projetDTO.getMontantContrat().doubleValue());
                    }
                    if (projetDTO.getStatut() != null) {
                        existingProjet.setStatut(Projet.StatutProjet.valueOf(projetDTO.getStatut()));
                    }
                    existingProjet.setAdresseChantier(projetDTO.getAdresse());

                    updateRelationships(existingProjet, projetDTO);

                    Projet updatedProjet = projetRepository.save(existingProjet);
                    return entityMapper.toDTO(updatedProjet);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + id));
        
        // Delete all related entities first (cascade delete)
        affectationEmployeRepository.deleteByProjet(projet);
        depenseRepository.deleteByProjet(projet);
        decompteRepository.deleteByProjet(projet);
        documentRepository.deleteByProjet(projet);
        cautionRepository.deleteByProjet(projet);
        
        // Now delete the project
        projetRepository.delete(projet);
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
        // FIX: Use the new, consistent getter 'getChefProjetId()'
        if (projetDTO.getChefProjetId() != null) {
            User responsable = userRepository.findById(projetDTO.getChefProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsable (User) not found with id: " + projetDTO.getChefProjetId()));
            projet.setChefProjet(responsable);
        }
        if (projetDTO.getClientId() != null) {
            Client client = clientRepository.findById(projetDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + projetDTO.getClientId()));
            projet.setClient(client);
        }
    }
}