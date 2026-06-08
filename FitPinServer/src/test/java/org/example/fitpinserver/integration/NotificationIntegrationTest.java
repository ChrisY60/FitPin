package org.example.fitpinserver.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import org.example.fitpinserver.DAL.repositories.CommentJPARepository;
import org.example.fitpinserver.DAL.repositories.NotificationJPARepository;
import org.example.fitpinserver.DAL.repositories.PostJPARepository;
import org.example.fitpinserver.DAL.repositories.PostLikeJPARepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class NotificationIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private NotificationJPARepository notificationJPARepository;

    @Autowired
    private CommentJPARepository commentJPARepository;

    @Autowired
    private PostLikeJPARepository postLikeJPARepository;

    @Autowired
    private PostJPARepository postJPARepository;

    @Autowired
    private UserJPARepository userJPARepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();
        notificationJPARepository.deleteAll();
        commentJPARepository.deleteAll();
        postLikeJPARepository.deleteAll();
        postJPARepository.deleteAll();
        userJPARepository.deleteAll();
    }

    @Test
    void likingAnotherUsersPost_CreatesUnreadLikeNotificationForPublisher() throws Exception {
        String aliceToken = registerAndLogin("alice", "alice@example.com", "password123");
        String bobToken = registerAndLogin("bob", "bob@example.com", "password123");
        long postId = createPost(aliceToken, "https://example.com/fit.png", "My new fit");

        mockMvc.perform(post("/posts/" + postId + "/likes").cookie(tokenCookie(bobToken)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/notifications").cookie(tokenCookie(aliceToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("LIKE"))
                .andExpect(jsonPath("$[0].actorUsername").value("bob"))
                .andExpect(jsonPath("$[0].postId").value(postId))
                .andExpect(jsonPath("$[0].read").value(false));
    }

    @Test
    void likingOwnPost_DoesNotCreateNotification() throws Exception {
        String aliceToken = registerAndLogin("alice", "alice@example.com", "password123");
        long postId = createPost(aliceToken, "https://example.com/fit.png", "My new fit");

        mockMvc.perform(post("/posts/" + postId + "/likes").cookie(tokenCookie(aliceToken)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/notifications").cookie(tokenCookie(aliceToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void commentingOnAnotherUsersPost_CreatesUnreadCommentNotificationForPublisher() throws Exception {
        String aliceToken = registerAndLogin("alice", "alice@example.com", "password123");
        String bobToken = registerAndLogin("bob", "bob@example.com", "password123");
        long postId = createPost(aliceToken, "https://example.com/fit.png", "My new fit");

        mockMvc.perform(post("/posts/" + postId + "/comments")
                        .cookie(tokenCookie(bobToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content": "Love this fit!"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/notifications").cookie(tokenCookie(aliceToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("COMMENT"))
                .andExpect(jsonPath("$[0].actorUsername").value("bob"))
                .andExpect(jsonPath("$[0].read").value(false));
    }

    @Test
    void getNotifications_WithoutAuthentication_Returns403() throws Exception {
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isForbidden());
    }

    @Test
    void markingNotificationAsRead_UpdatesReadStatusAndUnreadCount() throws Exception {
        String aliceToken = registerAndLogin("alice", "alice@example.com", "password123");
        String bobToken = registerAndLogin("bob", "bob@example.com", "password123");
        long postId = createPost(aliceToken, "https://example.com/fit.png", "My new fit");

        mockMvc.perform(post("/posts/" + postId + "/likes").cookie(tokenCookie(bobToken)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/notifications/unread-count").cookie(tokenCookie(aliceToken)))
                .andExpect(jsonPath("$.count").value(1));

        long notificationId = getFirstNotificationId(aliceToken);

        mockMvc.perform(put("/notifications/" + notificationId + "/read").cookie(tokenCookie(aliceToken)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/notifications/unread-count").cookie(tokenCookie(aliceToken)))
                .andExpect(jsonPath("$.count").value(0));

        mockMvc.perform(get("/notifications").cookie(tokenCookie(aliceToken)))
                .andExpect(jsonPath("$[0].read").value(true));
    }

    @Test
    void markingAnotherUsersNotificationAsRead_Returns400AndLeavesItUnread() throws Exception {
        String aliceToken = registerAndLogin("alice", "alice@example.com", "password123");
        String bobToken = registerAndLogin("bob", "bob@example.com", "password123");
        String charlieToken = registerAndLogin("charlie", "charlie@example.com", "password123");
        long postId = createPost(aliceToken, "https://example.com/fit.png", "My new fit");

        mockMvc.perform(post("/posts/" + postId + "/likes").cookie(tokenCookie(bobToken)))
                .andExpect(status().isCreated());

        long notificationId = getFirstNotificationId(aliceToken);

        mockMvc.perform(put("/notifications/" + notificationId + "/read").cookie(tokenCookie(charlieToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Notification not found"));

        mockMvc.perform(get("/notifications").cookie(tokenCookie(aliceToken)))
                .andExpect(jsonPath("$[0].read").value(false));
    }

    private String registerAndLogin(String username, String email, String password) throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username": "%s", "emailAddress": "%s", "password": "%s"}
                                """.formatted(username, email, password)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"emailOrUsername": "%s", "password": "%s"}
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();

        return readJson(result).get("token").asText();
    }

    private long createPost(String token, String imageUrl, String caption) throws Exception {
        MvcResult result = mockMvc.perform(post("/posts")
                        .cookie(tokenCookie(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"imageUrl": "%s", "caption": "%s"}
                                """.formatted(imageUrl, caption)))
                .andExpect(status().isCreated())
                .andReturn();

        return readJson(result).get("id").asLong();
    }

    private long getFirstNotificationId(String token) throws Exception {
        MvcResult result = mockMvc.perform(get("/notifications").cookie(tokenCookie(token)))
                .andExpect(status().isOk())
                .andReturn();

        return readJson(result).get(0).get("id").asLong();
    }

    private Cookie tokenCookie(String token) {
        return new Cookie("token", token);
    }

    private JsonNode readJson(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}
