package com.interiordesign.model;

/**
 * Represents a wall segment in a floor plan
 */
public class Wall {
    private Long id;
    private Long floorPlanId; // Foreign key to FloorPlan - INDEXED
    
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double thickness; // Wall thickness in pixels/units
    
    // Constructors
    public Wall() {
        this.thickness = 8.0; // Default thickness
    }
    
    public Wall(double startX, double startY, double endX, double endY) {
        this();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
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
    
    public double getStartX() {
        return startX;
    }
    
    public void setStartX(double startX) {
        this.startX = startX;
    }
    
    public double getStartY() {
        return startY;
    }
    
    public void setStartY(double startY) {
        this.startY = startY;
    }
    
    public double getEndX() {
        return endX;
    }
    
    public void setEndX(double endX) {
        this.endX = endX;
    }
    
    public double getEndY() {
        return endY;
    }
    
    public void setEndY(double endY) {
        this.endY = endY;
    }
    
    public double getThickness() {
        return thickness;
    }
    
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }
    
    /**
     * Calculate wall length
     */
    public double getLength() {
        double dx = endX - startX;
        double dy = endY - startY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Check if this wall is horizontal
     */
    public boolean isHorizontal() {
        return Math.abs(endY - startY) < 1.0;
    }
    
    /**
     * Check if this wall is vertical
     */
    public boolean isVertical() {
        return Math.abs(endX - startX) < 1.0;
    }
    
    @Override
    public String toString() {
        return "Wall{" +
                "id=" + id +
                ", floorPlanId=" + floorPlanId +
                ", start=(" + startX + "," + startY + ")" +
                ", end=(" + endX + "," + endY + ")" +
                ", thickness=" + thickness +
                ", length=" + String.format("%.2f", getLength()) +
                '}';
    }
}
