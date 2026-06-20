package org.example.fitpinserver.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SearchIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Test
    void search_WithoutType_ReturnsAllCategories() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        mockMvc.perform(get("/search").param("q", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.brands").isArray())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void search_WithType_OnlyPopulatesSelectedCategory() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        mockMvc.perform(get("/search").param("q", "").param("type", "users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.products").isEmpty())
                .andExpect(jsonPath("$.brands").isEmpty())
                .andExpect(jsonPath("$.tags").isEmpty());
    }
}
