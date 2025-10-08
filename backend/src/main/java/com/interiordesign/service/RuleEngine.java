package com.interiordesign.service;

import com.interiordesign.ai.model.AILayoutResponse;
import com.interiordesign.model.Furniture;
import com.interiordesign.model.FurniturePosition;
import com.interiordesign.model.Room;
import com.interiordesign.model.RoomLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Rule engine for validating and adjusting furniture placements
 * Applies business rules to ensure valid room layouts
 */
@Component
public class RuleEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
    
    private static final double WALL_CLEARANCE = 0.5;  // Minimum clearance from walls in meters
    private static final double MIN_FURNITURE_GAP = 0.3;  // Minimum gap between furniture pieces
    
    /**
     * Generate validated room layout based on AI suggestions
     * Applies validation rules and adjusts positions as needed
     * 
     * @param room Room specifications
     * @param availableFurniture Available furniture catalog
     * @param aiSuggestions AI-generated layout suggestions
     * @return Validated and adjusted room layout
     */
    public RoomLayout generateLayout(Room room, List<Furniture> availableFurniture, 
                                    AILayoutResponse aiSuggestions) {
        logger.info("Generating layout for room {} x {} with budget ${}", 
                room.getLength(), room.getWidth(), room.getBudget());
        
        RoomLayout layout = new RoomLayout();
        layout.setReasoning(aiSuggestions.getReasoning());
        
        List<FurniturePosition> placedFurniture = new ArrayList<>();
        int totalCost = 0;
        
        // Process each AI-suggested furniture placement
        for (AILayoutResponse.AIFurniturePlacement aiPlacement : aiSuggestions.getFurniture()) {
            // Find furniture in catalog by name
            Optional<Furniture> furnitureOpt = findFurnitureByName(availableFurniture, aiPlacement.getName());
            
            if (furnitureOpt.isEmpty()) {
                logger.warn("Furniture '{}' not found in catalog, skipping", aiPlacement.getName());
                layout.addWarning("Furniture '" + aiPlacement.getName() + "' not found in catalog");
                continue;
            }
            
            Furniture furniture = furnitureOpt.get();
            
            // Check budget constraint
            if (totalCost + furniture.getPrice() > room.getBudget()) {
                logger.info("Budget exceeded, cannot add {} (${}) - would exceed budget by ${}",
                        furniture.getName(), furniture.getPrice(), 
                        (totalCost + furniture.getPrice() - room.getBudget()));
                layout.addWarning("Budget limit reached, could not place " + furniture.getName());
                continue;
            }
            
            // Create furniture position
            FurniturePosition position = new FurniturePosition(furniture, aiPlacement.getX(), aiPlacement.getY());
            
            // Validate and adjust position
            if (validateAndAdjustPosition(position, room, placedFurniture)) {
                placedFurniture.add(position);
                totalCost += furniture.getPrice();
                logger.debug("Placed {} at ({}, {})", furniture.getName(), position.getX(), position.getY());
            } else {
                logger.warn("Could not place {} at ({}, {}) - validation failed",
                        furniture.getName(), aiPlacement.getX(), aiPlacement.getY());
                layout.addWarning("Could not place " + furniture.getName() + " at suggested position");
            }
        }
        
        // Set final layout data
        layout.setFurniture(placedFurniture);
        layout.setTotalCost(totalCost);
        
        // Add summary warning if no furniture placed
        if (placedFurniture.isEmpty()) {
            layout.addWarning("No furniture could be placed with the given constraints");
        }
        
        logger.info("Layout generation complete: {} items placed, total cost ${}", 
                placedFurniture.size(), totalCost);
        
        return layout;
    }
    
    /**
     * Validate and adjust furniture position to meet all rules
     * 
     * @param position Furniture position to validate
     * @param room Room specifications
     * @param existingFurniture Already placed furniture
     * @return true if position is valid or successfully adjusted
     */
    private boolean validateAndAdjustPosition(FurniturePosition position, Room room, 
                                             List<FurniturePosition> existingFurniture) {
        Furniture furniture = position.getFurniture();
        
        // Rule 1: Check if furniture fits in room with wall clearance
        if (!fitsInRoomWithClearance(position, room)) {
            logger.debug("{} doesn't fit in room at ({}, {})", 
                    furniture.getName(), position.getX(), position.getY());
            
            // Try to adjust position
            if (!tryAdjustToFitRoom(position, room)) {
                return false;
            }
        }
        
        // Rule 2: Check for collisions with existing furniture
        for (FurniturePosition existing : existingFurniture) {
            if (hasCollision(position, existing)) {
                logger.debug("{} collides with {}", furniture.getName(), existing.getFurniture().getName());
                
                // Try to adjust position to avoid collision
                if (!tryAdjustToAvoidCollision(position, existing, room)) {
                    return false;
                }
            }
        }
        
        // Final validation after adjustments
        if (!fitsInRoomWithClearance(position, room)) {
            return false;
        }
        
        for (FurniturePosition existing : existingFurniture) {
            if (hasCollision(position, existing)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check if furniture fits in room with required wall clearance
     */
    private boolean fitsInRoomWithClearance(FurniturePosition position, Room room) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getFurniture().getWidth();
        double depth = position.getFurniture().getDepth();
        
        // Check boundaries with clearance
        return x >= WALL_CLEARANCE &&
               y >= WALL_CLEARANCE &&
               (x + width) <= (room.getLength() - WALL_CLEARANCE) &&
               (y + depth) <= (room.getWidth() - WALL_CLEARANCE);
    }
    
    /**
     * Check if two furniture positions collide (with minimum gap)
     */
    private boolean hasCollision(FurniturePosition pos1, FurniturePosition pos2) {
        double x1 = pos1.getX() - MIN_FURNITURE_GAP;
        double y1 = pos1.getY() - MIN_FURNITURE_GAP;
        double x1End = pos1.getX() + pos1.getFurniture().getWidth() + MIN_FURNITURE_GAP;
        double y1End = pos1.getY() + pos1.getFurniture().getDepth() + MIN_FURNITURE_GAP;
        
        double x2 = pos2.getX();
        double y2 = pos2.getY();
        double x2End = pos2.getX() + pos2.getFurniture().getWidth();
        double y2End = pos2.getY() + pos2.getFurniture().getDepth();
        
        // Check for overlap
        return !(x1End <= x2 || x1 >= x2End || y1End <= y2 || y1 >= y2End);
    }
    
    /**
     * Try to adjust furniture position to fit in room
     */
    private boolean tryAdjustToFitRoom(FurniturePosition position, Room room) {
        double x = position.getX();
        double y = position.getY();
        double width = position.getFurniture().getWidth();
        double depth = position.getFurniture().getDepth();
        
        // Adjust x coordinate if needed
        if (x < WALL_CLEARANCE) {
            x = WALL_CLEARANCE;
        } else if (x + width > room.getLength() - WALL_CLEARANCE) {
            x = room.getLength() - WALL_CLEARANCE - width;
        }
        
        // Adjust y coordinate if needed
        if (y < WALL_CLEARANCE) {
            y = WALL_CLEARANCE;
        } else if (y + depth > room.getWidth() - WALL_CLEARANCE) {
            y = room.getWidth() - WALL_CLEARANCE - depth;
        }
        
        // Check if adjusted position is valid
        if (x < 0 || y < 0 || x + width > room.getLength() || y + depth > room.getWidth()) {
            return false;  // Furniture too large for room
        }
        
        position.setX(x);
        position.setY(y);
        return true;
    }
    
    /**
     * Try to adjust furniture position to avoid collision
     */
    private boolean tryAdjustToAvoidCollision(FurniturePosition position, FurniturePosition existing, Room room) {
        // Try small adjustments in different directions
        double[][] adjustments = {
            {0.5, 0},    // Move right
            {-0.5, 0},   // Move left
            {0, 0.5},    // Move up
            {0, -0.5},   // Move down
            {0.5, 0.5},  // Move diagonal
            {-0.5, 0.5},
            {0.5, -0.5},
            {-0.5, -0.5}
        };
        
        double originalX = position.getX();
        double originalY = position.getY();
        
        for (double[] adj : adjustments) {
            position.setX(originalX + adj[0]);
            position.setY(originalY + adj[1]);
            
            if (fitsInRoomWithClearance(position, room) && !hasCollision(position, existing)) {
                logger.debug("Adjusted position by ({}, {}) to avoid collision", adj[0], adj[1]);
                return true;
            }
        }
        
        // Restore original position if no adjustment worked
        position.setX(originalX);
        position.setY(originalY);
        return false;
    }
    
    /**
     * Find furniture in catalog by name (case-insensitive)
     */
    private Optional<Furniture> findFurnitureByName(List<Furniture> furniture, String name) {
        return furniture.stream()
                .filter(f -> f.getName().equalsIgnoreCase(name.trim()))
                .findFirst();
    }
}
