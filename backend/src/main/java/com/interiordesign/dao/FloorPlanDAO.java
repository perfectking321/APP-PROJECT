package com.interiordesign.dao;

import com.interiordesign.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FloorPlanDAO {
    
    private final JdbcTemplate jdbcTemplate;
    
    public FloorPlanDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Save floor plan with all related entities (walls, doors, windows, rooms)
     */
    public FloorPlan save(FloorPlan floorPlan) {
        // Insert floor plan
        String sql = "INSERT INTO floor_plans (original_image_url, processed_image_url, " +
                    "ai_analysis_json, validation_confidence, uploaded_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, floorPlan.getOriginalImageUrl());
            ps.setString(2, floorPlan.getProcessedImageUrl());
            ps.setString(3, floorPlan.getAiAnalysisJson());
            ps.setDouble(4, floorPlan.getValidationConfidence());
            ps.setTimestamp(5, Timestamp.valueOf(floorPlan.getUploadedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(floorPlan.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Long floorPlanId = keyHolder.getKey().longValue();
        floorPlan.setId(floorPlanId);
        
        // Save related entities
        saveWalls(floorPlanId, floorPlan.getWalls());
        saveDoors(floorPlanId, floorPlan.getDoors());
        saveWindows(floorPlanId, floorPlan.getWindows());
        saveRooms(floorPlanId, floorPlan.getRooms());
        
        return floorPlan;
    }
    
    /**
     * Find floor plan by ID with all related entities
     */
    public FloorPlan findById(Long id) {
        String sql = "SELECT * FROM floor_plans WHERE id = ?";
        
        FloorPlan floorPlan = jdbcTemplate.queryForObject(sql, floorPlanRowMapper(), id);
        
        if (floorPlan != null) {
            // Load related entities
            floorPlan.setWalls(findWallsByFloorPlanId(id));
            floorPlan.setDoors(findDoorsByFloorPlanId(id));
            floorPlan.setWindows(findWindowsByFloorPlanId(id));
            floorPlan.setRooms(findRoomsByFloorPlanId(id));
        }
        
        return floorPlan;
    }
    
    /**
     * Save walls for a floor plan
     */
    private void saveWalls(Long floorPlanId, List<Wall> walls) {
        if (walls == null || walls.isEmpty()) return;
        
        String sql = "INSERT INTO walls (floor_plan_id, start_x, start_y, end_x, end_y, thickness) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        for (Wall wall : walls) {
            wall.setFloorPlanId(floorPlanId);
            jdbcTemplate.update(sql, floorPlanId, wall.getStartX(), wall.getStartY(),
                              wall.getEndX(), wall.getEndY(), wall.getThickness());
        }
    }
    
    /**
     * Save doors for a floor plan
     */
    private void saveDoors(Long floorPlanId, List<Door> doors) {
        if (doors == null || doors.isEmpty()) return;
        
        String sql = "INSERT INTO doors (floor_plan_id, x, y, width, rotation) VALUES (?, ?, ?, ?, ?)";
        
        for (Door door : doors) {
            door.setFloorPlanId(floorPlanId);
            jdbcTemplate.update(sql, floorPlanId, door.getX(), door.getY(),
                              door.getWidth(), door.getRotation());
        }
    }
    
    /**
     * Save windows for a floor plan
     */
    private void saveWindows(Long floorPlanId, List<Window> windows) {
        if (windows == null || windows.isEmpty()) return;
        
        String sql = "INSERT INTO windows (floor_plan_id, x, y, width) VALUES (?, ?, ?, ?)";
        
        for (Window window : windows) {
            window.setFloorPlanId(floorPlanId);
            jdbcTemplate.update(sql, floorPlanId, window.getX(), window.getY(), window.getWidth());
        }
    }
    
    /**
     * Save room boundaries for a floor plan
     */
    private void saveRooms(Long floorPlanId, List<RoomBoundary> rooms) {
        if (rooms == null || rooms.isEmpty()) return;
        
        String sql = "INSERT INTO room_boundaries (floor_plan_id, room_type, x, y, width, height) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        for (RoomBoundary room : rooms) {
            room.setFloorPlanId(floorPlanId);
            jdbcTemplate.update(sql, floorPlanId, room.getRoomType(), room.getX(), room.getY(),
                              room.getWidth(), room.getHeight());
        }
    }
    
    /**
     * Find walls by floor plan ID (uses indexed column)
     */
    private List<Wall> findWallsByFloorPlanId(Long floorPlanId) {
        String sql = "SELECT * FROM walls WHERE floor_plan_id = ?";
        return jdbcTemplate.query(sql, wallRowMapper(), floorPlanId);
    }
    
    /**
     * Find doors by floor plan ID (uses indexed column)
     */
    private List<Door> findDoorsByFloorPlanId(Long floorPlanId) {
        String sql = "SELECT * FROM doors WHERE floor_plan_id = ?";
        return jdbcTemplate.query(sql, doorRowMapper(), floorPlanId);
    }
    
    /**
     * Find windows by floor plan ID (uses indexed column)
     */
    private List<Window> findWindowsByFloorPlanId(Long floorPlanId) {
        String sql = "SELECT * FROM windows WHERE floor_plan_id = ?";
        return jdbcTemplate.query(sql, windowRowMapper(), floorPlanId);
    }
    
    /**
     * Find room boundaries by floor plan ID (uses indexed column)
     */
    private List<RoomBoundary> findRoomsByFloorPlanId(Long floorPlanId) {
        String sql = "SELECT * FROM room_boundaries WHERE floor_plan_id = ?";
        return jdbcTemplate.query(sql, roomBoundaryRowMapper(), floorPlanId);
    }
    
    // ===== Row Mappers =====
    
    private RowMapper<FloorPlan> floorPlanRowMapper() {
        return (rs, rowNum) -> {
            FloorPlan floorPlan = new FloorPlan();
            floorPlan.setId(rs.getLong("id"));
            floorPlan.setOriginalImageUrl(rs.getString("original_image_url"));
            floorPlan.setProcessedImageUrl(rs.getString("processed_image_url"));
            floorPlan.setAiAnalysisJson(rs.getString("ai_analysis_json"));
            floorPlan.setValidationConfidence(rs.getDouble("validation_confidence"));
            floorPlan.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            floorPlan.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return floorPlan;
        };
    }
    
    private RowMapper<Wall> wallRowMapper() {
        return (rs, rowNum) -> {
            Wall wall = new Wall();
            wall.setId(rs.getLong("id"));
            wall.setFloorPlanId(rs.getLong("floor_plan_id"));
            wall.setStartX(rs.getDouble("start_x"));
            wall.setStartY(rs.getDouble("start_y"));
            wall.setEndX(rs.getDouble("end_x"));
            wall.setEndY(rs.getDouble("end_y"));
            wall.setThickness(rs.getDouble("thickness"));
            return wall;
        };
    }
    
    private RowMapper<Door> doorRowMapper() {
        return (rs, rowNum) -> {
            Door door = new Door();
            door.setId(rs.getLong("id"));
            door.setFloorPlanId(rs.getLong("floor_plan_id"));
            door.setX(rs.getDouble("x"));
            door.setY(rs.getDouble("y"));
            door.setWidth(rs.getDouble("width"));
            door.setRotation(rs.getInt("rotation"));
            return door;
        };
    }
    
    private RowMapper<Window> windowRowMapper() {
        return (rs, rowNum) -> {
            Window window = new Window();
            window.setId(rs.getLong("id"));
            window.setFloorPlanId(rs.getLong("floor_plan_id"));
            window.setX(rs.getDouble("x"));
            window.setY(rs.getDouble("y"));
            window.setWidth(rs.getDouble("width"));
            return window;
        };
    }
    
    private RowMapper<RoomBoundary> roomBoundaryRowMapper() {
        return (rs, rowNum) -> {
            RoomBoundary room = new RoomBoundary();
            room.setId(rs.getLong("id"));
            room.setFloorPlanId(rs.getLong("floor_plan_id"));
            room.setRoomType(rs.getString("room_type"));
            room.setX(rs.getDouble("x"));
            room.setY(rs.getDouble("y"));
            room.setWidth(rs.getDouble("width"));
            room.setHeight(rs.getDouble("height"));
            return room;
        };
    }
}
