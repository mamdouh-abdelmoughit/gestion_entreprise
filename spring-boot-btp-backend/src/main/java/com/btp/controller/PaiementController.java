package com.btp.controller;

import com.btp.dto.PaiementDTO;
import com.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paiements")
public class PaiementController {

    @Autowired private PaiementService paiementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public Page<PaiementDTO> findAll(Pageable pageable) {
        return paiementService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public PaiementDTO findById(@PathVariable Long id) {
        return paiementService.findById(id);
    }

    @GetMapping("/facture/{factureId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CLIENT')")
    public Page<PaiementDTO> findByFacture(@PathVariable Long factureId, Pageable pageable) {
        return paiementService.findByFacture(factureId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public PaiementDTO create(@RequestBody PaiementDTO dto) {
        return paiementService.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public PaiementDTO update(@PathVariable Long id, @RequestBody PaiementDTO dto) {
        return paiementService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paiementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
