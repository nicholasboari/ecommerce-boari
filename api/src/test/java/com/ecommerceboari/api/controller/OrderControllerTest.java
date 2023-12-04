package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.request.OrderRequestDTO;
import com.ecommerceboari.api.dto.response.OrderResponseDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.service.OrderService;
import com.ecommerceboari.api.service.UserService;
import com.ecommerceboari.api.util.OrderCreator;
import com.ecommerceboari.api.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Returns a list of orders when findOrderByUser is called")
    void findOrderByUser_ReturnsListOfOrders_WhenSuccessful() {
        UserResponseDTO user = UserCreator.createValidUserResponseDTO();
        List<OrderResponseDTO> expectedOrders = Arrays.asList(
                OrderCreator.createValidOrderResponseDTO(),
                OrderCreator.createValidOrderResponseDTO(),
                OrderCreator.createValidOrderResponseDTO()
        );

        Mockito.when(userService.findUserAuthenticated()).thenReturn(user);
        Mockito.when(orderService.findOrdersByUser(user)).thenReturn(expectedOrders);

        ResponseEntity<List<OrderResponseDTO>> responseEntity = orderController.findOrderByUser();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedOrders.size());
    }

    @Test
    @DisplayName("Creates a new order when save is called with a valid DTO")
    void save_CreatesNewOrder_WhenSuccessful() {
        UserResponseDTO user = UserCreator.createValidUserResponseDTO();
        OrderRequestDTO validOrderDTO = OrderCreator.createValidOrderRequestDTO();
        OrderResponseDTO expectedOrder = OrderCreator.createValidOrderResponseDTO();

        Mockito.when(userService.findUserAuthenticated()).thenReturn(user);
        Mockito.when(orderService.save(user, validOrderDTO)).thenReturn(expectedOrder);

        ResponseEntity<OrderResponseDTO> responseEntity = orderController.save(validOrderDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedOrder.getId());
    }
}