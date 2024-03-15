package com.microapp.productservice.repository;

import com.microapp.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Predicate;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);
    List<Product> findAllByIdIn(List<Long> ids);
}
