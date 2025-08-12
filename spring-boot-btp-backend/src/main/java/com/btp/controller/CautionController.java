package com.btp.controller;

import com.btp.entity.Caution;
import com.btp.service.CautionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cautions")
public class CautionController {

    @Autowired
    private CautionService cautionService;

    @GetMapping
    public List<Caution> getAllCautions() {
        return cautionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caution> getCautionById(@PathVariable Long id) {
        return cautionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Caution createCaution(@RequestBody Caution caution) {
        return cautionService.save(caution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caution> updateCaution(@PathVariable Long id, @RequestBody Caution caution) {
        return cautionService.update(id, caution)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaution(@PathVariable Long id) {
        if (cautionService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{type}")
    public List<Caution> getCautionsByType(@PathVariable Caution.TypeCaution type) {
        return cautionService.findByType(type);
    }

    @GetMapping("/statut/{statut}")
    public List<Caution> getCautionsByStatut(@PathVariable Caution.StatutCaution statut) {
        return cautionService.findByStatut(statut);
    }
}
