package com.btp.controller;

import com.btp.dto.ChequeEffetDTO;
import com.btp.service.ChequeEffetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cheques-effets")
public class ChequeEffetController {

    @Autowired private ChequeEffetService chequeEffetService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public Page<ChequeEffetDTO> findAll(Pageable pageable) {
        return chequeEffetService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public ChequeEffetDTO findById(@PathVariable Long id) {
        return chequeEffetService.findById(id);
    }

    @GetMapping("/projet/{projetId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC')")
    public Page<ChequeEffetDTO> findByProjet(@PathVariable Long projetId, Pageable pageable) {
        return chequeEffetService.findByProjet(projetId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public ChequeEffetDTO create(@RequestBody ChequeEffetDTO dto) {
        return chequeEffetService.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public ChequeEffetDTO update(@PathVariable Long id, @RequestBody ChequeEffetDTO dto) {
        return chequeEffetService.update(id, dto);
    }

    // Quick status transition endpoint (e.g. encaisser, rejeter)
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public ChequeEffetDTO updateStatut(@PathVariable Long id, @RequestParam String statut) {
        return chequeEffetService.updateStatut(id, statut);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chequeEffetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
