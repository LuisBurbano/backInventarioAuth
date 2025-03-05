package com.inventario.inventario.services;

import com.inventario.inventario.entities.User;
import com.inventario.inventario.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService; // Para obtener roles si es necesario

    // Obtener todos los usuarios
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    // Buscar un usuario por ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Crear o actualizar un usuario
    public User save(User user) {
        // Aquí puedes agregar más lógica si necesitas validar los roles o hacer algo extra
        return userRepository.save(user);
    }

    // Eliminar un usuario por ID
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
