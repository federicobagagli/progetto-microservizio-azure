package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Permette l'accesso solo dal tuo dominio specifico
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")  // Sostituisci con l'URL del tuo front-end
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);  // Se hai bisogno di inviare credenziali (cookies, sessioni, etc.)
            }
        };
    }
}
