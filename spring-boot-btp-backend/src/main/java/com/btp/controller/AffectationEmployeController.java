package com.btp.controller;

import com.btp.entity.AffectationEmploye;
import com.btp.service.AffectationEmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affectations")
@CrossOrigin(origins = "*")
public class AffectationEmployeController {

    @Autowired
    private AffectationEmployeService affectationEmployeService;

    @GetMapping
    public ResponseEntity<List<AffectationEmploye>> getAllAffectations() {
        return ResponseEntity.ok(affectationEmployeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AffectationEmploye> getAffectationById(@PathVariable Long id) {
        return affectationEmployeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AffectationEmploye> createAffectation(@RequestBody AffectationEmploye affectation) {
        AffectationEmploye savedAffectation = affectationEmployeService.save(affectation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAffectation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AffectationEmploye> updateAffectation(@PathVariable Long id, @RequestBody AffectationEmploye affectation) {
        return affectationEmployeService.findById(id)
                .map(existing -> {
                    affectation.setId(id);
                    return ResponseEntity.ok(affectationEmployeService.save(affectation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAffectation(@PathVariable Long id) {
        if (affectationEmployeService.findById(id).isPresent()) {
            affectationEmployeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
