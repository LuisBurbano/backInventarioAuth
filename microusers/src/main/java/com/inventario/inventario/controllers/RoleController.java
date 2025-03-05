package com.inventario.inventario.controllers;

import com.inventario.inventario.entities.Role;
import com.inventario.inventario.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Solo administradores pueden ver todos los roles
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    // Solo administradores pueden crear roles
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Role addRole(@RequestBody Role role) {
        return roleService.save(role);
    }

    // Solo administradores pueden ver roles por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Solo administradores pueden eliminar roles
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Solo administradores pueden actualizar roles
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@RequestBody Role role, @PathVariable Long id) {
        Optional<Role> roleOptional = roleService.findById(id);
        if (roleOptional.isPresent()) {
            Role roleDb = roleOptional.get();
            roleDb.setName(role.getName());
            return ResponseEntity.ok(roleService.save(roleDb));
        }
        return ResponseEntity.notFound().build();
    }
}