package com.inventario.inventario.controllers;

import com.inventario.inventario.entities.AuthRequest;
import com.inventario.inventario.entities.User;
import com.inventario.inventario.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Solo administradores pueden ver todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // Solo administradores pueden crear usuarios
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    // Solo administradores pueden ver usuarios por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Solo administradores pueden eliminar usuarios
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Solo administradores pueden actualizar usuarios
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setName(user.getName());
            userDb.setPassword(user.getPassword());
            userDb.setEmail(user.getEmail());
            userDb.setRole(user.getRole());
            return ResponseEntity.ok(userService.save(userDb));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestBody AuthRequest authRequest) {
        System.out.println("Datos recibidos: " + authRequest);
        try {
            User authenticatedUser = userService.login(authRequest.getEmail(), authRequest.getPassword());
            System.out.println("Usuario autenticado: " + authenticatedUser);
            return ResponseEntity.ok(Map.of("role", authenticatedUser.getRole()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}