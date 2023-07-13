package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.OrderDTO;
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
    public ResponseEntity<List<OrderDTO>> findOrderByUser() {
        UserResponseDTO user = userService.findUserAuthenticated();
        List<OrderDTO> order = orderService.findPaged(user);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> save(@RequestBody OrderDTO orderDTO) {
        UserResponseDTO user = userService.findUserAuthenticated();
        OrderDTO order = orderService.save(user, orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
