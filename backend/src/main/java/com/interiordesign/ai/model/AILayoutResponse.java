package com.interiordesign.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Response DTO from AI layout generation
 * Contains AI-suggested furniture placements and reasoning
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AILayoutResponse {
    
    private List<AIFurniturePlacement> furniture;
    private int totalCost;
    private String reasoning;

    // Constructors
    public AILayoutResponse() {
    }

    public AILayoutResponse(List<AIFurniturePlacement> furniture, int totalCost, String reasoning) {
        this.furniture = furniture;
        this.totalCost = totalCost;
        this.reasoning = reasoning;
    }

    // Getters and Setters
    public List<AIFurniturePlacement> getFurniture() {
        return furniture;
    }

    public void setFurniture(List<AIFurniturePlacement> furniture) {
        this.furniture = furniture;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    /**
     * Nested class representing a single furniture placement suggestion from AI
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AIFurniturePlacement {
        private String name;
        private double x;
        private double y;
        private double rotation;
        private String reasoning;

        // Constructors
        public AIFurniturePlacement() {
        }

        public AIFurniturePlacement(String name, double x, double y, double rotation, String reasoning) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.reasoning = reasoning;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public double getRotation() {
            return rotation;
        }

        public void setRotation(double rotation) {
            this.rotation = rotation;
        }

        public String getReasoning() {
            return reasoning;
        }

        public void setReasoning(String reasoning) {
            this.reasoning = reasoning;
        }

        @Override
        public String toString() {
            return "AIFurniturePlacement{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", rotation=" + rotation +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AILayoutResponse{" +
                "furnitureCount=" + (furniture != null ? furniture.size() : 0) +
                ", totalCost=" + totalCost +
                ", reasoning='" + reasoning + '\'' +
                '}';
    }
}
