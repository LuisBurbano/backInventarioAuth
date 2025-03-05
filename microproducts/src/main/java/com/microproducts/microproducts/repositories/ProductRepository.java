package com.microproducts.microproducts.repositories;


import com.microproducts.microproducts.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
