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
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.btp.entity.User; // 1. Import User
import com.btp.repository.UserRepository; // 2. Import UserRepository

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.validation.Valid;

import java.net.MalformedURLException;
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
    private UserRepository userRepository;

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

        // --- START OF THE FINAL FIX ---
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        document.setCreatedBy(currentUser);
        // --- END OF THE FINAL FIX ---

        updateRelationships(document, documentDTO);
        Document savedDocument = documentRepository.save(document);
        return entityMapper.toDTO(savedDocument);
    }


    @Transactional
    // INSIDE DocumentService.java

    public DocumentDTO update(Long id, @Valid DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setNom(documentDTO.getNom());
                    if (documentDTO.getType() != null) {
                        existingDocument.setType(Document.TypeDocument.valueOf(documentDTO.getType()));
                    }
                    // FIX: Corrected setter methods to match entity fields
                    existingDocument.setFichier(documentDTO.getFichier());
                    existingDocument.setTailleFichier(documentDTO.getTaille());
                    existingDocument.setDateUpload(documentDTO.getDateUpload());
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
        public Resource loadAsResource(Long documentId) {
        // 1. Find the document metadata in the database
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            throw new RuntimeException("Document not found with ID: " + documentId);
        }
        Document document = documentOptional.get();

        try {
            // 2. Resolve the absolute file path from the stored chemin
            Path filePath = Paths.get(document.getFichier()).toAbsolutePath().normalize();
            
            // 3. Create a URL resource from the file path
            Resource resource = new UrlResource(filePath.toUri());

            // 4. Check if the resource exists and is readable
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or is not readable: " + document.getFichier());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error resolving file path: " + document.getFichier(), e);
        }
    }
}
