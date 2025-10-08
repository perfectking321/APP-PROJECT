package com.interiordesign.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Room entity representing user input for layout generation
 * Contains room dimensions and budget constraints
 */
public class Room {
    
    @NotNull(message = "Room length is required")
    @Min(value = 3, message = "Length must be at least 3 meters")
    @Max(value = 15, message = "Length must not exceed 15 meters")
    private Double length;
    
    @NotNull(message = "Room width is required")
    @Min(value = 3, message = "Width must be at least 3 meters")
    @Max(value = 15, message = "Width must not exceed 15 meters")
    private Double width;
    
    @NotNull(message = "Budget is required")
    @Min(value = 500, message = "Budget must be at least $500")
    @Max(value = 10000, message = "Budget must not exceed $10,000")
    private Integer budget;

    // Constructors
    public Room() {
    }

    public Room(Double length, Double width, Integer budget) {
        this.length = length;
        this.width = width;
        this.budget = budget;
    }

    // Getters and Setters
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }
    
    /**
     * Calculate room area in square meters
     */
    public double getArea() {
        return length * width;
    }

    @Override
    public String toString() {
        return "Room{" +
                "length=" + length +
                ", width=" + width +
                ", budget=" + budget +
                ", area=" + getArea() + "m²" +
                '}';
    }
}
