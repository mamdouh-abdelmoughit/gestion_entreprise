package com.btp.controller;

import com.btp.dto.FactureDTO;
import com.btp.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/factures")
public class FactureController {

    @Autowired private FactureService factureService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public Page<FactureDTO> findAll(Pageable pageable) {
        return factureService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public FactureDTO findById(@PathVariable Long id) {
        return factureService.findById(id);
    }

    @GetMapping("/projet/{projetId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public Page<FactureDTO> findByProjet(@PathVariable Long projetId, Pageable pageable) {
        return factureService.findByProjet(projetId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public FactureDTO create(@RequestBody FactureDTO dto) {
        return factureService.save(dto);
    }

    @PostMapping("/generate-from-attachement/{attachementId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public FactureDTO generateFromAttachement(
            @PathVariable Long attachementId,
            @RequestBody Map<String, Object> params) {
        String numero = (String) params.get("numero");
        Double tva = params.get("tva") != null ? Double.parseDouble(params.get("tva").toString()) : 20.0;
        Double retenuGarantie = params.get("retenuGarantie") != null ? Double.parseDouble(params.get("retenuGarantie").toString()) : 0.0;
        Double avance = params.get("avance") != null ? Double.parseDouble(params.get("avance").toString()) : 0.0;
        return factureService.generateFromAttachement(attachementId, numero, tva, retenuGarantie, avance);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public FactureDTO update(@PathVariable Long id, @RequestBody FactureDTO dto) {
        return factureService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
