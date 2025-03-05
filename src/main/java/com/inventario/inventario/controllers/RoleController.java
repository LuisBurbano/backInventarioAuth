package com.inventario.inventario.controllers;

import com.inventario.inventario.entities.Role;
import com.inventario.inventario.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Obtener todos los roles
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    // Crear un nuevo rol
    @PostMapping
    public Role addRole(@RequestBody Role role) {
        return roleService.save(role);
    }

    // Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar un rol por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar un rol por ID
    @PutMapping("/{id}")
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
