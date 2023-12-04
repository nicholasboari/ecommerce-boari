package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.dto.ProductOrderDTO;
import com.ecommerceboari.api.dto.request.OrderRequestDTO;
import com.ecommerceboari.api.dto.response.OrderResponseDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.OrderRepository;
import com.ecommerceboari.api.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    private UserResponseDTO userResponseDTO;
    private List<OrderResponseDTO> expectedOrderslist;
    private ProductDTO productDTO;
    private OrderRequestDTO orderDTO;
    private Product product;
    private Order order;
    private List<ProductOrderDTO> productOrderDTOlist;

    @BeforeEach
    void setup() {
        userResponseDTO = UserCreator.createValidUserResponseDTO();
        expectedOrderslist = OrderCreator.createValidListOfOrderResponseDTO();
        productDTO = ProductCreator.createValidProductDTO();
        product = ProductCreator.createValidProduct();
        productOrderDTOlist = ProductOrderCreator.createValidListOfProductOrderDTO();
        orderDTO = OrderCreator.createValidOrderRequestDTO();
        order = OrderCreator.createValidOrder();
    }

    @Test
    @DisplayName("Return a list of orders by user when successful")
    void findOrdersByUser_ShouldReturnListOfOrdersByUser_WhenSuccessful() {
        UserResponseDTO user = UserCreator.createValidUserResponseDTO();

        List<OrderResponseDTO> expectedOrders = OrderCreator.createValidListOfOrderResponseDTO();

        Mockito.when(userService.findById(user.getId())).thenReturn(user);

        List<OrderResponseDTO> result = orderService.findOrdersByUser(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedOrders.size(), result.size());
    }

    @Test
    @DisplayName("Save a new order when user's address is not null")
    void save_ShouldSaveOrder_WhenAddressIsNotNull() {
        Mockito.when(productService.findById(1L)).thenReturn(productDTO);
        Mockito.when(modelMapper.map(Mockito.any(ProductDTO.class), Mockito.eq(Product.class))).thenReturn(product);

        Order orderSaved = OrderCreator.createValidOrder();
        OrderResponseDTO orderResponseDTO = OrderCreator.createValidOrderResponseDTO();
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(orderSaved);
        Mockito.when(modelMapper.map(Mockito.any(Order.class), Mockito.eq(OrderResponseDTO.class))).thenReturn(orderResponseDTO);
        OrderResponseDTO result = orderService.save(userResponseDTO, orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getMoment());
        Assertions.assertEquals(orderDTO.getProducts().size(), result.getProducts().size());
    }

}