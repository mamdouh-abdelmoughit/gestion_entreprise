package com.btp.controller;

import com.btp.dto.DepenseDTO;
import com.btp.service.DepenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/depenses")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @GetMapping
    public ResponseEntity<Page<DepenseDTO>> getAllDepenses(Pageable pageable) {
        return ResponseEntity.ok(depenseService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepenseDTO> getDepenseById(@PathVariable Long id) {
        return ResponseEntity.ok(depenseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DepenseDTO> createDepense(@Valid @RequestBody DepenseDTO depenseDTO) {
        DepenseDTO created = depenseService.save(depenseDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepenseDTO> updateDepense(@PathVariable Long id, @Valid @RequestBody DepenseDTO depenseDTO) {
        return ResponseEntity.ok(depenseService.update(id, depenseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        depenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
