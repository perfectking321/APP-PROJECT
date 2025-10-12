package com.interiordesign.model;

/**
 * Represents a detected room boundary from floor plan
 */
public class RoomBoundary {
    private Long id;
    private Long floorPlanId; // Foreign key to FloorPlan - INDEXED
    
    private String roomType; // LIVING_ROOM, BEDROOM, KITCHEN, etc.
    private double x; // Top-left X
    private double y; // Top-left Y
    private double width;
    private double height;
    
    // Constructors
    public RoomBoundary() {
    }
    
    public RoomBoundary(String roomType, double x, double y, double width, double height) {
        this.roomType = roomType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
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
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    /**
     * Calculate room area in square units
     */
    public double getArea() {
        return width * height;
    }
    
    /**
     * Check if a point is inside this room
     */
    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= x + width &&
               pointY >= y && pointY <= y + height;
    }
    
    @Override
    public String toString() {
        return "RoomBoundary{" +
                "id=" + id +
                ", floorPlanId=" + floorPlanId +
                ", roomType='" + roomType + '\'' +
                ", bounds=(" + x + "," + y + "," + width + "x" + height + ")" +
                ", area=" + String.format("%.2f", getArea()) +
                '}';
    }
}
