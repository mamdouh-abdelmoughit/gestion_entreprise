package com.btp.controller;

import com.btp.entity.Decompte;
import com.btp.service.DecompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decomptes")
@CrossOrigin(origins = "*")
public class DecompteController {

    @Autowired
    private DecompteService decompteService;

    @GetMapping
    public ResponseEntity<List<Decompte>> getAllDecomptes() {
        return ResponseEntity.ok(decompteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Decompte> getDecompteById(@PathVariable Long id) {
        return decompteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Decompte> createDecompte(@RequestBody Decompte decompte) {
        Decompte savedDecompte = decompteService.save(decompte);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDecompte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Decompte> updateDecompte(@PathVariable Long id, @RequestBody Decompte decompte) {
        return decompteService.findById(id)
                .map(existing -> {
                    decompte.setId(id);
                    return ResponseEntity.ok(decompteService.save(decompte));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecompte(@PathVariable Long id) {
        if (decompteService.findById(id).isPresent()) {
            decompteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
