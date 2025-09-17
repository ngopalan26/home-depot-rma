package com.homedepot.rma.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AIConfig {

    @Value("${openai.api.key:demo-key}")
    private String openaiApiKey;

    @Bean
    public OpenAiService openAiService() {
        // For demo purposes, we'll create a mock service if no API key is provided
        if ("demo-key".equals(openaiApiKey)) {
            return null; // Will be handled by the service layer
        }
        return new OpenAiService(openaiApiKey, Duration.ofSeconds(30));
    }
}
