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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("type") String type,
            @RequestParam("description") String description) throws Exception {

        String fileRef = fileStorageService.save(file);

        DocumentDTO dto = new DocumentDTO();
        dto.setNom(nom);
        dto.setType(type);
        dto.setDescription(description);
        dto.setFichier(fileRef);
        dto.setTaille(file.getSize());
        dto.setDateUpload(LocalDate.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.save(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT', 'FOURNISSEUR')")
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT', 'FOURNISSEUR')")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentDTO> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDTO documentDTO) {
        return ResponseEntity.ok(documentService.update(id, documentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        DocumentDTO doc = documentService.findById(id);
        if (doc.getFichier() != null) {
            fileStorageService.delete(doc.getFichier());
        }
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT', 'FOURNISSEUR')")
    public ResponseEntity<?> downloadDocument(@PathVariable Long id) {
        return serveFile(documentService.findById(id), "attachment");
    }

    @GetMapping("/{id}/preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT', 'FOURNISSEUR')")
    public ResponseEntity<?> previewDocument(@PathVariable Long id) {
        return serveFile(documentService.findById(id), "inline");
    }

    private ResponseEntity<?> serveFile(DocumentDTO doc, String disposition) {
        String fileRef = doc.getFichier();
        if (fileRef == null) return ResponseEntity.notFound().build();

        Optional<Resource> maybeResource = fileStorageService.loadAsResource(fileRef);

        if (maybeResource.isPresent()) {
            try {
                Resource r = maybeResource.get();
                String mimeType = Files.probeContentType(r.getFile().toPath());
                if (mimeType == null) mimeType = "application/octet-stream";

                String ext = "";
                String orig = r.getFilename();
                if (orig != null) {
                    int dot = orig.lastIndexOf('.');
                    if (dot > 0) ext = orig.substring(dot);
                }
                String name = doc.getNom();
                if (!name.toLowerCase().endsWith(ext.toLowerCase())) name += ext;

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + name + "\"")
                    .body(r);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        // External storage (Supabase) — redirect to public URL.
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(fileStorageService.getPublicUrl(fileRef)))
            .build();
    }
}
