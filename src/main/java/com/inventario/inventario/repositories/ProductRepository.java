package com.inventario.inventario.repositories;

import com.inventario.inventario.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
