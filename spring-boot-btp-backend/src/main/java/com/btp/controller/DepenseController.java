package com.btp.controller;

import com.btp.entity.Depense;
import com.btp.service.DepenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depenses")
@CrossOrigin(origins = "*")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @GetMapping
    public ResponseEntity<List<Depense>> getAllDepenses() {
        return ResponseEntity.ok(depenseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Long id) {
        return depenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Depense> createDepense(@RequestBody Depense depense) {
        Depense savedDepense = depenseService.save(depense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable Long id, @RequestBody Depense depense) {
        return depenseService.findById(id)
                .map(existing -> {
                    depense.setId(id);
                    return ResponseEntity.ok(depenseService.save(depense));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        if (depenseService.findById(id).isPresent()) {
            depenseService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
