# Interior Design AI Layout Planner - Spring Boot Backend

A REST API backend for an AI-powered interior design room layout planner. Uses OpenRouter's Llama 3.1 70B model to generate intelligent furniture placements, validated by a rule-based engine.

## 🏗️ Architecture

```
React Frontend (Port 3000)
          ↓
   REST API Calls
          ↓
Spring Boot Backend (Port 8080)
          ↓
    LayoutService
          ↓
    ┌─────────────┬─────────────┐
    ↓             ↓             ↓
FurnitureDAO   AIService   RuleEngine
    ↓             ↓             ↓
H2 Database  OpenRouter   Validation
              API
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- OpenRouter API Key ([Get one here](https://openrouter.ai/))

### Installation & Running

1. **Clone and navigate to backend:**
   ```bash
   cd backend
   ```

2. **Set OpenRouter API Key:**
   ```bash
   # Windows PowerShell
   $env:OPENROUTER_API_KEY="your_api_key_here"
   
   # Linux/Mac
   export OPENROUTER_API_KEY=your_api_key_here
   ```

3. **Build and run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Verify it's running:**
   ```bash
   curl http://localhost:8080/api/health
   ```

## 📡 API Endpoints

### 1. Health Check
```http
GET /api/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Interior Design API",
  "timestamp": 1728518400000,
  "aiServiceConfigured": true
}
```

### 2. Get Furniture Catalog
```http
GET /api/furniture
```

**Response:**
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
  ...
]
```

### 3. Generate Layout (Main Endpoint)
```http
POST /api/layout
Content-Type: application/json

{
  "length": 5.0,
  "width": 4.0,
  "budget": 2000
}
```

**Response:**
```json
{
  "furniture": [
    {
      "furniture": {
        "id": 1,
        "name": "Sofa",
        "width": 2.0,
        "depth": 0.9,
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
        "price": 200,
        "category": "coffee"
      },
      "x": 2.5,
      "y": 1.5
    }
  ],
  "totalCost": 1800,
  "warnings": [],
  "reasoning": "Sofa placed on longest wall for optimal viewing. Coffee table positioned for easy access from seating area."
}
```

**Validation Constraints:**
- `length`: 3-15 meters
- `width`: 3-15 meters
- `budget`: $500-$10,000

**Error Response (Validation):**
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

## 🧠 How It Works

### 1. Request Flow
```
User Request → LayoutRestController → LayoutService
                                          ↓
                                    FurnitureDAO.findAll()
                                          ↓
                                    AIService.getSuggestedLayout()
                                          ↓
                                    RuleEngine.generateLayout()
                                          ↓
                                    Return RoomLayout
```

### 2. AI Integration
- **AIPromptBuilder**: Creates detailed prompts with room specs and furniture catalog
- **AIService**: Calls OpenRouter API with retry logic and timeout handling
- **AIResponseParser**: Extracts and validates JSON from AI responses

### 3. Rule Engine Validation
The RuleEngine applies these rules to AI suggestions:

✅ **Boundary Check**: Furniture must fit within room dimensions  
✅ **Wall Clearance**: Minimum 0.5m from all walls  
✅ **Collision Detection**: No overlapping furniture (0.3m min gap)  
✅ **Budget Constraint**: Total cost ≤ specified budget  
✅ **Auto-Adjustment**: Attempts to fix invalid positions  

## 🗄️ Database Schema

### Furniture Table
```sql
CREATE TABLE furniture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    width DOUBLE NOT NULL,
    depth DOUBLE NOT NULL,
    price INT NOT NULL,
    category VARCHAR(50) NOT NULL
);
```

**Sample Data:** 25+ furniture items including sofas, tables, beds, chairs, etc.

## 📁 Project Structure

