package com.btp.controller;

import com.btp.dto.DecompteDTO;
import com.btp.service.DecompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/decomptes")
public class DecompteController {

    @Autowired
    private DecompteService decompteService;

    @GetMapping
    public ResponseEntity<Page<DecompteDTO>> getAllDecomptes(Pageable pageable) {
        return ResponseEntity.ok(decompteService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecompteDTO> getDecompteById(@PathVariable Long id) {
        return ResponseEntity.ok(decompteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DecompteDTO> createDecompte(@Valid @RequestBody DecompteDTO decompteDTO) {
        DecompteDTO created = decompteService.save(decompteDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DecompteDTO> updateDecompte(@PathVariable Long id, @Valid @RequestBody DecompteDTO decompteDTO) {
        return ResponseEntity.ok(decompteService.update(id, decompteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecompte(@PathVariable Long id) {
        decompteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByProjet(@PathVariable Long projetId, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByProjet(projetId, pageable));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByCreatedById(userId, pageable));
    }

    @GetMapping("/search/by-date-range")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/search/by-projet/{projetId}/by-statut/{statut}")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByProjetAndStatut(
            @PathVariable Long projetId,
            @PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByProjetAndStatut(projetId, statut, pageable));
    }

    @GetMapping("/search/amount-greater-than")
    public ResponseEntity<Page<DecompteDTO>> getDecomptesByAmountGreaterThan(@RequestParam Double amount, Pageable pageable) {
        return ResponseEntity.ok(decompteService.findByAmountGreaterThan(amount, pageable));
    }
}
