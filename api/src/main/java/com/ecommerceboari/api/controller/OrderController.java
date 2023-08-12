package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.order.OrderRequestDTO;
import com.ecommerceboari.api.dto.order.OrderResponseDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.OrderService;
import com.ecommerceboari.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findOrderByUser() {
        UserResponseDTO user = userService.findUserAuthenticated();
        List<OrderResponseDTO> order = orderService.findPaged(user);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> save(@RequestBody OrderRequestDTO orderDTO) {
        UserResponseDTO user = userService.findUserAuthenticated();
        OrderResponseDTO order = orderService.save(user, orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