```
src/main/java/com/interiordesign/
├── InteriorDesignApplication.java     # Main entry point
├── config/
│   ├── CorsConfig.java                # CORS configuration
│   └── WebClientConfig.java           # WebClient bean
├── controller/
│   ├── LayoutRestController.java      # REST endpoints
│   └── GlobalExceptionHandler.java    # Error handling
├── dao/
│   └── FurnitureDAO.java              # Database access
├── model/
│   ├── Furniture.java                 # Furniture entity
│   ├── Room.java                      # Room input DTO
│   ├── FurniturePosition.java         # Position in layout
│   └── RoomLayout.java                # Layout response DTO
├── service/
│   ├── LayoutService.java             # Main orchestrator
│   └── RuleEngine.java                # Validation logic
└── ai/
    ├── AIService.java                 # OpenRouter integration
    ├── AIPromptBuilder.java           # Prompt generation
    ├── AIResponseParser.java          # Response parsing
    ├── exception/
    │   └── AIServiceException.java    # Custom exception
    └── model/
        ├── AILayoutRequest.java       # AI request DTO
        └── AILayoutResponse.java      # AI response DTO

src/main/resources/
├── application.properties             # Configuration
├── schema.sql                         # Database schema
└── data.sql                           # Sample data
```

## ⚙️ Configuration

### Application Properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:mem:interiordb

# AI Configuration
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.api.key=${OPENROUTER_API_KEY}
openrouter.model=meta-llama/llama-3.1-70b-instruct:free

# Timeouts & Retries
ai.timeout.seconds=30
ai.max-retries=3

# CORS
cors.allowed.origins=http://localhost:3000
```

### Environment Variables
```bash
# Required
OPENROUTER_API_KEY=your_api_key_here

# Optional
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## 🧪 Testing

### Manual Testing with cURL

**Test Health:**
```bash
curl http://localhost:8080/api/health
```

**Get Furniture:**
```bash
curl http://localhost:8080/api/furniture
```

**Generate Layout:**
```bash
curl -X POST http://localhost:8080/api/layout \
  -H "Content-Type: application/json" \
  -d '{
    "length": 5.0,
    "width": 4.0,
    "budget": 2000
  }'
```

### Test Scenarios

**Small Room, Low Budget:**
```json
{ "length": 4.0, "width": 3.0, "budget": 800 }
```

**Medium Room, Medium Budget:**
```json
{ "length": 8.0, "width": 6.0, "budget": 3000 }
```

**Large Room, High Budget:**
```json
{ "length": 12.0, "width": 10.0, "budget": 8000 }
```

**Invalid Input (should fail):**
```json
{ "length": 2.0, "width": 20.0, "budget": 100 }
```

## 🐛 Troubleshooting

### Issue: "OpenRouter API Key Not Configured"
**Solution:** Set the environment variable:
```bash
export OPENROUTER_API_KEY=your_key_here
```

### Issue: "Failed to call AI service"
**Possible Causes:**
1. Invalid API key
2. Network connectivity issues
3. OpenRouter API down
4. Request timeout

**Check logs for details:**
```bash
tail -f logs/application.log
```

### Issue: H2 Database Not Initializing
**Solution:** Check `application.properties`:
```properties
spring.sql.init.mode=always
```

### Issue: CORS Errors from Frontend
**Solution:** Verify CORS configuration in `application.properties`:
```properties
cors.allowed.origins=http://localhost:3000
```

## 📊 Logging

Application logs include:
- Request/response details
- AI API calls and responses (sanitized)
- Rule engine validation steps
- Error stack traces

**Log Levels:**
- `INFO`: General application flow
- `DEBUG`: Detailed debugging information
- `WARN`: Warnings (e.g., budget exceeded)
- `ERROR`: Errors and exceptions

## 🔒 Security Considerations

✅ **API Key Protection**: Never exposed in responses  
✅ **Input Validation**: All inputs validated with constraints  
✅ **CORS Configuration**: Only allows specified origins  
✅ **Error Handling**: Sanitized error messages  
✅ **SQL Injection**: Protected by JDBC template  

## 🚀 Performance

- **Average Response Time**: 2-5 seconds (depends on AI response)
- **AI Timeout**: 30 seconds (configurable)
- **Retry Logic**: Up to 3 retries with 2-second delay
- **Database**: In-memory H2 (fast, no disk I/O)

## 📈 Future Enhancements

- [ ] Add furniture rotation support
- [ ] Implement caching for AI responses
- [ ] Add user authentication
- [ ] Save/load layouts
- [ ] Multiple room types (bedroom, kitchen, office)
- [ ] 3D visualization data
- [ ] Custom furniture catalog management
- [ ] Layout rating/feedback system

## 📄 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Built with ❤️ using Spring Boot, OpenRouter AI, and Java 17**
