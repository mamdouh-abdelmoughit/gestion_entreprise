package com.btp.service;

import com.btp.dto.DocumentDTO;
import com.btp.entity.AppelOffre; // 1. Import AppelOffre
import com.btp.entity.Document;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.mapper.EntityMapper;
import com.btp.repository.AppelOffreRepository; // 2. Import AppelOffreRepository
import com.btp.repository.DocumentRepository;
import com.btp.repository.EmployeRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AppelOffreRepository appelOffreRepository; // 3. Inject AppelOffreRepository

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable).map(entityMapper::toDTO);
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        document.setCreatedBy(currentUser);

        updateRelationships(document, documentDTO);
        Document savedDocument = documentRepository.save(document);
        return entityMapper.toDTO(savedDocument);
    }

    @Transactional
    public DocumentDTO update(Long id, @Valid DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setNom(documentDTO.getNom());
                    if (documentDTO.getType() != null) {
                        existingDocument.setType(Document.TypeDocument.valueOf(documentDTO.getType()));
                    }
                    existingDocument.setDescription(documentDTO.getDescription());

                    updateRelationships(existingDocument, documentDTO);

                    return entityMapper.toDTO(documentRepository.save(existingDocument));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    // --- START OF THE FINAL FIX ---
    // This is the single, correct version of the method.
    // It uses the FileStorageService to cleanly separate concerns.
    @Transactional(readOnly = true)
    public Resource loadAsResource(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        return fileStorageService.load(document.getFichier());
    }

    // This private helper method is now updated to handle all possible relationships.
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

        // Your Document entity can also be linked to an AppelOffre.
        // We need to handle this relationship as well.
        if (documentDTO.getAppelOffreId() != null) {
            AppelOffre appelOffre = appelOffreRepository.findById(documentDTO.getAppelOffreId())
                    .orElseThrow(() -> new ResourceNotFoundException("AppelOffre not found with id: " + documentDTO.getAppelOffreId()));
            document.setAppelOffre(appelOffre);
        }
    }
    // --- END OF THE FINAL FIX ---
}