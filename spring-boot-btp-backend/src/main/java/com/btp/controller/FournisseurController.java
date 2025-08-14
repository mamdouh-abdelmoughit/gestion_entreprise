package com.btp.controller;

import com.btp.dto.FournisseurDTO;
import com.btp.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @GetMapping
    public ResponseEntity<Page<FournisseurDTO>> getAllFournisseurs(Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseurById(@PathVariable Long id) {
        return ResponseEntity.ok(fournisseurService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FournisseurDTO> createFournisseur(@Valid @RequestBody FournisseurDTO fournisseurDTO) {
        FournisseurDTO created = fournisseurService.save(fournisseurDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO> updateFournisseur(@PathVariable Long id, @Valid @RequestBody FournisseurDTO fournisseurDTO) {
        return ResponseEntity.ok(fournisseurService.update(id, fournisseurDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByType(
            @PathVariable String type, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByType(type, pageable));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByStatut(
            @PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByStatut(statut, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByCreatedById(userId, pageable));
    }

    @GetMapping("/search/by-ice/{ice}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByIce(@PathVariable String ice, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByIce(ice, pageable));
    }

    @GetMapping("/search/by-rc/{rc}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByRc(@PathVariable String rc, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByRc(rc, pageable));
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<Page<FournisseurDTO>> searchFournisseursByName(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.searchByName(keyword, pageable));
    }

    @GetMapping("/search/by-specialite/{specialite}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursBySpecialite(@PathVariable String specialite, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findBySpecialite(specialite, pageable));
    }

    @GetMapping("/search/by-statut/{statut}/by-creator/{userId}")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursByStatutAndCreator(@PathVariable String statut, @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(fournisseurService.findByStatutAndCreatedBy(statut, userId, pageable));
    }
}
