package com.interiordesign.ai;

import com.interiordesign.ai.model.AILayoutRequest;
import com.interiordesign.model.Furniture;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Builds AI prompts for furniture layout generation
 * Creates structured prompts with room specifications and furniture catalog
 */
@Component
public class AIPromptBuilder {
    
    /**
     * Build a detailed prompt for AI layout generation
     * @param request Layout request with room dimensions, budget, and furniture catalog
     * @return Formatted prompt string for AI
     */
    public String buildLayoutPrompt(AILayoutRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        // System instructions
        prompt.append("You are an expert interior designer. Design an optimal furniture layout for the given room.\n\n");
        
        // Room specifications
        prompt.append("=== ROOM SPECIFICATIONS ===\n");
        prompt.append(String.format("Dimensions: %.1fm (length) × %.1fm (width)\n", 
                request.getRoomLength(), request.getRoomWidth()));
        prompt.append(String.format("Total Area: %.1f m²\n", request.getRoomLength() * request.getRoomWidth()));
        prompt.append(String.format("Budget: $%d\n\n", request.getBudget()));
        
        // Available furniture catalog
        prompt.append("=== AVAILABLE FURNITURE CATALOG ===\n");
        List<Furniture> furniture = request.getAvailableFurniture();
        
        if (furniture != null && !furniture.isEmpty()) {
            for (Furniture item : furniture) {
                prompt.append(String.format("- %s: %.1fm × %.1fm, $%d (category: %s)\n",
                        item.getName(),
                        item.getWidth(),
                        item.getDepth(),
                        item.getPrice(),
                        item.getCategory()));
            }
        }
        prompt.append("\n");
        
        // Design requirements
        prompt.append("=== DESIGN REQUIREMENTS ===\n");
        prompt.append("1. All furniture MUST fit within room boundaries\n");
        prompt.append("2. Maintain at least 0.5m clearance from all walls\n");
        prompt.append("3. Total cost MUST NOT exceed the budget\n");
        prompt.append("4. Avoid furniture overlaps - leave walking space between items\n");
        prompt.append("5. Create a functional and aesthetically pleasing layout\n");
        prompt.append("6. Consider typical room flow and furniture relationships\n");
        prompt.append("7. Place larger items (sofas, beds) against walls when possible\n");
        prompt.append("8. Position seating to face entertainment centers or conversation areas\n\n");
        
        // Coordinate system explanation
        prompt.append("=== COORDINATE SYSTEM ===\n");
        prompt.append("- Origin (0, 0) is at the BOTTOM-LEFT corner of the room\n");
        prompt.append("- X-axis: horizontal (0 to room length)\n");
        prompt.append("- Y-axis: vertical (0 to room width)\n");
        prompt.append("- Furniture position (x, y) represents the BOTTOM-LEFT corner of the furniture\n");
        prompt.append("- Example: Room 5m × 4m, place sofa at (0.5, 0.5) means 0.5m from left wall and 0.5m from bottom wall\n\n");
        
        // Output format requirements
        prompt.append("=== REQUIRED OUTPUT FORMAT ===\n");
        prompt.append("Respond ONLY with valid JSON in this EXACT format (no markdown, no code blocks):\n");
        prompt.append("{\n");
        prompt.append("  \"furniture\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"name\": \"Sofa\",\n");
        prompt.append("      \"x\": 1.0,\n");
        prompt.append("      \"y\": 0.5,\n");
        prompt.append("      \"rotation\": 0,\n");
        prompt.append("      \"reasoning\": \"Placed against longest wall for optimal room flow\"\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"totalCost\": 1500,\n");
        prompt.append("  \"reasoning\": \"Overall layout explanation focusing on functionality and aesthetics\"\n");
        prompt.append("}\n\n");
        
        // Additional notes
        prompt.append("IMPORTANT NOTES:\n");
        prompt.append("- Use furniture names EXACTLY as listed in the catalog\n");
        prompt.append("- Ensure x + furniture_width <= room_length\n");
        prompt.append("- Ensure y + furniture_depth <= room_width\n");
        prompt.append("- Calculate totalCost by summing prices of selected furniture\n");
        prompt.append("- Return pure JSON only, no extra text before or after\n");
        
        return prompt.toString();
    }
}
