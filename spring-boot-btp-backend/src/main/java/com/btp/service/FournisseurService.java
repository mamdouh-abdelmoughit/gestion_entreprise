package com.btp.service;

import com.btp.dto.FournisseurDTO;
import com.btp.entity.Fournisseur;
import com.btp.entity.Organization;
import com.btp.entity.User;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.exception.UnauthorizedException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.CautionRepository;
import com.btp.repository.DepenseRepository;
import com.btp.repository.FournisseurRepository;
import com.btp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
@Transactional
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CautionRepository cautionRepository;

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private TenantAwareService tenantAwareService;

    /**
     * Find all fournisseurs - filtered by organization for multi-tenancy.
     */
    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findAll(Pageable pageable) {
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null) {
            return fournisseurRepository.findByOrganizationId(orgId, pageable).map(entityMapper::toDTO);
        }
        return fournisseurRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public FournisseurDTO findById(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && fournisseur.getOrganization() != null 
                && !orgId.equals(fournisseur.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This fournisseur belongs to another organization");
        }
        
        return entityMapper.toDTO(fournisseur);
    }

    public FournisseurDTO save(@Valid FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = entityMapper.toEntity(fournisseurDTO);

        User currentUser = tenantAwareService.getCurrentUser();
        Organization organization = tenantAwareService.getCurrentOrganization();
        
        fournisseur.setCreatedBy(currentUser);
        fournisseur.setOrganization(organization);

        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        return entityMapper.toDTO(savedFournisseur);
    }

    public FournisseurDTO update(Long id, @Valid FournisseurDTO fournisseurDTO) {
        Fournisseur existing = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && existing.getOrganization() != null 
                && !orgId.equals(existing.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This fournisseur belongs to another organization");
        }
        
        existing.setNom(fournisseurDTO.getNom());
        existing.setTelephone(fournisseurDTO.getTelephone());
        existing.setEmail(fournisseurDTO.getEmail());
        existing.setAdresse(fournisseurDTO.getAdresse());
        if (fournisseurDTO.getSpecialites() != null) {
            existing.setSpecialites(new java.util.ArrayList<>(fournisseurDTO.getSpecialites()));
        }
        if (fournisseurDTO.getStatut() != null) {
            existing.setStatut(Fournisseur.StatutFournisseur.valueOf(fournisseurDTO.getStatut()));
        }
        Fournisseur updatedFournisseur = fournisseurRepository.save(existing);
        return entityMapper.toDTO(updatedFournisseur);
    }

    public void deleteById(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && fournisseur.getOrganization() != null 
                && !orgId.equals(fournisseur.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This fournisseur belongs to another organization");
        }
        
        // Check for dependencies
        long cautionCount = cautionRepository.countByFournisseur(fournisseur);
        long depenseCount = depenseRepository.countByFournisseur(fournisseur);
        
        if (cautionCount > 0 || depenseCount > 0) {
            StringBuilder message = new StringBuilder("Impossible de supprimer ce fournisseur car il est référencé par: ");
            if (cautionCount > 0) message.append(cautionCount).append(" caution(s), ");
            if (depenseCount > 0) message.append(depenseCount).append(" dépense(s)");
            throw new BadRequestException(message.toString().replaceAll(", $", ""));
        }
        
        fournisseurRepository.delete(fournisseur);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByType(String type, Pageable pageable) {
        Fournisseur.TypeFournisseur typeEnum = Fournisseur.TypeFournisseur.valueOf(type.toUpperCase());
        return fournisseurRepository.findByType(typeEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByStatut(String statut, Pageable pageable) {
        Fournisseur.StatutFournisseur statutEnum = Fournisseur.StatutFournisseur.valueOf(statut.toUpperCase());
        return fournisseurRepository.findByStatut(statutEnum, pageable).map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByCreatedById(Long userId, Pageable pageable) {
        return fournisseurRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByIce(String ice, Pageable pageable) {
        return fournisseurRepository.findByIce(ice, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByRc(String rc, Pageable pageable) {
        return fournisseurRepository.findByRc(rc, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> searchByName(String keyword, Pageable pageable) {
        return fournisseurRepository.searchByName(keyword, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findBySpecialite(String specialite, Pageable pageable) {
        return fournisseurRepository.findBySpecialite(specialite, pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FournisseurDTO> findByStatutAndCreatedBy(String statut, Long userId, Pageable pageable) {
        Fournisseur.StatutFournisseur statutEnum = Fournisseur.StatutFournisseur.valueOf(statut.toUpperCase());
        return fournisseurRepository.findByStatutAndCreatedBy(statutEnum, userId, pageable)
                .map(entityMapper::toDTO);
    }
}
