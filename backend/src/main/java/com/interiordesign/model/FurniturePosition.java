package com.interiordesign.model;

/**
 * Represents the position of a furniture piece in the room layout
 * Contains furniture reference and x, y coordinates
 */
public class FurniturePosition {
    
    private Furniture furniture;
    private double x;  // X coordinate in meters from room origin (bottom-left)
    private double y;  // Y coordinate in meters from room origin (bottom-left)

    // Constructors
    public FurniturePosition() {
    }

    public FurniturePosition(Furniture furniture, double x, double y) {
        this.furniture = furniture;
        this.x = x;
        this.y = y;
    }

    // Getters and Setters
    public Furniture getFurniture() {
        return furniture;
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
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
    
    /**
     * Check if this furniture position overlaps with another
     */
    public boolean overlapsWith(FurniturePosition other) {
        // Calculate boundaries
        double thisRight = this.x + this.furniture.getWidth();
        double thisTop = this.y + this.furniture.getDepth();
        double otherRight = other.x + other.furniture.getWidth();
        double otherTop = other.y + other.furniture.getDepth();
        
        // Check for overlap (no overlap if completely separated on any axis)
        return !(thisRight <= other.x || this.x >= otherRight ||
                 thisTop <= other.y || this.y >= otherTop);
    }
    
    /**
     * Check if furniture fits within room boundaries
     */
    public boolean fitsInRoom(double roomLength, double roomWidth) {
        return x >= 0 && y >= 0 &&
               (x + furniture.getWidth()) <= roomLength &&
               (y + furniture.getDepth()) <= roomWidth;
    }

    @Override
    public String toString() {
        return "FurniturePosition{" +
                "furniture=" + furniture.getName() +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
