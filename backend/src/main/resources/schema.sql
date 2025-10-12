-- ====================================
-- Interior Design Database Schema
-- ====================================

-- Drop tables if exists (in correct order due to foreign keys)
DROP TABLE IF EXISTS windows;
DROP TABLE IF EXISTS doors;
DROP TABLE IF EXISTS walls;
DROP TABLE IF EXISTS room_boundaries;
DROP TABLE IF EXISTS floor_plans;
DROP TABLE IF EXISTS furniture;

-- ====================================
-- Furniture Table
-- ====================================
CREATE TABLE furniture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    width DOUBLE NOT NULL,
    depth DOUBLE NOT NULL,
    price INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    CONSTRAINT chk_width CHECK (width > 0),
    CONSTRAINT chk_depth CHECK (depth > 0),
    CONSTRAINT chk_price CHECK (price > 0)
);

-- Create index for faster queries
CREATE INDEX idx_furniture_category ON furniture(category);
CREATE INDEX idx_furniture_price ON furniture(price);

-- ====================================
-- Floor Plans Table
-- ====================================
CREATE TABLE floor_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_image_url VARCHAR(500) NOT NULL,
    processed_image_url VARCHAR(500),
    ai_analysis_json TEXT,
    validation_confidence DOUBLE NOT NULL DEFAULT 0.0,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_confidence CHECK (validation_confidence >= 0.0 AND validation_confidence <= 1.0)
);

-- ====================================
-- Walls Table (with indexed floor_plan_id)
-- ====================================
CREATE TABLE walls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_plan_id BIGINT NOT NULL,
    start_x DOUBLE NOT NULL,
    start_y DOUBLE NOT NULL,
    end_x DOUBLE NOT NULL,
    end_y DOUBLE NOT NULL,
    thickness DOUBLE NOT NULL DEFAULT 8.0,
    FOREIGN KEY (floor_plan_id) REFERENCES floor_plans(id) ON DELETE CASCADE
);

-- INDEXED COLUMN for faster queries
CREATE INDEX idx_walls_floor_plan_id ON walls(floor_plan_id);

-- ====================================
-- Doors Table (with indexed floor_plan_id)
-- ====================================
CREATE TABLE doors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_plan_id BIGINT NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    width DOUBLE NOT NULL DEFAULT 80.0,
    rotation INT NOT NULL DEFAULT 0,
    FOREIGN KEY (floor_plan_id) REFERENCES floor_plans(id) ON DELETE CASCADE,
    CONSTRAINT chk_rotation CHECK (rotation IN (0, 90, 180, 270))
);

-- INDEXED COLUMN for faster queries
CREATE INDEX idx_doors_floor_plan_id ON doors(floor_plan_id);

-- ====================================
-- Windows Table (with indexed floor_plan_id)
-- ====================================
CREATE TABLE windows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_plan_id BIGINT NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    width DOUBLE NOT NULL DEFAULT 120.0,
    FOREIGN KEY (floor_plan_id) REFERENCES floor_plans(id) ON DELETE CASCADE
);

-- INDEXED COLUMN for faster queries
CREATE INDEX idx_windows_floor_plan_id ON windows(floor_plan_id);

-- ====================================
-- Room Boundaries Table (with indexed floor_plan_id)
-- ====================================
CREATE TABLE room_boundaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_plan_id BIGINT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    width DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    FOREIGN KEY (floor_plan_id) REFERENCES floor_plans(id) ON DELETE CASCADE
);

-- INDEXED COLUMN for faster queries
CREATE INDEX idx_room_boundaries_floor_plan_id ON room_boundaries(floor_plan_id);
CREATE INDEX idx_room_boundaries_room_type ON room_boundaries(room_type);
