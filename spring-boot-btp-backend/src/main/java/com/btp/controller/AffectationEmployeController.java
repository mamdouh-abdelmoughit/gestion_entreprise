package com.btp.controller;

import com.btp.dto.AffectationEmployeDTO;
import com.btp.service.AffectationEmployeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/affectations")
public class AffectationEmployeController {

    @Autowired
    private AffectationEmployeService affectationEmployeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<AffectationEmployeDTO>> getAllAffectations(Pageable pageable) {
        return ResponseEntity.ok(affectationEmployeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AffectationEmployeDTO> getAffectationById(@PathVariable Long id) {
        return ResponseEntity.ok(affectationEmployeService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AffectationEmployeDTO> createAffectation(@Valid @RequestBody AffectationEmployeDTO affectationEmployeDTO) {
        AffectationEmployeDTO created = affectationEmployeService.create(affectationEmployeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AffectationEmployeDTO> updateAffectation(@PathVariable Long id, @Valid @RequestBody AffectationEmployeDTO affectationEmployeDTO) {
        return ResponseEntity.ok(affectationEmployeService.update(id, affectationEmployeDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAffectation(@PathVariable Long id) {
        affectationEmployeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
