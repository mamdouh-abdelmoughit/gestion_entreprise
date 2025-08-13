package com.btp.service;

import com.btp.dto.EmployeDTO;
import com.btp.entity.Employe;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<EmployeDTO> findAll(Pageable pageable) {
        return employeRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        return employeRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + id));
    }

    public EmployeDTO save(@Valid EmployeDTO employeDTO) {
        Employe employe = entityMapper.toEntity(employeDTO);
        Employe savedEmploye = employeRepository.save(employe);
        return entityMapper.toDTO(savedEmploye);
    }

    public EmployeDTO update(Long id, @Valid EmployeDTO employeDTO) {
        return employeRepository.findById(id)
                .map(existingEmploye -> {
                    existingEmploye.setNom(employeDTO.getNom());
                    existingEmploye.setPrenom(employeDTO.getPrenom());
                    existingEmploye.setTelephone(employeDTO.getTelephone());
                    existingEmploye.setEmail(employeDTO.getEmail());
                    existingEmploye.setPoste(employeDTO.getPoste());
                    existingEmploye.setDateEmbauche(employeDTO.getDateEmbauche());
                    existingEmploye.setSalaire(employeDTO.getSalaire());
                    existingEmploye.setStatut(employeDTO.getStatut());
                    existingEmploye.setAdresse(employeDTO.getAdresse());
                    
                    Employe updatedEmploye = employeRepository.save(existingEmploye);
                    return entityMapper.toDTO(updatedEmploye);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + id));
    }

    public void deleteById(Long id) {
        employeRepository.deleteById(id);
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
