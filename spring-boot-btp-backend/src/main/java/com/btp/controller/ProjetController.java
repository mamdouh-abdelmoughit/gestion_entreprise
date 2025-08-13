package com.btp.controller;

import com.btp.dto.ProjetDTO;
import com.btp.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @GetMapping
    public ResponseEntity<Page<ProjetDTO>> getAllProjets(Pageable pageable) {
        return ResponseEntity.ok(projetService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProjetDTO> createProjet(@Valid @RequestBody ProjetDTO projetDTO) {
        ProjetDTO created = projetService.save(projetDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> updateProjet(@PathVariable Long id, @Valid @RequestBody ProjetDTO projetDTO) {
        return ResponseEntity.ok(projetService.update(id, projetDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chef/{chefProjetId}")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByChef(@PathVariable Long chefProjetId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByChefProjet(chefProjetId, pageable));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-statut/{statut}/by-chef/{chefProjetId}")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByStatutAndChef(
            @PathVariable String statut,
            @PathVariable Long chefProjetId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByStatutAndChefProjet(statut, chefProjetId, pageable));
    }

    @GetMapping("/search/active")
    public ResponseEntity<Page<ProjetDTO>> getActiveProjets(Pageable pageable) {
        return ResponseEntity.ok(projetService.findActiveProjects(pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<ProjetDTO>> getProjetsByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(projetService.findByCreatedById(userId, pageable));
    }
}
