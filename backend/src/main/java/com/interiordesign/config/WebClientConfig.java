package com.interiordesign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Web Client Configuration for AI API calls
 * Configures WebClient for calling OpenRouter API
 */
@Configuration
public class WebClientConfig {

    /**
     * Create WebClient bean for making HTTP requests
     * Used by AIService to call OpenRouter API
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
    }
}
