package com.btp.service;

import com.btp.dto.RoleDTO;
import com.btp.entity.Role;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.RoleRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // FIX: Injected UserRepository to fetch the current user
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<RoleDTO> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public RoleDTO findById(Long id) {
        return roleRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Transactional
    public RoleDTO save(@Valid RoleDTO roleDTO) {
        Role role = entityMapper.toEntity(roleDTO);
        // Get the username of the currently authenticated user from the security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Fetch the full User entity from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user '" + username + "' not found in database."));
        // Set the creator of the new role
        role.setCreatedBy(currentUser);
        Role savedRole = roleRepository.save(role);
        return entityMapper.toDTO(savedRole);
    }

    @Transactional
    public RoleDTO update(Long id, @Valid RoleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setNom(roleDTO.getNom());
                    existingRole.setDescription(roleDTO.getDescription());

                    Role updatedRole = roleRepository.save(existingRole);
                    return entityMapper.toDTO(updatedRole);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}