package com.btp.controller;

import com.btp.dto.DocumentDTO;
import com.btp.service.DocumentService;
import com.btp.service.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.time.LocalDate;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Upload a document (file + metadata)
     */
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
        documentDTO.setDateUpload(LocalDate.now());

        // 3. Save metadata in DB
        DocumentDTO savedDocument = documentService.save(documentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
    }

    /**
     * Get all documents (metadata only)
     */
    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.findAll(pageable));
    }

    /**
     * Get metadata for a single document
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    /**
     * Update metadata (not file itself)
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDTO documentDTO) {

        return ResponseEntity.ok(documentService.update(id, documentDTO));
    }

    /**
     * Delete a document (both metadata + file on disk)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        DocumentDTO doc = documentService.findById(id);
        if (doc.getFichier() != null) {
            try {
                Files.deleteIfExists(fileStorageService.getRoot().resolve(doc.getFichier()));
            } catch (Exception ignored) {}
        }
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Download a document (force download)
     */
// In DocumentController.java
// Find this method:
// In your DocumentController.java

@GetMapping("/{id}/download")
public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
    try {
        DocumentDTO doc = documentService.findById(id);
        Resource resource = fileStorageService.load(doc.getFichier());

        // Get the file's MIME type based on its content or extension.
        String mimeType = Files.probeContentType(resource.getFile().toPath());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Fallback to generic binary stream
        }

        // Get the user-friendly name and append the extension.
        String originalFilename = resource.getFilename();
        String filename = doc.getNom();
        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }
        }
        if (!filename.toLowerCase().endsWith(extension.toLowerCase())) {
            filename += extension;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType)) // Sets the correct MIME type
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

    /**
     * Preview a document in browser (if supported by MIME type)
     */
    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> previewDocument(@PathVariable Long id) {
        try {
            DocumentDTO doc = documentService.findById(id);
            Resource resource = fileStorageService.load(doc.getFichier());

            String mimeType = Files.probeContentType(resource.getFile().toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
