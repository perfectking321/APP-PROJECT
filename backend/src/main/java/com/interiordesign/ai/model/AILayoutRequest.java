package com.interiordesign.ai.model;

import com.interiordesign.model.Furniture;
import java.util.List;

/**
 * Request DTO for AI layout generation
 * Contains room parameters and available furniture catalog
 */
public class AILayoutRequest {
    
    private double roomLength;
    private double roomWidth;
    private int budget;
    private List<Furniture> availableFurniture;

    // Constructors
    public AILayoutRequest() {
    }

    public AILayoutRequest(double roomLength, double roomWidth, int budget, List<Furniture> availableFurniture) {
        this.roomLength = roomLength;
        this.roomWidth = roomWidth;
        this.budget = budget;
        this.availableFurniture = availableFurniture;
    }

    // Getters and Setters
    public double getRoomLength() {
        return roomLength;
    }

    public void setRoomLength(double roomLength) {
        this.roomLength = roomLength;
    }

    public double getRoomWidth() {
        return roomWidth;
    }

    public void setRoomWidth(double roomWidth) {
        this.roomWidth = roomWidth;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Furniture> getAvailableFurniture() {
        return availableFurniture;
    }

    public void setAvailableFurniture(List<Furniture> availableFurniture) {
        this.availableFurniture = availableFurniture;
    }
}
