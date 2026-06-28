package com.btp.controller;

import com.btp.dto.AppelOffreDTO;
import com.btp.service.AppelOffreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/appel-offres")
public class AppelOffreController {

    @Autowired
    private AppelOffreService appelOffreService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FOURNISSEUR')")
    public ResponseEntity<Page<AppelOffreDTO>> getAllAppelOffres(Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOURNISSEUR')")
    public ResponseEntity<AppelOffreDTO> getAppelOffreById(@PathVariable Long id) {
        return ResponseEntity.ok(appelOffreService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppelOffreDTO> createAppelOffre(@Valid @RequestBody AppelOffreDTO appelOffreDTO) {
        System.out.println("--- INSIDE CONTROLLER ---");
        System.out.println("Received DTO: " + appelOffreDTO.toString());
        AppelOffreDTO created = appelOffreService.save(appelOffreDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppelOffreDTO> updateAppelOffre(@PathVariable Long id, @Valid @RequestBody AppelOffreDTO appelOffreDTO) {
        return ResponseEntity.ok(appelOffreService.update(id, appelOffreDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppelOffre(@PathVariable Long id) {
        appelOffreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOURNISSEUR')")
    public ResponseEntity<Page<AppelOffreDTO>> getAppelOffresByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(appelOffreService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
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
