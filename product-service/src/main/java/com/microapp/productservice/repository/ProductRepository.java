package com.microapp.productservice.repository;

import com.microapp.productservice.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.CrudRepositoryExtensionsKt;

import java.math.BigDecimal;

public interface ProductRepository{
    boolean existsByPriceRange(BigDecimal min,BigDecimal max);
    boolean existsByNameExceptId(String name,Long id);
}
