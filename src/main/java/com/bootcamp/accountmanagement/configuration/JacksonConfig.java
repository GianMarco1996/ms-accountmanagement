package com.bootcamp.accountmanagement.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Configura globalmente Jackson para excluir campos null
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}