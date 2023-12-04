package com.ecommerceboari.api.integration;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.util.CategoryCreator;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerIT {


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
    @DisplayName("Return a page of categories")
    void findPaged_ReturnPageOfCategories_WhenSuccessful() throws Exception {
        mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(2)
    @DisplayName("Return all categories")
    void findAll_ReturnAllCategories_WhenSuccessful() throws Exception {
        String contentAsString = mockMvc.perform(get("/api/v1/categories/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertFalse(contentAsString.trim().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Return a category by ID")
    void findById_ReturnCategoryById_WhenSuccessful() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(4)
    @DisplayName("Return categories by name containing")
    void findByNameContaining_ReturnCategoriesByNameContaining_WhenSuccessful() throws Exception {
        String contentAsString = mockMvc.perform(get("/api/v1/categories/find")
                        .param("name", "Cate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode responseJson = objectMapper.readTree(contentAsString);
        Assertions.assertEquals(3, responseJson.size());
    }

    @Test
    @Order(5)
    @DisplayName("Create a new category when user is admin or manager")
    void save_CreatesNewCategory_WhenUserIsAdminOrManager() throws Exception {
        String jsonAddressDTOBody = objectMapper.writeValueAsString(CategoryCreator.createValidCategoryDTOWithoutID());
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Category D"));
    }

    @Test
    @Order(6)
    @DisplayName("Update a category when user is admin or manager")
    void update_UpdateACategory_WhenUserIsAdminOrManager() throws Exception {
        CategoryDTO CategoryDTO = CategoryCreator.createValidCategoryDTOWithoutID();
        CategoryDTO.setName("Name updated");
        String jsonAddressDTOBody = objectMapper.writeValueAsString(CategoryDTO);
        mockMvc.perform(put("/api/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name updated"));
    }

    @Test
    @Order(7)
    @DisplayName("Delete a category when user is admin or manager")
    void delete_DeleteACategory_WhenUserIsAdminOrManager() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("Throws a forbidden exception when user is not admin or manager")
    void save_ThrowsException_WhenUserIsNotAdminOrManager() throws Exception {
        CategoryDTO validCategoryDTO = CategoryCreator.createValidCategoryDTOWithoutID();
        validCategoryDTO.setName("New name");
        String jsonAddressDTOBody = objectMapper.writeValueAsString(validCategoryDTO);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}