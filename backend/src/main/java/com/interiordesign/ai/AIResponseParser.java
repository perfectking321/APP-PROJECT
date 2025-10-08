package com.interiordesign.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interiordesign.ai.exception.AIServiceException;
import com.interiordesign.ai.model.AILayoutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Parses AI responses and extracts JSON layout data
 * Handles various response formats including markdown code blocks
 */
@Component
public class AIResponseParser {
    
    private static final Logger logger = LoggerFactory.getLogger(AIResponseParser.class);
    private final ObjectMapper objectMapper;
    
    public AIResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * Parse AI response string into AILayoutResponse object
     * Handles JSON wrapped in markdown code blocks or plain JSON
     * 
     * @param aiResponse Raw response from AI
     * @return Parsed AILayoutResponse
     * @throws AIServiceException if parsing fails
     */
    public AILayoutResponse parseResponse(String aiResponse) {
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            throw new AIServiceException("AI response is empty");
        }
        
        logger.debug("Parsing AI response: {}", aiResponse.substring(0, Math.min(200, aiResponse.length())));
        
        try {
            // Extract JSON from response
            String jsonContent = extractJSON(aiResponse);
            
            // Parse JSON into object
            AILayoutResponse response = objectMapper.readValue(jsonContent, AILayoutResponse.class);
            
            // Validate response
            validateResponse(response);
            
            logger.info("Successfully parsed AI response with {} furniture items", 
                    response.getFurniture() != null ? response.getFurniture().size() : 0);
            
            return response;
            
        } catch (Exception e) {
            logger.error("Failed to parse AI response", e);
            throw new AIServiceException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract JSON content from AI response
     * Handles markdown code blocks (```json ... ```) and plain JSON
     */
    private String extractJSON(String response) {
        String trimmed = response.trim();
        
        // Check for markdown code block with json tag
        if (trimmed.contains("```json")) {
            int startIndex = trimmed.indexOf("```json") + 7;
            int endIndex = trimmed.indexOf("```", startIndex);
            if (endIndex > startIndex) {
                return trimmed.substring(startIndex, endIndex).trim();
            }
        }
        
        // Check for generic markdown code block
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            int startIndex = trimmed.indexOf("```") + 3;
            // Skip language identifier if present
            int newlineIndex = trimmed.indexOf("\n", startIndex);
            if (newlineIndex > startIndex) {
                startIndex = newlineIndex + 1;
            }
            int endIndex = trimmed.lastIndexOf("```");
            if (endIndex > startIndex) {
                return trimmed.substring(startIndex, endIndex).trim();
            }
        }
        
        // Try to find JSON object boundaries
        int jsonStart = trimmed.indexOf("{");
        int jsonEnd = trimmed.lastIndexOf("}");
        
        if (jsonStart >= 0 && jsonEnd > jsonStart) {
            return trimmed.substring(jsonStart, jsonEnd + 1);
        }
        
        // Return as-is if no special formatting detected
        return trimmed;
    }
    
    /**
     * Validate parsed AI response
     * Checks for required fields and reasonable values
     */
    private void validateResponse(AILayoutResponse response) {
        if (response == null) {
            throw new AIServiceException("Parsed response is null");
        }
        
        if (response.getFurniture() == null) {
            throw new AIServiceException("Furniture list is null in AI response");
        }
        
        if (response.getTotalCost() < 0) {
            throw new AIServiceException("Total cost cannot be negative");
        }
        
        // Validate each furniture placement
        for (int i = 0; i < response.getFurniture().size(); i++) {
            AILayoutResponse.AIFurniturePlacement placement = response.getFurniture().get(i);
            
            if (placement.getName() == null || placement.getName().trim().isEmpty()) {
                throw new AIServiceException("Furniture name is missing at index " + i);
            }
            
            if (placement.getX() < 0 || placement.getY() < 0) {
                throw new AIServiceException(
                    String.format("Invalid coordinates for %s: (%.2f, %.2f)", 
                        placement.getName(), placement.getX(), placement.getY())
                );
            }
            
            // Check for unreasonably large coordinates (likely an error)
            if (placement.getX() > 100 || placement.getY() > 100) {
                logger.warn("Unusually large coordinates for {}: ({}, {})", 
                    placement.getName(), placement.getX(), placement.getY());
            }
        }
        
        logger.debug("AI response validation passed");
    }
}
