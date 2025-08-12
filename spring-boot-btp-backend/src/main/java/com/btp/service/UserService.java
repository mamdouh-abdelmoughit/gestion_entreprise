package com.btp.service;

import com.btp.dto.UserDTO;
import com.btp.entity.User;
import com.btp.mapper.EntityMapper;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public UserDTO save(UserDTO userDTO) {
        User user = entityMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return entityMapper.toDTO(savedUser);
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Update fields
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setFirstName(userDTO.getFirstName());
                    existingUser.setLastName(userDTO.getLastName());
                    existingUser.setTelephone(userDTO.getTelephone());
                    existingUser.setEnabled(userDTO.isEnabled());
                    existingUser.setLastLogin(userDTO.getLastLogin());
                    
                    User updatedUser = userRepository.save(existingUser);
                    return entityMapper.toDTO(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
