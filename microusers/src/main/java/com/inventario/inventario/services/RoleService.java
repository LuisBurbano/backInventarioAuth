package com.inventario.inventario.services;

import com.inventario.inventario.entities.Role;
import com.inventario.inventario.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Obtener todos los roles
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    // Buscar un rol por ID
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    // Crear o actualizar un rol
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    // Eliminar un rol por ID
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

}
