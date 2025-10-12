# CubiCasa Floor Plan Parser Service

Python Flask microservice for parsing floor plan images using CubiCasa5k model.

## Features

- Auto-downloads CubiCasa model on first startup
- Validates uploaded images are floor plans (not photos)
- Extracts walls, doors, windows, and room boundaries
- Returns structured JSON for Java backend integration

## Setup

### 1. Install Python Dependencies

```powershell
cd python-service
pip install -r requirements.txt
```

### 2. Install CubiCasa5k Model

**IMPORTANT:** CubiCasa5k doesn't provide pre-trained weights out of the box. You need to:

#### Option A: Use Pre-trained Weights (if available)
```powershell
# Create models directory
mkdir models

# Download weights (REPLACE WITH ACTUAL URL)
# curl -o models/cubicasa_model.pth https://your-weights-url.com/model.pth
```

#### Option B: Train Your Own Model
```powershell
# Clone CubiCasa5k repository
git clone https://github.com/CubiCasa/CubiCasa5k.git

# Follow training instructions in their repo
# Copy trained weights to python-service/models/
```

#### Option C: Use Simplified Mock (For Testing)
The service includes a mock implementation that returns sample data. This allows you to test the integration without the full model.

### 3. Run Service

```powershell
python app.py
```

Service runs on `http://localhost:5000`

## API Endpoints

### Health Check
```
GET /health
```

### Parse Floor Plan
```
POST /api/parse-floorplan
Content-Type: multipart/form-data

{
  "file": <floor_plan_image.png>
}
```

**Response:**
```json
{
  "success": true,
  "filename": "floorplan.png",
  "validation_confidence": 0.85,
  "data": {
    "walls": [
      {"startX": 0, "startY": 0, "endX": 500, "endY": 0, "thickness": 8}
    ],
    "doors": [
      {"x": 250, "y": 0, "width": 80, "rotation": 90}
    ],
    "windows": [
      {"x": 100, "y": 0, "width": 120}
    ],
    "rooms": [
      {
        "type": "LIVING_ROOM",
        "bounds": {"x": 0, "y": 0, "width": 500, "height": 400}
      }
    ]
  }
}
```

### Model Status
```
GET /api/model-status
```

## Integration with Spring Boot

The Java backend will call this service:

```java
String pythonServiceUrl = "http://localhost:5000/api/parse-floorplan";
// POST file to endpoint
// Parse JSON response
// Store in database
```

## Notes

- Max file size: 10MB
- Supported formats: PNG, JPG, JPEG
- Processing time: 5-10 seconds per image
- Model memory usage: ~2GB RAM

## Troubleshooting

### Import errors
Make sure all dependencies are installed:
```powershell
pip install -r requirements.txt
```

### Model not loading
Check that weights file exists in `models/cubicasa_model.pth`

### Out of memory
Reduce batch size or use CPU instead of GPU

## TODO

- [ ] Implement actual CubiCasa model architecture
- [ ] Add model caching for faster startup
- [ ] Add batch processing for multiple images
- [ ] Implement result caching (same image = cached result)
- [ ] Add monitoring/metrics endpoints
