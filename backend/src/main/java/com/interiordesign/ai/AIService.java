package com.interiordesign.ai;

import com.interiordesign.ai.exception.AIServiceException;
import com.interiordesign.ai.model.AILayoutRequest;
import com.interiordesign.ai.model.AILayoutResponse;
import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for AI-powered layout generation using OpenRouter API
 * Calls Llama 3.1 70B model to generate furniture placement suggestions
 */
@Service
public class AIService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    
    private final WebClient webClient;
    private final AIPromptBuilder promptBuilder;
    private final AIResponseParser responseParser;
    
    @Value("${openrouter.api.url}")
    private String apiUrl;
    
    @Value("${openrouter.api.key}")
    private String apiKey;
    
    @Value("${openrouter.model}")
    private String model;
    
    @Value("${ai.timeout.seconds}")
    private int timeoutSeconds;
    
    @Value("${ai.max-retries}")
    private int maxRetries;
    
    public AIService(WebClient webClient, AIPromptBuilder promptBuilder, AIResponseParser responseParser) {
        this.webClient = webClient;
        this.promptBuilder = promptBuilder;
        this.responseParser = responseParser;
    }
    
    /**
     * Get AI-suggested furniture layout for the given room
     * 
     * @param room Room specifications (dimensions and budget)
     * @param availableFurniture List of furniture items available for placement
     * @return AILayoutResponse with suggested furniture positions
     * @throws AIServiceException if AI call fails
     */
    public AILayoutResponse getSuggestedLayout(Room room, List<Furniture> availableFurniture) {
        logger.info("Requesting AI layout for room: {} x {} with budget ${}", 
                room.getLength(), room.getWidth(), room.getBudget());
        
        try {
            // Build request
            AILayoutRequest request = new AILayoutRequest(
                room.getLength(),
                room.getWidth(),
                room.getBudget(),
                availableFurniture
            );
            
            // Build prompt
            String prompt = promptBuilder.buildLayoutPrompt(request);
            logger.debug("Generated prompt with {} characters", prompt.length());
            
            // Call OpenRouter API
            String aiResponse = callOpenRouterAPI(prompt);
            
            // Parse response
            AILayoutResponse layoutResponse = responseParser.parseResponse(aiResponse);
            
            logger.info("Successfully generated AI layout with {} furniture items, total cost: ${}", 
                    layoutResponse.getFurniture().size(), layoutResponse.getTotalCost());
            
            return layoutResponse;
            
        } catch (Exception e) {
            logger.error("AI layout generation failed", e);
            throw new AIServiceException("Failed to generate AI layout: " + e.getMessage(), e);
        }
    }
    
    /**
     * Call OpenRouter API with retry logic
     * 
     * @param prompt User prompt for AI
     * @return AI response as string
     */
    private String callOpenRouterAPI(String prompt) {
        logger.debug("Calling OpenRouter API: {}", apiUrl);
        
        // Validate API key
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            throw new AIServiceException(
                "OpenRouter API key not configured. Set OPENROUTER_API_KEY environment variable."
            );
        }
        
        // Build request body
        Map<String, Object> requestBody = buildRequestBody(prompt);
        
        try {
            // Make API call with timeout and retry
            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .retryWhen(Retry.fixedDelay(maxRetries, Duration.ofSeconds(2))
                            .filter(throwable -> isRetryableError(throwable))
                            .doBeforeRetry(retrySignal -> 
                                logger.warn("Retrying AI API call, attempt: {}", retrySignal.totalRetries() + 1)
                            )
                    )
                    .block();
            
            // Extract content from response
            return extractContentFromResponse(response);
            
        } catch (WebClientResponseException e) {
            logger.error("OpenRouter API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AIServiceException("OpenRouter API error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to call OpenRouter API", e);
            throw new AIServiceException("Failed to call AI service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Build OpenRouter API request body
     */
    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        
        // Build messages array
        List<Map<String, String>> messages = List.of(
            Map.of(
                "role", "system",
                "content", "You are an expert interior designer specializing in optimal furniture placement. " +
                          "Always respond with valid JSON only, no additional text."
            ),
            Map.of(
                "role", "user",
                "content", prompt
            )
        );
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        
        return requestBody;
    }
    
    /**
     * Extract content from OpenRouter API response
     * Response format: { "choices": [{ "message": { "content": "..." } }] }
     */
    private String extractContentFromResponse(String response) {
        try {
            // Simple JSON extraction (you could use ObjectMapper for more robust parsing)
            int contentStart = response.indexOf("\"content\"");
            if (contentStart == -1) {
                throw new AIServiceException("No content field in API response");
            }
            
            // Find the start of the content value
            int valueStart = response.indexOf(":", contentStart) + 1;
            int quoteStart = response.indexOf("\"", valueStart) + 1;
            
            // Find the end of the content value (accounting for escaped quotes)
            int quoteEnd = quoteStart;
            while (quoteEnd < response.length()) {
                quoteEnd = response.indexOf("\"", quoteEnd);
                if (quoteEnd == -1 || response.charAt(quoteEnd - 1) != '\\') {
                    break;
                }
                quoteEnd++;
            }
            
            if (quoteEnd == -1) {
                throw new AIServiceException("Malformed content field in API response");
            }
            
            String content = response.substring(quoteStart, quoteEnd);
            
            // Unescape JSON string
            content = content.replace("\\n", "\n")
                           .replace("\\\"", "\"")
                           .replace("\\\\", "\\");
            
            logger.debug("Extracted content: {}", content.substring(0, Math.min(200, content.length())));
            return content;
            
        } catch (Exception e) {
            logger.error("Failed to extract content from response", e);
            throw new AIServiceException("Failed to parse API response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Determine if error is retryable (network issues, timeouts, 5xx errors)
     */
    private boolean isRetryableError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException e = (WebClientResponseException) throwable;
            int statusCode = e.getStatusCode().value();
            // Retry on 5xx server errors and 429 rate limiting
            return statusCode >= 500 || statusCode == 429;
        }
        // Retry on timeout and network errors
        return throwable instanceof java.util.concurrent.TimeoutException ||
               throwable instanceof java.io.IOException;
    }
}
