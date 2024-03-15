package com.microapp.productservice.repository.impl;

import com.microapp.productservice.model.Product;
import com.microapp.productservice.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager entityManager;


    @Override
    public boolean existsByPriceRange(BigDecimal min, BigDecimal max) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Product> root = cq.from(Product.class);

        Predicate minPricePred = cb.greaterThanOrEqualTo(root.get("salePrice"),min);
        Predicate maxPricePred = cb.lessThanOrEqualTo(root.get("salePrice"),max);

        Predicate orPred = cb.and(minPricePred,maxPricePred);

        cq.select(cb.count(root)).where(orPred);
        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByNameExceptId(String name, Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Product> root = cq.from(Product.class);


        Predicate idPred = cb.notEqual(root.get("id"), id);
        Predicate namePred = cb.equal(root.get("name"), name);

        Predicate orPred = cb.and(idPred, namePred);

        cq.select(cb.count(root)).where(orPred);
        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }
}
