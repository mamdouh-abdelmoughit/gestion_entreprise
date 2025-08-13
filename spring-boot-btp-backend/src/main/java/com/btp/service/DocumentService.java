package com.btp.service;

import com.btp.dto.DocumentDTO;
import com.btp.entity.Document;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DocumentRepository;
import com.btp.repository.EmployeRepository;
import com.btp.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable)
                .map(entityMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public DocumentDTO findById(Long id) {
        return documentRepository.findById(id)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
    }

    @Transactional
    public DocumentDTO save(@Valid DocumentDTO documentDTO) {
        Document document = entityMapper.toEntity(documentDTO);
        updateRelationships(document, documentDTO);
        Document savedDocument = documentRepository.save(document);
        return entityMapper.toDTO(savedDocument);
    }

    @Transactional
    public DocumentDTO update(Long id, @Valid DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setNom(documentDTO.getNom());
                    existingDocument.setType(documentDTO.getType());
                    existingDocument.setChemin(documentDTO.getChemin());
                    existingDocument.setDateUpload(documentDTO.getDateUpload());
                    existingDocument.setTaille(documentDTO.getTaille());
                    existingDocument.setDescription(documentDTO.getDescription());
                    
                    updateRelationships(existingDocument, documentDTO);

                    Document updatedDocument = documentRepository.save(existingDocument);
                    return entityMapper.toDTO(updatedDocument);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    private void updateRelationships(Document document, DocumentDTO documentDTO) {
        if (documentDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(documentDTO.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found with id: " + documentDTO.getProjetId()));
            document.setProjet(projet);
        }

        if (documentDTO.getEmployeId() != null) {
            Employe employe = employeRepository.findById(documentDTO.getEmployeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employe not found with id: " + documentDTO.getEmployeId()));
            document.setEmploye(employe);
        }
    }
}
