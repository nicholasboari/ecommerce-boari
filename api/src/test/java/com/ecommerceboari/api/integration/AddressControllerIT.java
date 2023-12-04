package com.ecommerceboari.api.integration;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.util.AddressCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private MvcResult createNewAddress() throws Exception {
        String jsonAddressDTOBody = objectMapper.writeValueAsString(AddressCreator.createValidAddressDTO());
        return mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }

    @BeforeEach
    void setup() throws Exception {
        String jsonResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"nicholasboari@gmail.com\", \"password\": \"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        token = jsonNode.get("token").asText();
    }

    @Test
    @Transactional
    @DisplayName("Save a new address when user is logged")
    void save_CreatesNewAddress_WhenUserIsLogged() throws Exception {
        MvcResult result = createNewAddress();

        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        long createdId = responseJson.get("id").asLong();

        Assertions.assertEquals(2L, createdId);
    }

    @Test
    @Transactional
    @DisplayName("Throws a bad request exception when user try save duplicate address")
    void save_ThrowsExceptionWhenTryingToSaveDuplicateAddress() throws Exception {
        String jsonAddressDTOBody = objectMapper.writeValueAsString(AddressCreator.createValidAddressDTO());
        createNewAddress();
        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddressDTOBody)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("Update an address when user is logged")
    void update_UpdateAddress_WhenUserIsLogged() throws Exception {
        String responseBody = createNewAddress().getResponse().getContentAsString();
        AddressDTO updatedAddressDTO = objectMapper.readValue(responseBody, AddressDTO.class);
        updatedAddressDTO.setCountry("Other Country");
        Long id = updatedAddressDTO.getId();

        mockMvc.perform(put("/api/v1/addresses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddressDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("Other Country"));
    }
}