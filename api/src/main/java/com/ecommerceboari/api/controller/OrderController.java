package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.order.OrderRequestDTO;
import com.ecommerceboari.api.dto.order.OrderResponseDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.OrderService;
import com.ecommerceboari.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findOrderByUser() {
        UserResponseDTO user = userService.findUserAuthenticated();
        List<OrderResponseDTO> order = orderService.findOrdersByUser(user);
        LOGGER.info("Received request to fetch order by user with ID: {}", user.getId());
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> save(@RequestBody OrderRequestDTO orderDTO) {
        UserResponseDTO user = userService.findUserAuthenticated();
        OrderResponseDTO order = orderService.save(user, orderDTO);
        LOGGER.info("Received request to create a new order ID: {}, by user with ID: {}", order.getId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
