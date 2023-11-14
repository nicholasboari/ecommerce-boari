package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.util.OrderCreator;
import com.ecommerceboari.api.util.ProductCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        orderRepository.saveAllAndFlush(List.of(OrderCreator.createValidOrder()));
        productRepository.saveAllAndFlush(List.of(ProductCreator.createValidProduct()));
    }

    @DisplayName("Save creates order when Successful")
    @Test
    void save_PersistOrder_WhenSuccessful() {
        Order orderToBeSaved = OrderCreator.createValidOrder();

        Order orderSaved = orderRepository.save(orderToBeSaved);

        Assertions.assertThat(orderSaved).isNotNull();
        Assertions.assertThat(orderSaved.getId()).isNotNull();
    }

    @DisplayName("Save updates order when Successful")
    @Test
    void update_ReplaceOrder_WhenSuccessful() {
        Order orderToBeSaved = OrderCreator.createValidOrder();

        Order orderSaved = this.orderRepository.save(orderToBeSaved);

        Order orderUpdated = this.orderRepository.save(orderSaved);

        Assertions.assertThat(orderUpdated).isNotNull();

        Assertions.assertThat(orderUpdated.getId()).isNotNull();
    }

    @DisplayName("Delete removes order when Successful")
    @Test
    void delete_RemovesOrder_WhenSuccessful() {
        Order orderToBeSaved = OrderCreator.createValidOrder();

        Order orderSaved = this.orderRepository.save(orderToBeSaved);

        this.orderRepository.delete(orderSaved);

        Optional<Order> orderOptional = this.orderRepository.findById(orderSaved.getId());

        Assertions.assertThat(orderOptional).isNotPresent();
    }
}