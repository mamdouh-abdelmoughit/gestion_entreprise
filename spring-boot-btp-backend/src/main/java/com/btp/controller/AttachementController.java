package com.btp.controller;

import com.btp.dto.AttachementDTO;
import com.btp.service.AttachementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attachements")
public class AttachementController {

    @Autowired private AttachementService attachementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public Page<AttachementDTO> findAll(Pageable pageable) {
        return attachementService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public AttachementDTO findById(@PathVariable Long id) {
        return attachementService.findById(id);
    }

    @GetMapping("/projet/{projetId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public Page<AttachementDTO> findByProjet(@PathVariable Long projetId, Pageable pageable) {
        return attachementService.findByProjet(projetId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC')")
    public AttachementDTO create(@RequestBody AttachementDTO dto) {
        return attachementService.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC')")
    public AttachementDTO update(@PathVariable Long id, @RequestBody AttachementDTO dto) {
        return attachementService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CP')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attachementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
