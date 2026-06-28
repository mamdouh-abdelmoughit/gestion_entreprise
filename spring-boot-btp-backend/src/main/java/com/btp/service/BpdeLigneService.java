package com.btp.service;

import com.btp.dto.BpdeLigneDTO;
import com.btp.entity.BpdeLigne;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.BpdeLigneRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BpdeLigneService {

    @Autowired private BpdeLigneRepository bpdeLigneRepository;
    @Autowired private ProjetRepository projetRepository;

    @Transactional(readOnly = true)
    public List<BpdeLigneDTO> findByProjet(Long projetId) {
        return bpdeLigneRepository.findByProjetId(projetId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BpdeLigneDTO findById(Long id) {
        return bpdeLigneRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("BpdeLigne not found: " + id));
    }

    public BpdeLigneDTO save(BpdeLigneDTO dto) {
        BpdeLigne entity = new BpdeLigne();
        mapDtoToEntity(dto, entity);
        return toDTO(bpdeLigneRepository.save(entity));
    }

    public BpdeLigneDTO update(Long id, BpdeLigneDTO dto) {
        BpdeLigne entity = bpdeLigneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BpdeLigne not found: " + id));
        mapDtoToEntity(dto, entity);
        return toDTO(bpdeLigneRepository.save(entity));
    }

    public void delete(Long id) {
        bpdeLigneRepository.deleteById(id);
    }

    private void mapDtoToEntity(BpdeLigneDTO dto, BpdeLigne entity) {
        entity.setDesignation(dto.getDesignation());
        entity.setUnite(dto.getUnite());
        entity.setPrixUnitaire(dto.getPrixUnitaire());
        entity.setOrdre(dto.getOrdre());
        if (dto.getProjetId() != null) {
            Projet p = projetRepository.findById(dto.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + dto.getProjetId()));
            entity.setProjet(p);
        }
    }

    private BpdeLigneDTO toDTO(BpdeLigne e) {
        BpdeLigneDTO dto = new BpdeLigneDTO();
        dto.setId(e.getId());
        dto.setDesignation(e.getDesignation());
        dto.setUnite(e.getUnite());
        dto.setPrixUnitaire(e.getPrixUnitaire());
        dto.setOrdre(e.getOrdre());
        if (e.getProjet() != null) {
            dto.setProjetId(e.getProjet().getId());
            dto.setProjetNom(e.getProjet().getNom());
        }
        return dto;
    }
}
