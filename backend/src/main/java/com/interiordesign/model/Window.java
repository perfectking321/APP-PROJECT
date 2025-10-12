package com.interiordesign.model;

/**
 * Represents a window in a floor plan
 */
public class Window {
    private Long id;
    private Long floorPlanId; // Foreign key to FloorPlan - INDEXED
    
    private double x; // Center X position
    private double y; // Center Y position
    private double width; // Window width
    
    // Constructors
    public Window() {
        this.width = 120.0; // Default window width
    }
    
    public Window(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFloorPlanId() {
        return floorPlanId;
    }
    
    public void setFloorPlanId(Long floorPlanId) {
        this.floorPlanId = floorPlanId;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getWidth() {
        return width;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    @Override
    public String toString() {
        return "Window{" +
                "id=" + id +
                ", floorPlanId=" + floorPlanId +
                ", position=(" + x + "," + y + ")" +
                ", width=" + width +
                '}';
    }
}
