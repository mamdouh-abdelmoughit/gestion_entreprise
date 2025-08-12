package com.btp.service;

import com.btp.dto.FournisseurDTO;
import com.btp.entity.Fournisseur;
import com.btp.mapper.EntityMapper;
import com.btp.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<FournisseurDTO> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<FournisseurDTO> findById(Long id) {
        return fournisseurRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public FournisseurDTO save(FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = entityMapper.toEntity(fournisseurDTO);
        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        return entityMapper.toDTO(savedFournisseur);
    }

    public Optional<FournisseurDTO> update(Long id, FournisseurDTO fournisseurDTO) {
        return fournisseurRepository.findById(id)
                .map(existing -> {
                    existing.setNom(fournisseurDTO.getNom());
                    existing.setType(fournisseurDTO.getType());
                    existing.setContact(fournisseurDTO.getContact());
                    existing.setTelephone(fournisseurDTO.getTelephone());
                    existing.setEmail(fournisseurDTO.getEmail());
                    existing.setAdresse(fournisseurDTO.getAdresse());
                    existing.setIce(fournisseurDTO.getIce());
                    existing.setRc(fournisseurDTO.getRc());
                    existing.setSpecialites(fournisseurDTO.getSpecialites());
                    existing.setStatut(fournisseurDTO.getStatut());
                    Fournisseur updatedFournisseur = fournisseurRepository.save(existing);
                    return entityMapper.toDTO(updatedFournisseur);
                });
    }

    public boolean delete(Long id) {
        return fournisseurRepository.findById(id)
                .map(fournisseur -> {
                    fournisseurRepository.delete(fournisseur);
                    return true;
                })
                .orElse(false);
    }

    public List<FournisseurDTO> findByType(Fournisseur.TypeFournisseur type) {
        return fournisseurRepository.findByType(type).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FournisseurDTO> findByStatut(Fournisseur.StatutFournisseur statut) {
        return fournisseurRepository.findByStatut(statut).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }
}
