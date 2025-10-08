package com.interiordesign.controller;

import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import com.interiordesign.service.LayoutService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for interior design layout API
 * Provides endpoints for furniture catalog and layout generation
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class LayoutRestController {
    
    private static final Logger logger = LoggerFactory.getLogger(LayoutRestController.class);
    
    private final LayoutService layoutService;
    
    @Value("${openrouter.api.key}")
    private String apiKey;
    
    public LayoutRestController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }
    
    /**
     * Root welcome endpoint
     * GET /api
     * 
     * @return API information
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> welcome() {
        logger.info("Welcome endpoint accessed");
        
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Interior Design API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "health", "/api/health",
            "furniture", "/api/furniture (GET)",
            "layout", "/api/layout (POST)",
            "h2Console", "/h2-console"
        ));
        response.put("message", "Welcome to Interior Design API! This is a REST API. Please use the frontend at http://localhost:3000");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Health check endpoint
     * GET /api/health
     * 
     * @return Simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check requested");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Interior Design API");
        response.put("timestamp", System.currentTimeMillis());
        
        // Check if API key is configured
        boolean apiConfigured = apiKey != null && 
                               !apiKey.isEmpty() && 
                               !apiKey.equals("your-api-key-here");
        response.put("aiServiceConfigured", apiConfigured);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all furniture from database
     * GET /api/furniture
     * 
     * @return List of all furniture items with details
     */
    @GetMapping("/furniture")
    public ResponseEntity<List<Furniture>> getAllFurniture() {
        logger.info("GET /api/furniture - Fetching all furniture");
        
        List<Furniture> furniture = layoutService.getAllFurniture();
        
        logger.info("Returning {} furniture items", furniture.size());
        return ResponseEntity.ok(furniture);
    }
    
    /**
     * Generate room layout with AI
     * POST /api/layout
     * 
     * Request body: { "length": 5.0, "width": 4.0, "budget": 2000 }
     * 
     * @param room Room specifications (dimensions and budget)
     * @return Complete room layout with furniture positions and cost
     */
    @PostMapping("/layout")
    public ResponseEntity<RoomLayout> generateLayout(@Valid @RequestBody Room room) {
        logger.info("POST /api/layout - Generating layout for room: {} x {} with budget ${}",
                room.getLength(), room.getWidth(), room.getBudget());
        
        // Log request details
        logger.debug("Room area: {} m², Budget density: ${}/m²",
                room.getArea(), room.getBudget() / room.getArea());
        
        // Generate layout using AI and rules
        RoomLayout layout = layoutService.createLayout(room);
        
        // Log response details
        logger.info("Layout generated: {} furniture items, total cost ${}, {} warnings",
                layout.getFurnitureCount(), layout.getTotalCost(), layout.getWarnings().size());
        
        if (!layout.getWarnings().isEmpty()) {
            logger.warn("Layout warnings: {}", layout.getWarnings());
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(layout);
    }
    
    /**
     * Test endpoint to verify API is responding
     * GET /api/test
     * 
     * @return Simple test message
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        logger.info("GET /api/test - Test endpoint called");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Interior Design API is working!");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}
