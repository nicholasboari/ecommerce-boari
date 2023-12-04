package com.ecommerceboari.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.dto.ProductOrderDTO;
import com.ecommerceboari.api.dto.request.OrderRequestDTO;
import com.ecommerceboari.api.dto.response.OrderResponseDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.exception.AddressNullException;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public List<OrderResponseDTO> findOrdersByUser(UserResponseDTO user) {
        return userService.findById(user.getId()).getOrder();
    }

    public OrderResponseDTO save(UserResponseDTO user, OrderRequestDTO orderDTO) {
        if (user.getAddress() == null)
            throw new AddressNullException("Address is null");

        List<Product> productList = new ArrayList<>();

        for (ProductOrderDTO productOrderDTO : orderDTO.getProducts()) {
            ProductDTO product = productService.findById(productOrderDTO.getId());
            Product mapped = modelMapper.map(product, Product.class);
            productList.add(mapped);
        }

        Order orderSaved = createOrderAndAssociateWithUser(user, productList);

        logger.info("Inserting {} to the database", orderSaved);
        return modelMapper.map(orderSaved, OrderResponseDTO.class);
    }

    private Order createOrderAndAssociateWithUser(UserResponseDTO user, List<Product> productList) {
        Order order = Order.builder()
                .products(new ArrayList<>(productList))
                .build();

        order.setMoment(LocalDateTime.now());
        double total = productList.stream().mapToDouble(Product::getPrice).sum();
        order.setTotal(total);

        Order orderSaved = orderRepository.save(order);
        OrderResponseDTO orderMapped = modelMapper.map(orderSaved, OrderResponseDTO.class);

        user.getOrder().add(orderMapped);
        userService.updateUserOrder(user);
        return orderSaved;
    }
}