package com.interiordesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Furniture entity representing a piece of furniture from database
 * Contains dimensions, price, and category information
 */
public class Furniture {
    
    private Long id;
    private String name;
    private double width;  // Width in meters
    private double depth;  // Depth in meters (called 'length' in frontend)
    private int price;     // Price in dollars
    private String category;

    // Constructors
    public Furniture() {
    }

    public Furniture(Long id, String name, double width, double depth, int price, String category) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.depth = depth;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
    
    // Alias for frontend compatibility (depth = length)
    @JsonProperty("length")
    public double getLength() {
        return depth;
    }
    
    @JsonProperty("length")
    public void setLength(double length) {
        this.depth = length;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", depth=" + depth +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
