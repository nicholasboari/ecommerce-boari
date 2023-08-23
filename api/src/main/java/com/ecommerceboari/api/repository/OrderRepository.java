package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
