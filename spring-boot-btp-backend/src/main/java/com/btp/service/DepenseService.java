package com.btp.service;

import com.btp.dto.DepenseDTO;
import com.btp.entity.Depense;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DepenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<DepenseDTO> findAll() {
        return depenseRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DepenseDTO> findById(Long id) {
        return depenseRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public DepenseDTO save(DepenseDTO depenseDTO) {
        Depense depense = entityMapper.toEntity(depenseDTO);
        Depense savedDepense = depenseRepository.save(depense);
        return entityMapper.toDTO(savedDepense);
    }

    public DepenseDTO update(Long id, DepenseDTO depenseDTO) {
        return depenseRepository.findById(id)
                .map(existingDepense -> {
                    existingDepense.setNumero(depenseDTO.getNumero());
                    existingDepense.setDescription(depenseDTO.getDescription());
                    existingDepense.setMontant(depenseDTO.getMontant());
                    existingDepense.setDateDepense(depenseDTO.getDateDepense());
                    existingDepense.setTypeDepense(depenseDTO.getTypeDepense());
                    existingDepense.setStatut(depenseDTO.getStatut());
                    
                    Depense updatedDepense = depenseRepository.save(existingDepense);
                    return entityMapper.toDTO(updatedDepense);
                })
                .orElseThrow(() -> new RuntimeException("Depense not found with id: " + id));
    }

    public void deleteById(Long id) {
        depenseRepository.deleteById(id);
    }
}
