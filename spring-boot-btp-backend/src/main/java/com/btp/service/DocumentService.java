package com.btp.service;

import com.btp.dto.DocumentDTO;
import com.btp.entity.Document;
import com.btp.mapper.EntityMapper;
import com.btp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityMapper entityMapper;

    public List<DocumentDTO> findAll() {
        return documentRepository.findAll().stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DocumentDTO> findById(Long id) {
        return documentRepository.findById(id)
                .map(entityMapper::toDTO);
    }

    public DocumentDTO save(DocumentDTO documentDTO) {
        Document document = entityMapper.toEntity(documentDTO);
        Document savedDocument = documentRepository.save(document);
        return entityMapper.toDTO(savedDocument);
    }

    public DocumentDTO update(Long id, DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setNom(documentDTO.getNom());
                    existingDocument.setType(documentDTO.getType());
                    existingDocument.setCheminFichier(documentDTO.getCheminFichier());
                    existingDocument.setDateUpload(documentDTO.getDateUpload());
                    existingDocument.setTaille(documentDTO.getTaille());
                    
                    Document updatedDocument = documentRepository.save(existingDocument);
                    return entityMapper.toDTO(updatedDocument);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }
}
