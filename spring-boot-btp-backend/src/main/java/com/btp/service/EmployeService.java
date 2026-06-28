package com.btp.service;

import com.btp.dto.EmployeDTO;
import com.btp.entity.Employe;
import com.btp.entity.Organization;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.exception.UnauthorizedException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AffectationEmployeRepository;
import com.btp.repository.DepenseRepository;
import com.btp.repository.DocumentRepository;
import com.btp.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.btp.entity.User;
import com.btp.repository.UserRepository;

import jakarta.validation.Valid;

import java.time.LocalDate;

@Service
@Transactional
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AffectationEmployeRepository affectationEmployeRepository;

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TenantAwareService tenantAwareService;

    /**
     * Find all employees - filtered by organization for multi-tenancy.
     */
    @Transactional(readOnly = true)
    public Page<EmployeDTO> findAll(Pageable pageable) {
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null) {
            return employeRepository.findByOrganizationId(orgId, pageable).map(entityMapper::toDTO);
        }
        return employeRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && employe.getOrganization() != null 
                && !orgId.equals(employe.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This employee belongs to another organization");
        }
        
        return entityMapper.toDTO(employe);
    }

    public EmployeDTO save(@Valid EmployeDTO employeDTO) {
        Employe employe = entityMapper.toEntity(employeDTO);

        User currentUser = tenantAwareService.getCurrentUser();
        Organization organization = tenantAwareService.getCurrentOrganization();
        
        employe.setCreatedBy(currentUser);
        employe.setOrganization(organization);

        Employe savedEmploye = employeRepository.save(employe);
        return entityMapper.toDTO(savedEmploye);
    }

    // INSIDE EmployeService.java

    public EmployeDTO update(Long id, @Valid EmployeDTO employeDTO) {
        return employeRepository.findById(id)
                .map(existingEmploye -> {
                    existingEmploye.setNom(employeDTO.getNom());
                    existingEmploye.setPrenom(employeDTO.getPrenom());
                    existingEmploye.setTelephone(employeDTO.getTelephone());
                    existingEmploye.setEmail(employeDTO.getEmail());
                    existingEmploye.setPoste(employeDTO.getPoste());
                    // FIX: Convert LocalDate from DTO to LocalDateTime for entity
                    if (employeDTO.getDateEmbauche() != null) {
                        existingEmploye.setDateEmbauche(LocalDate.from(employeDTO.getDateEmbauche().atStartOfDay()));
                    }
                    if (employeDTO.getSalaire() != null) {
                        existingEmploye.setSalaire(employeDTO.getSalaire().doubleValue());
                    }
                    // FIX: Convert String from DTO to Enum for the entity
                    if (employeDTO.getStatut() != null) {
                        existingEmploye.setStatut(Employe.StatutEmploye.valueOf(employeDTO.getStatut()));
                    }
                    existingEmploye.setAdresse(employeDTO.getAdresse());

                    Employe updatedEmploye = employeRepository.save(existingEmploye);
                    return entityMapper.toDTO(updatedEmploye);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + id));
    }

    public void deleteById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + id));
        
        // Check for dependencies
        long affectationCount = affectationEmployeRepository.countByEmploye(employe);
        long depenseCount = depenseRepository.countByEmploye(employe);
        long documentCount = documentRepository.countByEmploye(employe);
        
        if (affectationCount > 0 || depenseCount > 0 || documentCount > 0) {
            StringBuilder message = new StringBuilder("Impossible de supprimer cet employé car il est référencé par: ");
            if (affectationCount > 0) message.append(affectationCount).append(" affectation(s), ");
            if (depenseCount > 0) message.append(depenseCount).append(" dépense(s), ");
            if (documentCount > 0) message.append(documentCount).append(" document(s)");
            throw new BadRequestException(message.toString().replaceAll(", $", ""));
        }
        
        employeRepository.delete(employe);
    }

    @Transactional(readOnly = true)
    public Page<EmployeDTO> findByStatut(String statut, Pageable pageable) {
        Employe.StatutEmploye statutEnum = Employe.StatutEmploye.valueOf(statut.toUpperCase());
        return employeRepository.findByStatut(statutEnum, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<EmployeDTO> findByPoste(String poste, Pageable pageable) {
        return employeRepository.findByPoste(poste, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<EmployeDTO> findByCreatedById(Long userId, Pageable pageable) {
        return employeRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<EmployeDTO> searchByName(String keyword, Pageable pageable) {
        return employeRepository.searchByName(keyword, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<EmployeDTO> findByStatutAndCreatedBy(String statut, Long userId, Pageable pageable) {
        Employe.StatutEmploye statutEnum = Employe.StatutEmploye.valueOf(statut.toUpperCase());
        return employeRepository.findByStatutAndCreatedBy(statutEnum, userId, pageable)
                .map(entityMapper::toDTO);
    }
}
