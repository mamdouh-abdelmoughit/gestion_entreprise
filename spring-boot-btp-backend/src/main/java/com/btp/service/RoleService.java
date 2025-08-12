package com.btp.service;

import com.btp.dto.RoleDTO;
import com.btp.entity.Role;
import com.btp.mapper.EntityMapper;
import com.btp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RoleDTO> findById(Long id) {
        return roleRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public RoleDTO save(RoleDTO roleDTO) {
        Role role = entityMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return entityMapper.toDTO(savedRole);
    }

    public RoleDTO update(Long id, RoleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setNom(roleDTO.getNom());
                    existingRole.setDescription(roleDTO.getDescription());
                    Role updatedRole = roleRepository.save(existingRole);
                    return entityMapper.toDTO(updatedRole);
                })
                .orElse(null);
    }

    public boolean deleteById(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    return true;
                })
                .orElse(false);
    }
}
