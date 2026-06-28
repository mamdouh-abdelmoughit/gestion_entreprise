package com.btp.controller;

import com.btp.dto.ProjetDTO;
import com.btp.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<Page<ProjetDTO>> getAllProjets(Pageable pageable) {
        return ResponseEntity.ok(projetService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjetDTO> createProjet(@Valid @RequestBody ProjetDTO projetDTO) {
        ProjetDTO created = projetService.save(projetDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjetDTO> updateProjet(@PathVariable Long id, @Valid @RequestBody ProjetDTO projetDTO) {
        return ResponseEntity.ok(projetService.update(id, projetDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chef/{chefProjetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByChef(@PathVariable Long chefProjetId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByChefProjet(chefProjetId, pageable));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-statut/{statut}/by-chef/{chefProjetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByStatutAndChef(
            @PathVariable String statut,
            @PathVariable Long chefProjetId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByStatutAndChefProjet(statut, chefProjetId, pageable));
    }

    @GetMapping("/search/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<Page<ProjetDTO>> getActiveProjets(Pageable pageable) {
        return ResponseEntity.ok(projetService.findActiveProjects(pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByCreatedById(userId, pageable));
    }
}
