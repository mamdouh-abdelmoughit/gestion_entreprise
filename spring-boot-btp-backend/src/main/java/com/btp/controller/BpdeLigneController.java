package com.btp.controller;

import com.btp.dto.BpdeLigneDTO;
import com.btp.service.BpdeLigneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bpde-lignes")
public class BpdeLigneController {

    @Autowired private BpdeLigneService bpdeLigneService;

    @GetMapping("/projet/{projetId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC')")
    public List<BpdeLigneDTO> findByProjet(@PathVariable Long projetId) {
        return bpdeLigneService.findByProjet(projetId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC')")
    public BpdeLigneDTO findById(@PathVariable Long id) {
        return bpdeLigneService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public BpdeLigneDTO create(@RequestBody BpdeLigneDTO dto) {
        return bpdeLigneService.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public BpdeLigneDTO update(@PathVariable Long id, @RequestBody BpdeLigneDTO dto) {
        return bpdeLigneService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bpdeLigneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
