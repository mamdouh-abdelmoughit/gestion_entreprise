package com.btp.controller;

import com.btp.dto.CautionDTO;
import com.btp.service.CautionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cautions")
public class CautionController {

    @Autowired
    private CautionService cautionService;

    @GetMapping
    public ResponseEntity<Page<CautionDTO>> getAllCautions(Pageable pageable) {
        return ResponseEntity.ok(cautionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CautionDTO> getCautionById(@PathVariable Long id) {
        return ResponseEntity.ok(cautionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CautionDTO> createCaution(@Valid @RequestBody CautionDTO cautionDTO) {
        CautionDTO created = cautionService.save(cautionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CautionDTO> updateCaution(@PathVariable Long id, @Valid @RequestBody CautionDTO cautionDTO) {
        return ResponseEntity.ok(cautionService.update(id, cautionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaution(@PathVariable Long id) {
        cautionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<CautionDTO>> getCautionsByType(@PathVariable String type, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByType(type, pageable));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<CautionDTO>> getCautionsByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByStatut(statut, pageable));
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<Page<CautionDTO>> getCautionsByProjet(@PathVariable Long projetId, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByProjet(projetId, pageable));
    }

    @GetMapping("/appel-offre/{appelOffreId}")
    public ResponseEntity<Page<CautionDTO>> getCautionsByAppelOffre(@PathVariable Long appelOffreId, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByAppelOffre(appelOffreId, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<CautionDTO>> getCautionsByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByCreatedById(userId, pageable));
    }

    @GetMapping("/search/expired")
    public ResponseEntity<Page<CautionDTO>> getExpiredCautions(Pageable pageable) {
        return ResponseEntity.ok(cautionService.findExpiredCautions(pageable));
    }

    @GetMapping("/search/expiring-soon")
    public ResponseEntity<Page<CautionDTO>> getExpiringSoonCautions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findExpiringSoon(date, pageable));
    }

    @GetMapping("/search/by-banque")
    public ResponseEntity<Page<CautionDTO>> getCautionsByBanque(@RequestParam String banque, Pageable pageable) {
        return ResponseEntity.ok(cautionService.findByBanque(banque, pageable));
    }
}
