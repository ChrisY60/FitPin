package org.example.fitpinserver.integration;

import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import org.example.fitpinserver.DAL.repositories.NotificationJPARepository;
import org.example.fitpinserver.DAL.repositories.UserJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private NotificationJPARepository notificationJPARepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();
        notificationJPARepository.deleteAll();
        userJPARepository.deleteAll();
    }

    private Cookie loginAndGetCookie(String username, String email, String password) throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "emailAddress": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, email, password)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "emailOrUsername": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getCookie("token");
    }

    @Test
    void getUserProfile_ExistingUser_ReturnsProfile() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(get("/users/chris").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("chris"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.postCount").value(0));
    }

    @Test
    void getUserProfile_NonExistentUser_Returns400() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(get("/users/nobody").cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void getUserPosts_NoPosts_ReturnsEmptyList() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(get("/users/chris/posts").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getUserPosts_AfterCreatingPost_ReturnsPost() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(post("/posts")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "caption": "My outfit today",
                                    "imageUrl": "http://example.com/image.jpg",
                                    "productIds": [],
                                    "tagNames": []
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/chris/posts").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].publisherUsername").value("chris"));
    }

    @Test
    void updateOwnProfile_Authenticated_ReturnsUpdatedProfile() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(put("/users/me")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "bio": "Fashion enthusiast",
                                    "profilePictureUrl": "http://example.com/me.jpg"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("chris"))
                .andExpect(jsonPath("$.bio").value("Fashion enthusiast"))
                .andExpect(jsonPath("$.profilePictureUrl").value("http://example.com/me.jpg"));
    }

    @Test
    void updateOwnProfile_Unauthenticated_Returns403() throws Exception {
        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "bio": "Fashion enthusiast",
                                    "profilePictureUrl": "http://example.com/me.jpg"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
