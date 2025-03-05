package com.inventario.inventario.services;

import com.inventario.inventario.entities.User;
import com.inventario.inventario.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService; // Para obtener roles si es necesario

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private static final int LOCK_TIME_DURATION = 10; // Duración en minutos del bloqueo

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Eliminar un usuario por ID
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Método de Login
    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        // Verificar si la cuenta está bloqueada
        if (user.getLock_time() != null) {
            LocalDateTime lockTime = user.getLock_time().toLocalDateTime();
            long minutesPassed = ChronoUnit.MINUTES.between(lockTime, LocalDateTime.now());

            if (minutesPassed < LOCK_TIME_DURATION) {
                long minutesLeft = LOCK_TIME_DURATION - minutesPassed;
                throw new RuntimeException("Account is locked. Try again in " + minutesLeft + " minutes.");
            } else {
                // Desbloquear la cuenta después de que haya pasado el tiempo
                //TODO POR IMPLEMENTAR
            }
        }

        // Verificación de la contraseña
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Si la contraseña es correcta, resetear los intentos fallidos y la hora de bloqueo
            user.setFailed_attempts(0);
            user.setLock_time(null);
            userRepository.save(user);
            return user; // Devolver el usuario
        } else {
            // Si la contraseña es incorrecta, incrementar los intentos fallidos
            int failedAttempts = user.getFailed_attempts() + 1;
            user.setFailed_attempts(failedAttempts);

            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                // Bloquear la cuenta después de demasiados intentos fallidos
                user.setStatus(false);
                user.setLock_time(Timestamp.valueOf(LocalDateTime.now()));
                userRepository.save(user);
                throw new RuntimeException("Account locked due to too many failed login attempts. Try again in " + LOCK_TIME_DURATION + " minutes.");
            } else {
                userRepository.save(user);
                int attemptsLeft = MAX_FAILED_ATTEMPTS - failedAttempts;
                throw new RuntimeException("Invalid credentials. You have " + attemptsLeft + " attempt(s) left.");
            }
        }
    }

    // Actualizar un usuario
    public User updateUser(Long id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User existingUser = userOptional.get();
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());


        // Encriptar la contraseña si se proporciona
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

}
