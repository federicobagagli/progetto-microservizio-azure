// src/test/java/com/example/demo/controller/AuthControllerTest.java
package com.example.demo;

import com.example.demo.controller.AuthController;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void loginSuccess() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("admin");
        user.setRole("ROLE_ADMIN");
        user.setPasswordHash(encoder.encode("password")); // hash giusto

        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(java.util.Optional.of(user));

        Mockito.when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("faketoken123");

        String jsonBody = """
                {
                  "username": "admin",
                  "password": "password"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("faketoken123"));
    }

    @Test
    void loginFailsInvalidPassword() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("admin");
        user.setRole("ROLE_ADMIN");
        user.setPasswordHash(encoder.encode("anotherpassword")); // hash sbagliato

        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(java.util.Optional.of(user));

        String jsonBody = """
                {
                  "username": "admin",
                  "password": "password"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Invalid credentials")));
    }

    @Test
    void loginFailsUserNotFound() throws Exception {
        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(java.util.Optional.empty());

        String jsonBody = """
                {
                  "username": "admin",
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
