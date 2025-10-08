package com.interiordesign.dao;

import com.interiordesign.model.Furniture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Furniture entity
 * Handles all database operations for furniture catalog
 */
@Repository
public class FurnitureDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(FurnitureDAO.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public FurnitureDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * RowMapper to convert ResultSet to Furniture object
     */
    private static class FurnitureRowMapper implements RowMapper<Furniture> {
        @Override
        public Furniture mapRow(ResultSet rs, int rowNum) throws SQLException {
            Furniture furniture = new Furniture();
            furniture.setId(rs.getLong("id"));
            furniture.setName(rs.getString("name"));
            furniture.setWidth(rs.getDouble("width"));
            furniture.setDepth(rs.getDouble("depth"));
            furniture.setPrice(rs.getInt("price"));
            furniture.setCategory(rs.getString("category"));
            return furniture;
        }
    }
    
    /**
     * Find all furniture items in the catalog
     * @return List of all furniture
     */
    public List<Furniture> findAll() {
        String sql = "SELECT * FROM furniture ORDER BY category, price";
        logger.debug("Fetching all furniture from database");
        List<Furniture> furniture = jdbcTemplate.query(sql, new FurnitureRowMapper());
        logger.debug("Retrieved {} furniture items", furniture.size());
        return furniture;
    }
    
    /**
     * Find furniture by ID
     * @param id Furniture ID
     * @return Optional containing furniture if found
     */
    public Optional<Furniture> findById(Long id) {
        String sql = "SELECT * FROM furniture WHERE id = ?";
        logger.debug("Fetching furniture with id: {}", id);
        List<Furniture> results = jdbcTemplate.query(sql, new FurnitureRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Find furniture by name
     * @param name Furniture name
     * @return Optional containing furniture if found
     */
    public Optional<Furniture> findByName(String name) {
        String sql = "SELECT * FROM furniture WHERE LOWER(name) = LOWER(?)";
        logger.debug("Fetching furniture with name: {}", name);
        List<Furniture> results = jdbcTemplate.query(sql, new FurnitureRowMapper(), name);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Find furniture within price range
     * @param maxPrice Maximum price
     * @return List of furniture within budget
     */
    public List<Furniture> findByPriceRange(int maxPrice) {
        String sql = "SELECT * FROM furniture WHERE price <= ? ORDER BY price";
        logger.debug("Fetching furniture with price <= {}", maxPrice);
        return jdbcTemplate.query(sql, new FurnitureRowMapper(), maxPrice);
    }
    
    /**
     * Find furniture by category
     * @param category Furniture category
     * @return List of furniture in category
     */
    public List<Furniture> findByCategory(String category) {
        String sql = "SELECT * FROM furniture WHERE LOWER(category) = LOWER(?) ORDER BY price";
        logger.debug("Fetching furniture in category: {}", category);
        return jdbcTemplate.query(sql, new FurnitureRowMapper(), category);
    }
    
    /**
     * Get count of all furniture items
     * @return Total count
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM furniture";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
