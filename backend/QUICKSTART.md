# 🚀 Quick Start Guide - Spring Boot Backend

## Step 1: Prerequisites Check

Verify you have:
```bash
# Check Java version (need 17+)
java -version

# Check Maven version (need 3.6+)
mvn -version
```

If missing, install:
- **Java 17**: https://adoptium.net/
- **Maven**: https://maven.apache.org/download.cgi

---

## Step 2: Get OpenRouter API Key

1. Go to https://openrouter.ai/
2. Sign up or log in
3. Go to **Keys** section
4. Create a new API key
5. Copy the key (starts with `sk-or-v1-...`)

---

## Step 3: Set Environment Variable

### Windows PowerShell:
```powershell
$env:OPENROUTER_API_KEY="your_api_key_here"
```

### Windows Command Prompt:
```cmd
set OPENROUTER_API_KEY=your_api_key_here
```

### Linux/Mac:
```bash
export OPENROUTER_API_KEY=your_api_key_here
```

**Verify it's set:**
```bash
# Windows PowerShell
echo $env:OPENROUTER_API_KEY

# Linux/Mac
echo $OPENROUTER_API_KEY
```

---

## Step 4: Build & Run

Navigate to backend directory:
```bash
cd backend
```

**Option A: Using Maven**
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

**Option B: Using JAR**
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/interior-design-api-1.0.0.jar
```

---

## Step 5: Verify It's Running

You should see:
```
Application 'Interior Design API' is running!
Access URLs:
  Local:      http://localhost:8080/
  H2 Console: http://localhost:8080/h2-console
API Endpoints:
  Health:     http://localhost:8080/api/health
  Furniture:  http://localhost:8080/api/furniture
  Layout:     http://localhost:8080/api/layout (POST)
✓ OpenRouter API Key configured
```

**Test the API:**
```bash
curl http://localhost:8080/api/health
```

Should return:
```json
{
  "status": "UP",
  "service": "Interior Design API",
  "timestamp": 1728518400000,
  "aiServiceConfigured": true
}
```

---

## Step 6: Test API Endpoints

### Get Furniture Catalog
```bash
curl http://localhost:8080/api/furniture
```

### Generate Layout
```bash
curl -X POST http://localhost:8080/api/layout \
  -H "Content-Type: application/json" \
  -d '{"length":5.0,"width":4.0,"budget":2000}'
```

---

## Common Issues & Solutions

### ❌ "Port 8080 already in use"
**Solution 1:** Kill process using port 8080
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**Solution 2:** Change port
```bash
mvn spring-boot:run -Dserver.port=9090
```

### ❌ "OpenRouter API Key Not Configured"
**Solution:** Set environment variable (see Step 3)

### ❌ "Failed to execute goal... compilation failure"
**Solution:** Check Java version
```bash
java -version
# Should be 17 or higher
```

### ❌ "Could not resolve dependencies"
**Solution:** Update Maven
```bash
mvn clean install -U
```

---

## Next Steps

1. ✅ Keep backend running
2. ✅ Start React frontend (see frontend README)
3. ✅ Test full integration
4. ✅ Generate some layouts!

---

## Stopping the Application

**If running with Maven:**
- Press `Ctrl + C`

**If running as JAR:**
- Press `Ctrl + C`
- Or: `kill <PID>`

---

## H2 Database Console (Optional)

Access the database console at: http://localhost:8080/h2-console

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:interiordb`
- Username: `sa`
- Password: (leave empty)

---

## Logs Location

Logs are printed to console by default.

To enable file logging, add to `application.properties`:
```properties
logging.file.name=logs/application.log
```

---

## Production Deployment

For production, create `application-prod.properties`:
```properties
server.port=8080
logging.level.root=WARN
logging.level.com.interiordesign=INFO
```

Run with production profile:
```bash
java -jar target/interior-design-api-1.0.0.jar --spring.profiles.active=prod
```

---

## Docker Deployment (Optional)

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t interior-design-api .
docker run -p 8080:8080 -e OPENROUTER_API_KEY=your_key interior-design-api
```

---

## Need Help?

1. Check logs for detailed error messages
2. Review `README.md` for comprehensive documentation
3. Check `API_DOCUMENTATION.md` for API details
4. Review `ENVIRONMENT_SETUP.md` for configuration help

---

**Ready to go! 🎉**
