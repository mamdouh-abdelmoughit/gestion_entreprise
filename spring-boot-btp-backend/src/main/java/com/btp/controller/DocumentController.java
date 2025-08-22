package com.btp.controller;

import com.btp.dto.DocumentDTO;
import com.btp.service.DocumentService;
import com.btp.service.FileStorageService; // 1. Import new service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // 2. Import MultipartFile

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileStorageService fileStorageService; // 3. Inject new service

    // --- START OF REFACTORED POST METHOD ---
    // This new method replaces your old @PostMapping.
    // It accepts multipart/form-data instead of JSON.
    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("type") String type,
            @RequestParam("description") String description) {

        // 1. Save the file to disk
        String filename = fileStorageService.save(file);

        // 2. Create DTO with metadata
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setNom(nom);
        documentDTO.setType(type);
        documentDTO.setDescription(description);
        documentDTO.setFichier(filename);
        documentDTO.setTaille(file.getSize());
        documentDTO.setDateUpload(LocalDate.from(LocalDateTime.now()));

        // 3. Save metadata to the database
        DocumentDTO savedDocument = documentService.save(documentDTO);

        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }
    // --- END OF REFACTORED POST METHOD ---

    // All your other existing methods remain the same and are still valid.
    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDTO documentDTO) {
        // Note: This only updates metadata. A separate endpoint would be needed to replace the file.
        return ResponseEntity.ok(documentService.update(id, documentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            Resource resource = documentService.loadAsResource(id);
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}