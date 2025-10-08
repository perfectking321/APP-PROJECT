package com.interiordesign.service;

import com.interiordesign.ai.AIService;
import com.interiordesign.ai.model.AILayoutResponse;
import com.interiordesign.dao.FurnitureDAO;
import com.interiordesign.model.Furniture;
import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for orchestrating room layout generation
 * Coordinates between DAO, AI service, and rule engine
 */
@Service
public class LayoutService {
    
    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);
    
    private final FurnitureDAO furnitureDAO;
    private final RuleEngine ruleEngine;
    private final AIService aiService;
    
    public LayoutService(FurnitureDAO furnitureDAO, RuleEngine ruleEngine, AIService aiService) {
        this.furnitureDAO = furnitureDAO;
        this.ruleEngine = ruleEngine;
        this.aiService = aiService;
    }
    
    /**
     * Create a validated room layout using AI suggestions
     * 
     * @param room Room specifications (dimensions and budget)
     * @return Complete room layout with furniture positions
     */
    public RoomLayout createLayout(Room room) {
        logger.info("Creating layout for room: {}", room);
        
        long startTime = System.currentTimeMillis();
        
        // Step 1: Get all available furniture from database
        List<Furniture> availableFurniture = furnitureDAO.findAll();
        logger.debug("Retrieved {} furniture items from database", availableFurniture.size());
        
        if (availableFurniture.isEmpty()) {
            logger.warn("No furniture available in database");
            RoomLayout emptyLayout = new RoomLayout();
            emptyLayout.addWarning("No furniture available in catalog");
            return emptyLayout;
        }
        
        // Step 2: Call AI to get suggested layout
        AILayoutResponse aiSuggestions = aiService.getSuggestedLayout(room, availableFurniture);
        logger.debug("Received {} furniture suggestions from AI", 
                aiSuggestions.getFurniture() != null ? aiSuggestions.getFurniture().size() : 0);
        
        // Step 3: Pass AI suggestions to RuleEngine for validation and adjustment
        RoomLayout layout = ruleEngine.generateLayout(room, availableFurniture, aiSuggestions);
        
        long endTime = System.currentTimeMillis();
        logger.info("Layout generation completed in {}ms - {} items placed, cost: ${}", 
                (endTime - startTime), layout.getFurnitureCount(), layout.getTotalCost());
        
        return layout;
    }
    
    /**
     * Get all furniture from database
     * Used by frontend to display catalog
     * 
     * @return List of all furniture items
     */
    public List<Furniture> getAllFurniture() {
        logger.debug("Fetching all furniture");
        return furnitureDAO.findAll();
    }
}
