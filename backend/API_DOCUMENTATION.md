# API Documentation

Complete REST API reference for the Interior Design Layout Planner backend.

## Base URL
```
http://localhost:8080/api
```

---

## Endpoints

### 1. Health Check

Check if the API is running and configured properly.

**Endpoint:** `GET /api/health`

**Request:**
```http
GET /api/health HTTP/1.1
Host: localhost:8080
```

**Response:** `200 OK`
```json
{
  "status": "UP",
  "service": "Interior Design API",
  "timestamp": 1728518400000,
  "aiServiceConfigured": true
}
```

**Fields:**
- `status`: Service status (UP/DOWN)
- `service`: Service name
- `timestamp`: Current server timestamp
- `aiServiceConfigured`: Whether OpenRouter API key is configured

---

### 2. Get Furniture Catalog

Retrieve all available furniture items from the database.

**Endpoint:** `GET /api/furniture`

**Request:**
```http
GET /api/furniture HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Sofa",
    "width": 2.0,
    "depth": 0.9,
    "length": 0.9,
    "price": 800,
    "category": "sofa"
  },
  {
    "id": 2,
    "name": "Coffee Table",
    "width": 1.2,
    "depth": 0.6,
    "length": 0.6,
    "price": 200,
    "category": "coffee"
  }
]
```

**Fields:**
- `id`: Unique furniture identifier
- `name`: Furniture name
- `width`: Width in meters
- `depth`: Depth in meters
- `length`: Same as depth (for frontend compatibility)
- `price`: Price in dollars
- `category`: Furniture category

---

### 3. Generate Room Layout

Generate an AI-powered furniture layout for a room.

**Endpoint:** `POST /api/layout`

**Request:**
```http
POST /api/layout HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
  "length": 5.0,
  "width": 4.0,
  "budget": 2000
}
```

**Request Body:**
| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| length | number | Yes | 3.0 - 15.0 | Room length in meters |
| width | number | Yes | 3.0 - 15.0 | Room width in meters |
| budget | integer | Yes | 500 - 10000 | Budget in dollars |

**Response:** `200 OK`
```json
{
  "furniture": [
    {
      "furniture": {
        "id": 1,
        "name": "Sofa",
        "width": 2.0,
        "depth": 0.9,
        "length": 0.9,
        "price": 800,
        "category": "sofa"
      },
      "x": 1.0,
      "y": 2.5
    },
    {
      "furniture": {
        "id": 2,
        "name": "Coffee Table",
        "width": 1.2,
        "depth": 0.6,
        "length": 0.6,
        "price": 200,
        "category": "coffee"
      },
      "x": 2.5,
      "y": 1.5
    }
  ],
  "totalCost": 1800,
  "warnings": [],
  "reasoning": "Sofa placed on longest wall for optimal viewing. Coffee table positioned for easy access."
}
```

**Response Fields:**
- `furniture`: Array of furniture placements
  - `furniture`: Furniture object with details
  - `x`: X coordinate in meters (from left)
  - `y`: Y coordinate in meters (from bottom)
- `totalCost`: Total cost of all placed furniture
- `warnings`: Array of warning messages
- `reasoning`: AI explanation of layout decisions

---

## Error Responses

### Validation Error (400 Bad Request)

Returned when input parameters don't meet validation constraints.

```json
{
  "timestamp": "2025-10-09T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input parameters",
  "errors": {
    "length": "Length must be at least 3 meters",
    "budget": "Budget must be at least $500"
  }
}
```

**Common Validation Errors:**
- Length too small: `"Length must be at least 3 meters"`
- Length too large: `"Length must not exceed 15 meters"`
- Width too small: `"Width must be at least 3 meters"`
- Width too large: `"Width must not exceed 15 meters"`
- Budget too small: `"Budget must be at least $500"`
- Budget too large: `"Budget must not exceed $10,000"`

### AI Service Error (503 Service Unavailable)

Returned when AI service fails (API key issues, timeout, etc.)

```json
{
  "timestamp": "2025-10-09T12:00:00",
  "status": 503,
  "error": "AI Service Error",
  "message": "OpenRouter API key not configured. Set OPENROUTER_API_KEY environment variable."
}
```

**Common AI Errors:**
- API key not configured
- API request timeout
- OpenRouter API error
- Invalid AI response

### Server Error (500 Internal Server Error)

Returned for unexpected server errors.

```json
{
  "timestamp": "2025-10-09T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred: ..."
}
```

---

## Example Requests

### JavaScript (Fetch)

```javascript
// Generate layout
const response = await fetch('http://localhost:8080/api/layout', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    length: 5.0,
    width: 4.0,
    budget: 2000
  })
});

const layout = await response.json();
console.log(layout);
```

### JavaScript (Axios)

```javascript
import axios from 'axios';

const layout = await axios.post('http://localhost:8080/api/layout', {
  length: 5.0,
  width: 4.0,
  budget: 2000
});

console.log(layout.data);
```

### cURL

```bash
curl -X POST http://localhost:8080/api/layout \
  -H "Content-Type: application/json" \
  -d '{
    "length": 5.0,
    "width": 4.0,
    "budget": 2000
  }'
```

### Python (requests)

```python
import requests

response = requests.post('http://localhost:8080/api/layout', json={
    'length': 5.0,
    'width': 4.0,
    'budget': 2000
})

layout = response.json()
print(layout)
```

### Java (RestTemplate)

```java
RestTemplate restTemplate = new RestTemplate();
Room room = new Room(5.0, 4.0, 2000);

RoomLayout layout = restTemplate.postForObject(
    "http://localhost:8080/api/layout",
    room,
    RoomLayout.class
);
```

---

## Rate Limiting

Currently no rate limiting is implemented. Future versions may include:
- Rate limiting per IP address
- API key-based rate limiting
- Burst protection

---

## CORS

**Allowed Origins:** `http://localhost:3000` (configurable)  
**Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS  
**Allowed Headers:** All  
**Credentials:** Enabled  

To change allowed origins, update `application.properties`:
```properties
cors.allowed.origins=http://localhost:3000,http://localhost:3001
```

---

## Response Times

**Typical Response Times:**
- `GET /api/health`: < 50ms
- `GET /api/furniture`: < 100ms
- `POST /api/layout`: 2-5 seconds (AI processing)

**Timeouts:**
- AI request timeout: 30 seconds (configurable)
- Overall request timeout: 60 seconds

---

## Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| 200 | OK | Successful request |
| 400 | Bad Request | Validation errors |
| 500 | Internal Server Error | Unexpected server error |
| 503 | Service Unavailable | AI service failure |

---

## Versioning

Current version: **v1**  
API versioning strategy: URL-based (future: `/api/v2/...`)

---

## Support

For issues or questions:
- Check logs: Application logs contain detailed error information
- Review README: Troubleshooting section
- GitHub Issues: Report bugs or request features
