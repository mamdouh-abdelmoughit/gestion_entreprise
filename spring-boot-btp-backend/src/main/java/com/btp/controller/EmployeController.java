package com.btp.controller;

import com.btp.dto.EmployeDTO;
import com.btp.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping
    public ResponseEntity<Page<EmployeDTO>> getAllEmployes(Pageable pageable) {
        return ResponseEntity.ok(employeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getEmployeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeDTO> createEmploye(@Valid @RequestBody EmployeDTO employeDTO) {
        EmployeDTO created = employeService.save(employeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> updateEmploye(@PathVariable Long id, @Valid @RequestBody EmployeDTO employeDTO) {
        return ResponseEntity.ok(employeService.update(id, employeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<Page<EmployeDTO>> getEmployesByStatut(@PathVariable String statut, Pageable pageable) {
        return ResponseEntity.ok(employeService.findByStatut(statut, pageable));
    }

    @GetMapping("/poste/{poste}")
    public ResponseEntity<Page<EmployeDTO>> getEmployesByPoste(@PathVariable String poste, Pageable pageable) {
        return ResponseEntity.ok(employeService.findByPoste(poste, pageable));
    }

    @GetMapping("/search/by-creator/{userId}")
    public ResponseEntity<Page<EmployeDTO>> getEmployesByCreator(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(employeService.findByCreatedById(userId, pageable));
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<Page<EmployeDTO>> searchEmployesByName(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(employeService.searchByName(keyword, pageable));
    }

    @GetMapping("/search/by-statut/{statut}/by-creator/{userId}")
    public ResponseEntity<Page<EmployeDTO>> getEmployesByStatutAndCreator(
            @PathVariable String statut,
            @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(employeService.findByStatutAndCreatedBy(statut, userId, pageable));
    }
}
