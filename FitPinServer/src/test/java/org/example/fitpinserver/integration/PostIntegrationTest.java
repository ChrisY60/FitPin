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
class PostIntegrationTest {

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

    private Long createPost(Cookie authCookie, String caption, String imageUrl, String... tagNames) throws Exception {
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < tagNames.length; i++) {
            if (i > 0) tags.append(",");
            tags.append("\"").append(tagNames[i]).append("\"");
        }

        MvcResult result = mockMvc.perform(post("/posts")
                        .cookie(authCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "caption": "%s",
                                    "imageUrl": "%s",
                                    "productIds": [],
                                    "tagNames": [%s]
                                }
                                """.formatted(caption, imageUrl, tags)))
                .andExpect(status().isCreated())
                .andReturn();

        return ((Number) com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();
    }

    @Test
    void getAllPosts_NoPosts_ReturnsEmptyList() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(get("/posts").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createPost_Authenticated_ReturnsCreatedPostWithTags() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");

        mockMvc.perform(post("/posts")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "caption": "My outfit today",
                                    "imageUrl": "http://example.com/image.jpg",
                                    "productIds": [],
                                    "tagNames": ["streetwear", "denim"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.publisherUsername").value("chris"))
                .andExpect(jsonPath("$.caption").value("My outfit today"))
                .andExpect(jsonPath("$.imageUrl").value("http://example.com/image.jpg"))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.commentCount").value(0))
                .andExpect(jsonPath("$.tags.length()").value(2));
    }

    @Test
    void createPost_Unauthenticated_Returns403() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "caption": "My outfit today",
                                    "imageUrl": "http://example.com/image.jpg",
                                    "productIds": [],
                                    "tagNames": []
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllPosts_AfterCreatingPost_ReturnsPost() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        createPost(cookie, "My outfit today", "http://example.com/image.jpg");

        mockMvc.perform(get("/posts").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].publisherUsername").value("chris"))
                .andExpect(jsonPath("$[0].caption").value("My outfit today"));
    }

    @Test
    void deletePost_Owner_Returns204AndRemovesPost() throws Exception {
        Cookie cookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(cookie, "My outfit today", "http://example.com/image.jpg");

        mockMvc.perform(delete("/posts/" + postId)
                        .cookie(cookie))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/posts").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deletePost_NonOwner_Returns400() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie otherCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(delete("/posts/" + postId)
                        .cookie(otherCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You can only delete your own posts"));
    }

    @Test
    void likePost_Authenticated_ReturnsLikedTrueWithCount() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie likerCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(post("/posts/" + postId + "/likes")
                        .cookie(likerCookie))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.likeCount").value(1));
    }

    @Test
    void likePost_AlreadyLiked_Returns400() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie likerCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(post("/posts/" + postId + "/likes")
                        .cookie(likerCookie))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/posts/" + postId + "/likes")
                        .cookie(likerCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Post already liked"));
    }

    @Test
    void unlikePost_AfterLiking_ReturnsLikedFalseWithDecrementedCount() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie likerCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(post("/posts/" + postId + "/likes")
                        .cookie(likerCookie))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/posts/" + postId + "/likes")
                        .cookie(likerCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liked").value(false))
                .andExpect(jsonPath("$.likeCount").value(0));
    }

    @Test
    void addComment_Authenticated_ReturnsCreatedComment() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie commenterCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(post("/posts/" + postId + "/comments")
                        .cookie(commenterCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "Love this outfit!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Love this outfit!"))
                .andExpect(jsonPath("$.authorUsername").value("alex"));
    }

    @Test
    void getComments_AfterAddingComment_ReturnsComment() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie commenterCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        mockMvc.perform(post("/posts/" + postId + "/comments")
                        .cookie(commenterCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "Love this outfit!"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/posts/" + postId + "/comments").cookie(commenterCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].content").value("Love this outfit!"))
                .andExpect(jsonPath("$[0].authorUsername").value("alex"));
    }

    @Test
    void deleteComment_NonAuthor_Returns400() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie commenterCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        MvcResult result = mockMvc.perform(post("/posts/" + postId + "/comments")
                        .cookie(commenterCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "Love this outfit!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        Long commentId = ((Number) com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();

        mockMvc.perform(delete("/posts/" + postId + "/comments/" + commentId)
                        .cookie(ownerCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You can only delete your own comments"));
    }

    @Test
    void deleteComment_Author_Returns204() throws Exception {
        Cookie ownerCookie = loginAndGetCookie("chris", "chris@example.com", "password123");
        Long postId = createPost(ownerCookie, "My outfit today", "http://example.com/image.jpg");

        Cookie commenterCookie = loginAndGetCookie("alex", "alex@example.com", "password123");

        MvcResult result = mockMvc.perform(post("/posts/" + postId + "/comments")
                        .cookie(commenterCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "Love this outfit!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        Long commentId = ((Number) com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();

        mockMvc.perform(delete("/posts/" + postId + "/comments/" + commentId)
                        .cookie(commenterCookie))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/posts/" + postId + "/comments").cookie(commenterCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
