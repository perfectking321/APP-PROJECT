import logging
import xml.etree.ElementTree as ET
import re

logger = logging.getLogger(__name__)

def parse_cubicasa_svg(svg_string):
    """
    Parse CubiCasa SVG output to extract structured data
    
    Expected SVG structure from CubiCasa:
    - <polygon class="wall"> - Wall segments
    - <rect class="door"> - Doors
    - <rect class="window"> - Windows
    - <text class="room_label"> - Room type labels
    - <polygon class="room_X"> - Room boundaries (X = room type)
    
    Args:
        svg_string: SVG XML string from CubiCasa model
        
    Returns:
        dict: {
            'walls': [{'startX', 'startY', 'endX', 'endY', 'thickness'}],
            'doors': [{'x', 'y', 'width', 'rotation'}],
            'windows': [{'x', 'y', 'width'}],
            'rooms': [{'type', 'bounds': {'x', 'y', 'width', 'height'}}]
        }
    """
    try:
        # Parse SVG XML
        root = ET.fromstring(svg_string)
        
        # Define namespace (SVG uses default namespace)
        ns = {'svg': 'http://www.w3.org/2000/svg'}
        
        result = {
            'walls': [],
            'doors': [],
            'windows': [],
            'rooms': []
        }
        
        # Parse walls (polygons or lines with class="wall")
        for wall_elem in root.findall(".//*[@class='wall']", ns):
            if wall_elem.tag.endswith('polygon'):
                points_str = wall_elem.get('points', '')
                walls = parse_polygon_as_walls(points_str)
                result['walls'].extend(walls)
            elif wall_elem.tag.endswith('line'):
                wall = {
                    'startX': float(wall_elem.get('x1', 0)),
                    'startY': float(wall_elem.get('y1', 0)),
                    'endX': float(wall_elem.get('x2', 0)),
                    'endY': float(wall_elem.get('y2', 0)),
                    'thickness': float(wall_elem.get('stroke-width', 8))
                }
                result['walls'].append(wall)
        
        # Parse doors (rectangles with class="door")
        for door_elem in root.findall(".//*[@class='door']", ns):
            door = {
                'x': float(door_elem.get('x', 0)),
                'y': float(door_elem.get('y', 0)),
                'width': float(door_elem.get('width', 80)),
                'rotation': int(door_elem.get('transform', '').split('rotate(')[-1].split(')')[0] if 'rotate' in door_elem.get('transform', '') else 0)
            }
            result['doors'].append(door)
        
        # Parse windows (rectangles with class="window")
        for window_elem in root.findall(".//*[@class='window']", ns):
            window = {
                'x': float(window_elem.get('x', 0)),
                'y': float(window_elem.get('y', 0)),
                'width': float(window_elem.get('width', 120))
            }
            result['windows'].append(window)
        
        # Parse room labels and boundaries
        for text_elem in root.findall(".//*[@class='room_label']", ns):
            room_type = text_elem.text.strip() if text_elem.text else 'UNKNOWN'
            x = float(text_elem.get('x', 0))
            y = float(text_elem.get('y', 0))
            
            # Try to find corresponding room boundary polygon
            room_bounds = find_room_bounds_near_label(root, x, y, ns)
            
            room = {
                'type': normalize_room_type(room_type),
                'bounds': room_bounds
            }
            result['rooms'].append(room)
        
        logger.info(f"Parsed SVG: {len(result['walls'])} walls, {len(result['doors'])} doors, "
                   f"{len(result['windows'])} windows, {len(result['rooms'])} rooms")
        
        return result
        
    except Exception as e:
        logger.error(f"SVG parsing failed: {str(e)}")
        # Return empty structure on failure
        return {
            'walls': [],
            'doors': [],
            'windows': [],
            'rooms': []
        }

def parse_polygon_as_walls(points_str):
    """
    Convert polygon points to individual wall segments
    Example: "0,0 500,0 500,400 0,400" -> 4 walls
    """
    walls = []
    
    # Parse points
    points = []
    for point_pair in points_str.split():
        coords = point_pair.split(',')
        if len(coords) == 2:
            points.append((float(coords[0]), float(coords[1])))
    
    # Create wall segments between consecutive points
    for i in range(len(points)):
        start = points[i]
        end = points[(i + 1) % len(points)]  # Wrap around to first point
        
        walls.append({
            'startX': start[0],
            'startY': start[1],
            'endX': end[0],
            'endY': end[1],
            'thickness': 8.0  # Default thickness
        })
    
    return walls

def find_room_bounds_near_label(root, label_x, label_y, ns):
    """
    Find room boundary polygon near a room label
    Searches for polygons with class starting with "room_"
    """
    # Look for room boundary polygons
    for room_poly in root.findall(".//*[starts-with(@class, 'room_')]", ns):
        if room_poly.tag.endswith('polygon'):
            points_str = room_poly.get('points', '')
            bounds = calculate_bounding_box(points_str)
            
            # Check if label is inside bounds
            if (bounds['x'] <= label_x <= bounds['x'] + bounds['width'] and
                bounds['y'] <= label_y <= bounds['y'] + bounds['height']):
                return bounds
    
    # Default bounds if not found
    return {
        'x': label_x - 100,
        'y': label_y - 100,
        'width': 200,
        'height': 200
    }

def calculate_bounding_box(points_str):
    """Calculate bounding box from polygon points"""
    points = []
    for point_pair in points_str.split():
        coords = point_pair.split(',')
        if len(coords) == 2:
            points.append((float(coords[0]), float(coords[1])))
    
    if not points:
        return {'x': 0, 'y': 0, 'width': 0, 'height': 0}
    
    xs = [p[0] for p in points]
    ys = [p[1] for p in points]
    
    return {
        'x': min(xs),
        'y': min(ys),
        'width': max(xs) - min(xs),
        'height': max(ys) - min(ys)
    }

def normalize_room_type(room_label):
    """
    Normalize CubiCasa room labels to standard types
    
    CubiCasa labels: living_room, bedroom, kitchen, bathroom, etc.
    Our standard: LIVING_ROOM, BEDROOM, KITCHEN, BATHROOM, etc.
    """
    room_map = {
        'living_room': 'LIVING_ROOM',
        'livingroom': 'LIVING_ROOM',
        'living': 'LIVING_ROOM',
        'bedroom': 'BEDROOM',
        'bed_room': 'BEDROOM',
        'kitchen': 'KITCHEN',
        'bathroom': 'BATHROOM',
        'bath': 'BATHROOM',
        'dining_room': 'DINING_ROOM',
        'diningroom': 'DINING_ROOM',
        'dining': 'DINING_ROOM',
        'office': 'HOME_OFFICE',
        'study': 'HOME_OFFICE',
        'closet': 'CLOSET',
        'storage': 'STORAGE',
        'laundry': 'LAUNDRY_ROOM',
        'corridor': 'CORRIDOR',
        'hallway': 'CORRIDOR',
        'balcony': 'BALCONY',
        'terrace': 'BALCONY'
    }
    
    normalized = room_label.lower().replace(' ', '_')
    return room_map.get(normalized, 'OTHER')
