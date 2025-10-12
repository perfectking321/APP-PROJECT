package com.interiordesign.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an uploaded floor plan with parsed architectural elements
 */
public class FloorPlan {
    private Long id;
    private String originalImageUrl;
    private String processedImageUrl;
    private String aiAnalysisJson; // Raw JSON from Python service
    private double validationConfidence;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
    
    private List<Wall> walls = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();
    private List<Window> windows = new ArrayList<>();
    private List<RoomBoundary> rooms = new ArrayList<>();
    
    // Constructors
    public FloorPlan() {
        this.uploadedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public FloorPlan(String originalImageUrl) {
        this();
        this.originalImageUrl = originalImageUrl;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalImageUrl() {
        return originalImageUrl;
    }
    
    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }
    
    public String getProcessedImageUrl() {
        return processedImageUrl;
    }
    
    public void setProcessedImageUrl(String processedImageUrl) {
        this.processedImageUrl = processedImageUrl;
    }
    
    public String getAiAnalysisJson() {
        return aiAnalysisJson;
    }
    
    public void setAiAnalysisJson(String aiAnalysisJson) {
        this.aiAnalysisJson = aiAnalysisJson;
    }
    
    public double getValidationConfidence() {
        return validationConfidence;
    }
    
    public void setValidationConfidence(double validationConfidence) {
        this.validationConfidence = validationConfidence;
    }
    
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Wall> getWalls() {
        return walls;
    }
    
    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }
    
    public List<Door> getDoors() {
        return doors;
    }
    
    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }
    
    public List<Window> getWindows() {
        return windows;
    }
    
    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }
    
    public List<RoomBoundary> getRooms() {
        return rooms;
    }
    
    public void setRooms(List<RoomBoundary> rooms) {
        this.rooms = rooms;
    }
    
    @Override
    public String toString() {
        return "FloorPlan{" +
                "id=" + id +
                ", originalImageUrl='" + originalImageUrl + '\'' +
                ", validationConfidence=" + validationConfidence +
                ", walls=" + walls.size() +
                ", doors=" + doors.size() +
                ", windows=" + windows.size() +
                ", rooms=" + rooms.size() +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
