-- ====================================
-- Sample Furniture Data
-- ====================================

-- Living Room Furniture
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Sofa', 2.0, 0.9, 800, 'sofa'),
('Loveseat', 1.5, 0.9, 600, 'sofa'),
('Sectional Sofa', 3.0, 2.0, 1500, 'sofa');

-- Tables
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Coffee Table', 1.2, 0.6, 200, 'coffee'),
('Round Coffee Table', 0.9, 0.9, 250, 'coffee'),
('Side Table', 0.5, 0.5, 100, 'sidetable'),
('End Table', 0.6, 0.4, 120, 'sidetable');

-- Entertainment
INSERT INTO furniture (name, width, depth, price, category) VALUES
('TV Stand', 1.5, 0.4, 300, 'tvstand'),
('Media Console', 2.0, 0.5, 450, 'tvstand');

-- Storage
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Bookshelf', 0.8, 0.3, 150, 'bookshelf'),
('Tall Bookshelf', 1.0, 0.4, 250, 'bookshelf'),
('Cabinet', 1.2, 0.5, 400, 'storage');

-- Seating
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Armchair', 0.8, 0.8, 400, 'armchair'),
('Recliner', 0.9, 1.0, 550, 'armchair'),
('Ottoman', 0.6, 0.6, 150, 'ottoman');

-- Dining
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Dining Table', 1.8, 0.9, 600, 'dining'),
('Dining Chair', 0.5, 0.5, 100, 'chair');

-- Bedroom
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Queen Bed', 2.1, 2.3, 1200, 'bed'),
('King Bed', 2.3, 2.3, 1500, 'bed'),
('Nightstand', 0.5, 0.4, 150, 'nightstand'),
('Dresser', 1.5, 0.6, 500, 'dresser');

-- Office
INSERT INTO furniture (name, width, depth, price, category) VALUES
('Desk', 1.4, 0.7, 400, 'desk'),
('Office Chair', 0.6, 0.6, 200, 'chair'),
('Filing Cabinet', 0.5, 0.6, 250, 'storage');
