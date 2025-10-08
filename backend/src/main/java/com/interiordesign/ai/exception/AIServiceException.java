package com.interiordesign.ai.exception;

/**
 * Custom exception for AI service errors
 * Thrown when AI API calls fail or responses are invalid
 */
public class AIServiceException extends RuntimeException {
    
    public AIServiceException(String message) {
        super(message);
    }
    
    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
