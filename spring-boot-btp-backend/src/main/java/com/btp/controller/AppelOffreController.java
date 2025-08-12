package com.btp.controller;

import com.btp.entity.AppelOffre;
import com.btp.service.AppelOffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appel-offres")
public class AppelOffreController {

    @Autowired
    private AppelOffreService appelOffreService;

    @GetMapping
    public List<AppelOffre> getAllAppelOffres() {
        return appelOffreService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppelOffre> getAppelOffreById(@PathVariable Long id) {
        return appelOffreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AppelOffre createAppelOffre(@RequestBody AppelOffre appelOffre) {
        return appelOffreService.save(appelOffre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppelOffre> updateAppelOffre(@PathVariable Long id, @RequestBody AppelOffre appelOffre) {
        return appelOffreService.update(id, appelOffre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppelOffre(@PathVariable Long id) {
        if (appelOffreService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
