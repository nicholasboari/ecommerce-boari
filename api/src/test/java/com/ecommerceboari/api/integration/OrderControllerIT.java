package com.ecommerceboari.api.integration;

import com.ecommerceboari.api.dto.response.OrderResponseDTO;
import com.ecommerceboari.api.util.OrderCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        String userJsonResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"joaozinho@gmail.com\", \"password\": \"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode userJsonNode = objectMapper.readTree(userJsonResponse);
        userToken = userJsonNode.get("token").asText();
    }

    @Test
    @Order(2)
    @DisplayName("Return orders by user")
    void findOrderByUser_ReturnsOrdersByUser_WhenSuccessful() throws Exception {
        String contentAsString = mockMvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<OrderResponseDTO> orderList = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(orderList).hasSize(1);
        assertThat(orderList.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @Order(1)
    @DisplayName("Create a new order")
    void save_CreatesNewOrder_WhenSuccessful() throws Exception {
        String jsonAddressDTOBody = objectMapper.writeValueAsString(OrderCreator.createValidOrderRequestDTO());
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }
}