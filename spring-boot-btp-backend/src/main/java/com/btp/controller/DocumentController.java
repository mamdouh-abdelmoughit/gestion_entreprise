package com.btp.controller;

import com.btp.dto.DocumentDTO;
import com.btp.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(@Valid @RequestBody DocumentDTO documentDTO) {
        DocumentDTO created = documentService.save(documentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDTO documentDTO) {
        return ResponseEntity.ok(documentService.update(id, documentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
