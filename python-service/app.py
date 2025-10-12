import os
import logging
from flask import Flask, request, jsonify
from flask_cors import CORS
from werkzeug.utils import secure_filename
from model_loader import CubiCasaModelLoader
from image_validator import validate_floor_plan_image
from svg_parser import parse_cubicasa_svg

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

# Configuration
UPLOAD_FOLDER = '../uploads/floorplans'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10MB

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = MAX_FILE_SIZE

# Create upload directory
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# Initialize model loader (will auto-download on first startup)
model_loader = CubiCasaModelLoader()

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/', methods=['GET'])
def index():
    """Root endpoint with service information"""
    return jsonify({
        'service': 'CubiCasa Floor Plan Parser Service',
        'status': 'running',
        'version': '1.0.0',
        'endpoints': {
            'health': '/health',
            'parse_floorplan': '/api/parse-floorplan (POST)',
            'model_status': '/api/model-status'
        },
        'note': 'This is a backend service. Access the frontend application to use the interior design tool.'
    })

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'model_loaded': model_loader.is_model_loaded(),
        'service': 'CubiCasa Floor Plan Parser'
    })

@app.route('/api/parse-floorplan', methods=['POST'])
def parse_floorplan():
    """
    Parse uploaded floor plan image using CubiCasa5k model
    
    Returns:
        JSON with walls, doors, windows, rooms data
    """
    try:
        # Check if file is present
        if 'file' not in request.files:
            return jsonify({'error': 'No file provided'}), 400
        
        file = request.files['file']
        
        if not file.filename or file.filename == '':
            return jsonify({'error': 'No file selected'}), 400
        
        if not allowed_file(file.filename):
            return jsonify({'error': 'Invalid file type. Only PNG, JPG, JPEG allowed'}), 400
        
        # Save uploaded file
        filename = secure_filename(file.filename)
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(filepath)
        
        logger.info(f"Received floor plan: {filename}")
        
        # Step 1: Validate that image is a floor plan
        validation_result = validate_floor_plan_image(filepath)
        if not validation_result['is_valid']:
            os.remove(filepath)  # Clean up
            return jsonify({
                'error': 'Invalid floor plan image',
                'reason': validation_result['reason']
            }), 400
        
        logger.info(f"Image validation passed: {validation_result['confidence']:.2%} confidence")
        
        # Step 2: Load model if not already loaded
        if not model_loader.is_model_loaded():
            logger.info("Loading CubiCasa model...")
            model_loader.load_model()
        
        # Step 3: Process image with CubiCasa model
        logger.info("Processing floor plan with CubiCasa model...")
        svg_output = model_loader.process_image(filepath)
        
        # Step 4: Parse SVG to extract structured data
        parsed_data = parse_cubicasa_svg(svg_output)
        
        logger.info(f"Successfully parsed floor plan: {len(parsed_data['walls'])} walls, "
                   f"{len(parsed_data['doors'])} doors, {len(parsed_data['windows'])} windows, "
                   f"{len(parsed_data['rooms'])} rooms")
        
        # Return structured data
        return jsonify({
            'success': True,
            'filename': filename,
            'validation_confidence': validation_result['confidence'],
            'data': parsed_data
        }), 200
        
    except Exception as e:
        logger.error(f"Error processing floor plan: {str(e)}", exc_info=True)
        return jsonify({'error': f'Processing failed: {str(e)}'}), 500

@app.route('/api/model-status', methods=['GET'])
def model_status():
    """Get model loading status"""
    return jsonify({
        'loaded': model_loader.is_model_loaded(),
        'download_progress': model_loader.get_download_progress(),
        'model_path': model_loader.get_model_path()
    })

if __name__ == '__main__':
    logger.info("Starting CubiCasa Floor Plan Parser Service...")
    logger.info(f"Upload folder: {os.path.abspath(UPLOAD_FOLDER)}")
    
    # Check model availability
    if not model_loader.is_model_loaded():
        logger.warning("Model not loaded. Will download on first request.")
    
    app.run(host='0.0.0.0', port=5000, debug=True)
