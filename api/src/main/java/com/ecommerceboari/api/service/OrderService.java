package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.OrderDTO;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.dto.ProductOrderDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public List<OrderDTO> findPaged(UserResponseDTO user) {
        List<Order> orders = userService.findById(user.getId()).getOrder();
        return orders.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList();
    }

    @Transactional
    public OrderDTO save(UserResponseDTO user, OrderDTO orderDTO) {
        List<Product> productList = new ArrayList<>();

        for (ProductOrderDTO productOrderDTO : orderDTO.getProducts()) {
            ProductDTO product = productService.findById(productOrderDTO.getId());
            Product mapped = modelMapper.map(product, Product.class);
            productList.add(mapped);
        }

        Order orderSaved = createOrderAndAssociateWithUser(user, productList);
        return modelMapper.map(orderSaved, OrderDTO.class);
    }

    private Order createOrderAndAssociateWithUser(UserResponseDTO user, List<Product> productList) {
        Order order = Order.builder()
                .products(new ArrayList<>(productList))
                .build();
        Order orderSaved = orderRepository.save(order);

        user.getOrder().add(orderSaved);
        userService.save(user);
        return orderSaved;
    }

}
