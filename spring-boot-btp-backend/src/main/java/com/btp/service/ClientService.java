package com.btp.service;

import com.btp.dto.ClientDTO;
import com.btp.entity.Client;
import com.btp.entity.Organization;
import com.btp.entity.User;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.exception.UnauthorizedException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.ClientRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private TenantAwareService tenantAwareService;

    /**
     * Find all clients - filtered by organization for multi-tenancy.
     */
    public Page<ClientDTO> findAll(Pageable pageable) {
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null) {
            // Filter by organization
            return clientRepository.findByOrganizationId(orgId, pageable).map(entityMapper::toDTO);
        }
        // Fallback: If no organization (legacy data), return all
        return clientRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    /**
     * Find client by ID - verify organization access.
     */
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && client.getOrganization() != null 
                && !orgId.equals(client.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This client belongs to another organization");
        }
        
        return entityMapper.toDTO(client);
    }

    public ClientDTO save(ClientDTO clientDTO) {
        Client client = entityMapper.toEntity(clientDTO);
        User currentUser = tenantAwareService.getCurrentUser();
        Organization organization = tenantAwareService.getCurrentOrganization();
        
        client.setCreatedBy(currentUser);
        client.setOrganization(organization);
        
        return entityMapper.toDTO(clientRepository.save(client));
    }

    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && existingClient.getOrganization() != null 
                && !orgId.equals(existingClient.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This client belongs to another organization");
        }
        
        existingClient.setNom(clientDTO.getNom());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setTelephone(clientDTO.getTelephone());
        existingClient.setAdresse(clientDTO.getAdresse());
        
        return entityMapper.toDTO(clientRepository.save(existingClient));
    }

    public void deleteById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        // Verify organization access
        Long orgId = tenantAwareService.getCurrentOrganizationId();
        if (orgId != null && client.getOrganization() != null 
                && !orgId.equals(client.getOrganization().getId())) {
            throw new UnauthorizedException("Access denied: This client belongs to another organization");
        }
        
        // Check if client has associated projects
        long projectCount = projetRepository.countByClient(client);
        if (projectCount > 0) {
            throw new BadRequestException("Impossible de supprimer ce client car il est associé à " + projectCount + " projet(s). Veuillez d'abord supprimer ou réassigner les projets.");
        }
        
        clientRepository.delete(client);
    }
}