package com.mv.tisscherlock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite o acesso à API para origens específicas
        registry.addMapping("/api/**")  // Aplica CORS para todos os endpoints que começam com /api/
                .allowedOrigins("http://localhost:35405")  // Permite requisições de localhost:35405 (front-end)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Permite os métodos GET, POST, PUT, DELETE
                .allowedHeaders("*")  // Permite qualquer cabeçalho
                .allowCredentials(true);  // Permite que credenciais sejam enviadas (como cookies ou headers de autenticação)
    }
}
