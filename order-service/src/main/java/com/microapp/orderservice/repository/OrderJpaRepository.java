package com.microapp.orderservice.repository;

import com.microapp.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order,Long> {
}
