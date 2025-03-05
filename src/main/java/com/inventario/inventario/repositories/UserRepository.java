package com.inventario.inventario.repositories;

import com.inventario.inventario.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
