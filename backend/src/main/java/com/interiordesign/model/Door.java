package com.interiordesign.model;

/**
 * Represents a door in a floor plan
 */
public class Door {
    private Long id;
    private Long floorPlanId; // Foreign key to FloorPlan - INDEXED
    
    private double x; // Center X position
    private double y; // Center Y position
    private double width; // Door width
    private int rotation; // Rotation angle: 0, 90, 180, 270 degrees
    
    // Constructors
    public Door() {
        this.width = 80.0; // Default door width
        this.rotation = 0;
    }
    
    public Door(double x, double y, double width, int rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.rotation = rotation;
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
    
    public int getRotation() {
        return rotation;
    }
    
    public void setRotation(int rotation) {
        // Normalize rotation to 0, 90, 180, 270
        this.rotation = ((rotation % 360) + 360) % 360;
    }
    
    /**
     * Check if door is oriented horizontally
     */
    public boolean isHorizontal() {
        return rotation == 0 || rotation == 180;
    }
    
    /**
     * Check if door is oriented vertically
     */
    public boolean isVertical() {
        return rotation == 90 || rotation == 270;
    }
    
    @Override
    public String toString() {
        return "Door{" +
                "id=" + id +
                ", floorPlanId=" + floorPlanId +
                ", position=(" + x + "," + y + ")" +
                ", width=" + width +
                ", rotation=" + rotation + "°" +
                '}';
    }
}
