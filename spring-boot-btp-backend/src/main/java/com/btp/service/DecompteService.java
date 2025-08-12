package com.btp.service;

import com.btp.dto.DecompteDTO;
import com.btp.entity.Decompte;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DecompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DecompteService {

    @Autowired
    private DecompteRepository decompteRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<DecompteDTO> findAll() {
        return decompteRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DecompteDTO> findById(Long id) {
        return decompteRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public DecompteDTO save(DecompteDTO decompteDTO) {
        Decompte decompte = entityMapper.toEntity(decompteDTO);
        Decompte savedDecompte = decompteRepository.save(decompte);
        return entityMapper.toDTO(savedDecompte);
    }

    public DecompteDTO update(Long id, DecompteDTO decompteDTO) {
        return decompteRepository.findById(id)
                .map(existingDecompte -> {
                    existingDecompte.setNumero(decompteDTO.getNumero());
                    existingDecompte.setDateDecompte(decompteDTO.getDateDecompte());
                    existingDecompte.setPeriodeDebut(decompteDTO.getPeriodeDebut());
                    existingDecompte.setPeriodeFin(decompteDTO.getPeriodeFin());
                    existingDecompte.setMontantTotal(decompteDTO.getMontantTotal());
                    existingDecompte.setMontantRegle(decompteDTO.getMontantRegle());
                    existingDecompte.setMontantRestant(decompteDTO.getMontantRestant());
                    existingDecompte.setStatut(decompteDTO.getStatut());
                    
                    Decompte updatedDecompte = decompteRepository.save(existingDecompte);
                    return entityMapper.toDTO(updatedDecompte);
                })
                .orElseThrow(() -> new RuntimeException("Decompte not found with id: " + id));
    }

    public void deleteById(Long id) {
        decompteRepository.deleteById(id);
    }
}
