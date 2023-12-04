package com.ecommerceboari.api.integration;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.util.BrandCreator;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        String adminJsonResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"nicholasboari@gmail.com\", \"password\": \"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode adminJsonNode = objectMapper.readTree(adminJsonResponse);
        adminToken = adminJsonNode.get("token").asText();

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
    @Order(1)
    @DisplayName("Return a page of brands")
    void findAll_ReturnPageOfBrands_WhenSuccessful() throws Exception {
        mockMvc.perform(get("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(2)
    @DisplayName("Return a brand by ID")
    void findById_ReturnPageOfBrands_WhenSuccessful() throws Exception {
        mockMvc.perform(get("/api/v1/brands/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(3)
    @DisplayName("Save a new brand when user is admin or manager")
    void save_CreatesNewBrand_WhenUserIsAdminOrManager() throws Exception {
        String jsonAddressDTOBody = objectMapper.writeValueAsString(BrandCreator.createValidBrandDTOWithoutID());
        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brand D"));
    }

    @Test
    @Order(4)
    @DisplayName("Update a brand when user is admin or manager")
    void update_UpdateABrand_WhenUserIsAdminOrManager() throws Exception {
        BrandDTO validBrandDTO = BrandCreator.createValidBrandDTOWithoutID();
        validBrandDTO.setName("Name updated");
        String jsonAddressDTOBody = objectMapper.writeValueAsString(validBrandDTO);
        mockMvc.perform(put("/api/v1/brands/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name updated"));
    }

    @Test
    @Order(5)
    @DisplayName("Delete a brand when user is admin or manager")
    void delete_DeleteABrand_WhenUserIsAdminOrManager() throws Exception {
        mockMvc.perform(delete("/api/v1/brands/{id}", 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(6)
    @DisplayName("Throws a forbidden exception when user is not admin or manager")
    void save_ThrowsException_WhenUserIsNotAdminOrManager() throws Exception {
        BrandDTO validBrandDTO = BrandCreator.createValidBrandDTOWithoutID();
        validBrandDTO.setName("New name");
        String jsonAddressDTOBody = objectMapper.writeValueAsString(validBrandDTO);

        mockMvc.perform(post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}