package com.btp.controller;

import com.btp.dto.EvenementDTO;
import com.btp.service.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @GetMapping
    public ResponseEntity<Page<EvenementDTO>> getAllEvenements(Pageable pageable) {
        return ResponseEntity.ok(evenementService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementDTO> getEvenementById(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EvenementDTO> createEvenement(@Valid @RequestBody EvenementDTO evenementDTO) {
        EvenementDTO created = evenementService.save(evenementDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementDTO> updateEvenement(@PathVariable Long id, @Valid @RequestBody EvenementDTO evenementDTO) {
        return ResponseEntity.ok(evenementService.update(id, evenementDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
