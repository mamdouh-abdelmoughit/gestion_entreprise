package com.btp.service;

import com.btp.dto.ClientDTO;
import com.btp.entity.Client;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.ClientRepository;
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
    private EntityMapper entityMapper;

    public Page<ClientDTO> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(entityMapper::toDTO);
    }

    public ClientDTO findById(Long id) {
        return clientRepository.findById(id).map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    public ClientDTO save(ClientDTO clientDTO) {
        Client client = entityMapper.toEntity(clientDTO);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        client.setCreatedBy(currentUser);
        return entityMapper.toDTO(clientRepository.save(client));
    }

    public ClientDTO update(Long id, ClientDTO clientDTO) {
        return clientRepository.findById(id).map(existingClient -> {
            existingClient.setNom(clientDTO.getNom());
            existingClient.setEmail(clientDTO.getEmail());
            existingClient.setTelephone(clientDTO.getTelephone());
            existingClient.setAdresse(clientDTO.getAdresse());
            return entityMapper.toDTO(clientRepository.save(existingClient));
        }).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}