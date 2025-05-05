package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll(); // per evitare duplicati
        User user = new User();
        user.setUsername("admin");
        user.setRole("ROLE_ADMIN");
        user.setPasswordHash(encoder.encode("password"));
        userRepository.save(user);
    }

    @Test
    void loginSuccess() throws Exception {
        String jsonBody = """
                {
                  "username": "admin",
                  "password": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginFailsInvalidPassword() throws Exception {
        String jsonBody = """
                {
                  "username": "admin",
                  "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Invalid credentials")));
    }

    @Test
    void loginFailsUserNotFound() throws Exception {
        String jsonBody = """
                {
                  "username": "nouser",
                  "password": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("User not found")));
    }
}
