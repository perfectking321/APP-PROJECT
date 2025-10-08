-- ====================================
-- Interior Design Database Schema
-- ====================================

-- Drop table if exists
DROP TABLE IF EXISTS furniture;

-- Create furniture table
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
