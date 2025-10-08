package com.interiordesign.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the complete room layout with furniture positions
 * Contains all placed furniture, total cost, warnings, and AI reasoning
 */
public class RoomLayout {
    
    private List<FurniturePosition> furniture;  // Changed from 'positions' to 'furniture' for frontend compatibility
    private int totalCost;
    private List<String> warnings;
    private String reasoning;  // AI explanation of layout choices

    // Constructors
    public RoomLayout() {
        this.furniture = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.totalCost = 0;
    }

    public RoomLayout(List<FurniturePosition> furniture, int totalCost, List<String> warnings, String reasoning) {
        this.furniture = furniture != null ? furniture : new ArrayList<>();
        this.totalCost = totalCost;
        this.warnings = warnings != null ? warnings : new ArrayList<>();
        this.reasoning = reasoning;
    }

    // Getters and Setters
    public List<FurniturePosition> getFurniture() {
        return furniture;
    }

    public void setFurniture(List<FurniturePosition> furniture) {
        this.furniture = furniture;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
    
    /**
     * Add a furniture position to the layout
     */
    public void addFurniture(FurniturePosition position) {
        this.furniture.add(position);
        this.totalCost += position.getFurniture().getPrice();
    }
    
    /**
     * Add a warning message
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
    
    /**
     * Get count of furniture pieces
     */
    public int getFurnitureCount() {
        return furniture.size();
    }

    @Override
    public String toString() {
        return "RoomLayout{" +
                "furnitureCount=" + furniture.size() +
                ", totalCost=" + totalCost +
                ", warnings=" + warnings.size() +
                '}';
    }
}
