package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.ClientResponseDTO;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.dto.ProductOrderDTO;
import com.ecommerceboari.api.dto.order.OrderRequestDTO;
import com.ecommerceboari.api.dto.order.OrderResponseDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.exception.AddressNullException;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public List<OrderResponseDTO> findPaged(UserResponseDTO user) {
        List<OrderResponseDTO> orders = userService.findById(user.getId()).getOrder();
        ClientResponseDTO userMapped = modelMapper.map(user, ClientResponseDTO.class);

        return orders.stream().map(order -> {
            OrderResponseDTO mapped = modelMapper.map(order, OrderResponseDTO.class);
            mapped.setClient(userMapped);
            return mapped;
        }).toList();
    }

    @Transactional
    public OrderResponseDTO save(UserResponseDTO user, OrderRequestDTO orderDTO) {
        if (user.getAddress() == null) throw new AddressNullException("Address is null");

        List<Product> productList = new ArrayList<>();

        double total = 0.0;
        for (ProductOrderDTO productOrderDTO : orderDTO.getProducts()) {
            ProductDTO product = productService.findById(productOrderDTO.getId());
            total += product.getPrice();
            Product mapped = modelMapper.map(product, Product.class);
            productList.add(mapped);
        }

        Order orderSaved = createOrderAndAssociateWithUser(user, productList);
        orderSaved.setMoment(LocalDateTime.now());
        orderSaved.setTotal(total);

        ClientResponseDTO userMapped = modelMapper.map(user, ClientResponseDTO.class);
        OrderResponseDTO orderMapped = modelMapper.map(orderSaved, OrderResponseDTO.class);
        orderMapped.setClient(userMapped);
        return orderMapped;
    }

    private Order createOrderAndAssociateWithUser(UserResponseDTO user, List<Product> productList) {
        Order order = Order.builder()
                .products(new ArrayList<>(productList))
                .build();
        Order orderSaved = orderRepository.save(order);
        OrderResponseDTO orderMapped = modelMapper.map(orderSaved, OrderResponseDTO.class);

        user.getOrder().add(orderMapped);
        userService.updateUserOrder(user);
        return orderSaved;
    }
}
