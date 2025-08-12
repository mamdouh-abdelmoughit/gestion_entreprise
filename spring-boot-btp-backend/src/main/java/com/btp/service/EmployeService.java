package com.btp.service;

import com.btp.dto.EmployeDTO;
import com.btp.entity.Employe;
import com.btp.mapper.EntityMapper;
import com.btp.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<EmployeDTO> findAll() {
        return employeRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmployeDTO> findById(Long id) {
        return employeRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public EmployeDTO save(EmployeDTO employeDTO) {
        Employe employe = entityMapper.toEntity(employeDTO);
        Employe savedEmploye = employeRepository.save(employe);
        return entityMapper.toDTO(savedEmploye);
    }

    public EmployeDTO update(Long id, EmployeDTO employeDTO) {
        return employeRepository.findById(id)
                .map(existingEmploye -> {
                    existingEmploye.setNom(employeDTO.getNom());
                    existingEmploye.setPrenom(employeDTO.getPrenom());
                    existingEmploye.setCin(employeDTO.getCin());
                    existingEmploye.setTelephone(employeDTO.getTelephone());
                    existingEmploye.setEmail(employeDTO.getEmail());
                    existingEmploye.setPoste(employeDTO.getPoste());
                    existingEmploye.setDateEmbauche(employeDTO.getDateEmbauche());
                    existingEmploye.setSalaire(employeDTO.getSalaire());
                    existingEmploye.setStatut(employeDTO.getStatut());
                    existingEmploye.setAdresse(employeDTO.getAdresse());
                    existingEmploye.setCompetences(employeDTO.getCompetences());
                    
                    Employe updatedEmploye = employeRepository.save(existingEmploye);
                    return entityMapper.toDTO(updatedEmploye);
                })
                .orElseThrow(() -> new RuntimeException("Employe not found with id: " + id));
    }

    public void deleteById(Long id) {
        employeRepository.deleteById(id);
    }
}
