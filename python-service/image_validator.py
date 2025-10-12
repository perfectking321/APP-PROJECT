import logging
from PIL import Image
import numpy as np

logger = logging.getLogger(__name__)

def validate_floor_plan_image(image_path):
    """
    Validates that uploaded image is likely a floor plan
    
    Checks:
    1. Image is not too small/large
    2. Has sufficient edges/lines (architectural drawings have many lines)
    3. Not a photograph (low color variance)
    4. Has reasonable aspect ratio
    
    Args:
        image_path: Path to uploaded image
        
    Returns:
        dict: {'is_valid': bool, 'confidence': float, 'reason': str}
    """
    try:
        img = Image.open(image_path)
        width, height = img.size
        
        # Convert to numpy array
        img_array = np.array(img.convert('RGB'))
        
        # Check 1: Size validation
        if width < 200 or height < 200:
            return {
                'is_valid': False,
                'confidence': 0.0,
                'reason': 'Image too small. Minimum 200x200 pixels required.'
            }
        
        if width > 4000 or height > 4000:
            return {
                'is_valid': False,
                'confidence': 0.0,
                'reason': 'Image too large. Maximum 4000x4000 pixels allowed.'
            }
        
        # Check 2: Aspect ratio (floor plans usually not extremely elongated)
        aspect_ratio = max(width, height) / min(width, height)
        if aspect_ratio > 5:
            return {
                'is_valid': False,
                'confidence': 0.3,
                'reason': 'Unusual aspect ratio for floor plan (too elongated).'
            }
        
        # Check 3: Color analysis (floor plans tend to be less colorful than photos)
        grayscale = np.array(img.convert('L'))
        color_std = np.std(img_array)
        gray_std = np.std(grayscale)
        
        # Floor plans typically have lower color variance
        is_grayscale_like = color_std < 50 or gray_std > 40
        
        # Check 4: Edge detection (floor plans have many straight lines)
        try:
            import cv2
            
            # Convert to grayscale for edge detection
            gray = cv2.cvtColor(img_array, cv2.COLOR_RGB2GRAY)
            
            # Apply Canny edge detection
            edges = cv2.Canny(gray, 50, 150)
            edge_percentage = (np.count_nonzero(edges) / edges.size) * 100
            
            # Floor plans typically have 5-30% edge pixels
            has_sufficient_edges = 5 <= edge_percentage <= 50
            
            logger.info(f"Image validation: edges={edge_percentage:.2f}%, "
                       f"color_std={color_std:.2f}, aspect={aspect_ratio:.2f}")
            
            # Calculate confidence score
            confidence = 0.0
            
            if has_sufficient_edges:
                confidence += 0.5
            
            if is_grayscale_like:
                confidence += 0.3
            
            if aspect_ratio < 3:
                confidence += 0.2
            
            # Require at least 60% confidence
            if confidence >= 0.6:
                return {
                    'is_valid': True,
                    'confidence': confidence,
                    'reason': 'Image appears to be a valid floor plan'
                }
            else:
                return {
                    'is_valid': False,
                    'confidence': confidence,
                    'reason': f'Image does not appear to be a floor plan (confidence: {confidence:.1%}). '
                             f'Expected architectural drawing with clear lines and minimal colors.'
                }
        
        except ImportError:
            logger.warning("OpenCV not available, using basic validation only")
            
            # Fallback validation without edge detection
            if is_grayscale_like and aspect_ratio < 3:
                return {
                    'is_valid': True,
                    'confidence': 0.6,
                    'reason': 'Basic validation passed (OpenCV not available for detailed check)'
                }
            else:
                return {
                    'is_valid': False,
                    'confidence': 0.3,
                    'reason': 'Image validation failed (install opencv-python for better validation)'
                }
    
    except Exception as e:
        logger.error(f"Image validation error: {str(e)}")
        return {
            'is_valid': False,
            'confidence': 0.0,
            'reason': f'Validation error: {str(e)}'
        }
