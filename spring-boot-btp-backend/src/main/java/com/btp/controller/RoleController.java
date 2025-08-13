package com.btp.controller;

import com.btp.dto.RoleDTO;
import com.btp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleDTO>> getAllRoles(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.save(roleDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.update(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
