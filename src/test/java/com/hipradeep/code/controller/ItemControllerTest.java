package com.hipradeep.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnItem_whenItemExists() throws Exception {
        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void shouldReturnProblemDetail_whenItemNotFound() throws Exception {
        mockMvc.perform(get("/api/items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("https://api.example.com/errors/not-found"))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Item with ID 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnProblemDetail_whenRequestIsInvalid() throws Exception {
        String invalidJson = "{\"name\":\"\", \"price\":-5.0}";

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://api.example.com/errors/bad-request"))
                .andExpect(jsonPath("$.title").value("Invalid Request Parameter"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Item name cannot be empty"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnProblemDetail_whenGenericExceptionOccurs() throws Exception {
        mockMvc.perform(get("/api/items/trigger-500"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("https://api.example.com/errors/internal-server-error"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred: Simulated internal error for testing HTTP 500 handling"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
