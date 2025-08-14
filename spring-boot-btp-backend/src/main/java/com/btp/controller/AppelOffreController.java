package com.btp.controller;

import com.btp.dto.AppelOffreDTO;
import com.btp.service.AppelOffreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/appel-offres")
public class AppelOffreController {

    @Autowired
    private AppelOffreService appelOffreService;

    @GetMapping
    public ResponseEntity<Page<AppelOffreDTO>> getAllAppelOffres(Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppelOffreDTO> getAppelOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(appelOffreService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AppelOffreDTO> createAppelOffre(@Valid @RequestBody AppelOffreDTO appelOffreDTO) {
        AppelOffreDTO created = appelOffreService.save(appelOffreDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppelOffreDTO> updateAppelOffre(@PathVariable Long id, @Valid @RequestBody AppelOffreDTO appelOffreDTO) {
        return ResponseEntity.ok(appelOffreService.update(id, appelOffreDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppelOffre(@PathVariable Long id) {
        appelOffreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<AppelOffreDTO>> getAppelOffresByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<AppelOffreDTO>> getAppelOffresByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findByCreatedById(userId, pageable));
    }

    @GetMapping("/search/active")
    public ResponseEntity<Page<AppelOffreDTO>> getActiveAppelOffres(Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findActiveAppelsOffres(pageable));
    }

    @GetMapping("/search/by-statut/{statut}/by-creator/{userId}")
    public ResponseEntity<Page<AppelOffreDTO>> getAppelOffresByStatutAndCreator(@PathVariable String statut, @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findByStatutAndCreatedBy(statut, userId, pageable));
    }
}
