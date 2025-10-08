# Environment Configuration

## Required Environment Variables

### OPENROUTER_API_KEY
Your OpenRouter API key for AI-powered layout generation.

**Get your key:** https://openrouter.ai/

**Set the variable:**

**Windows PowerShell:**
```powershell
$env:OPENROUTER_API_KEY="sk-or-v1-..."
```

**Windows Command Prompt:**
```cmd
set OPENROUTER_API_KEY=sk-or-v1-...
```

**Linux/Mac:**
```bash
export OPENROUTER_API_KEY=sk-or-v1-...
```

**Permanent (Linux/Mac) - Add to ~/.bashrc or ~/.zshrc:**
```bash
export OPENROUTER_API_KEY=sk-or-v1-...
```

## Optional Environment Variables

### SERVER_PORT
Change the server port (default: 8080)
```bash
export SERVER_PORT=9090
```

### CORS_ALLOWED_ORIGINS
Change allowed CORS origins (default: http://localhost:3000)
```bash
export CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:3001
```

### AI_TIMEOUT_SECONDS
Change AI request timeout (default: 30)
```bash
export AI_TIMEOUT_SECONDS=60
```

### AI_MAX_RETRIES
Change maximum retry attempts (default: 3)
```bash
export AI_MAX_RETRIES=5
```

## IDE Configuration

### IntelliJ IDEA
1. Open **Run** → **Edit Configurations**
2. Select your Spring Boot configuration
3. Under **Environment Variables**, click the folder icon
4. Add: `OPENROUTER_API_KEY=your_key_here`
5. Click **OK** and run

### VS Code
1. Create `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot",
      "request": "launch",
      "mainClass": "com.interiordesign.InteriorDesignApplication",
      "env": {
        "OPENROUTER_API_KEY": "your_key_here"
      }
    }
  ]
}
```

### Eclipse
1. Right-click project → **Run As** → **Run Configurations**
2. Select **Spring Boot App**
3. Go to **Environment** tab
4. Click **New**
5. Add: `OPENROUTER_API_KEY` = `your_key_here`

## Docker Configuration

If running in Docker:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
ENV OPENROUTER_API_KEY=your_key_here
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Or use docker-compose:

```yaml
version: '3.8'
services:
  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - OPENROUTER_API_KEY=your_key_here
      - CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## Production Deployment

### Using .env file (not recommended for production)
Create `.env` file in project root:
```
OPENROUTER_API_KEY=your_key_here
```

Then source it before running:
```bash
source .env
mvn spring-boot:run
```

### Using application-prod.properties
Create `src/main/resources/application-prod.properties`:
```properties
openrouter.api.key=${OPENROUTER_API_KEY}
# Other production settings...
```

Run with profile:
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

## Security Best Practices

❌ **Never commit API keys to version control**  
❌ **Never hardcode keys in source files**  
✅ **Use environment variables**  
✅ **Use secrets management in production (AWS Secrets Manager, Azure Key Vault, etc.)**  
✅ **Rotate keys regularly**  
✅ **Use different keys for dev/staging/production**  

## Troubleshooting

### "API key not configured" warning
Check if variable is set:
```bash
echo $OPENROUTER_API_KEY
```

Should output your key (not empty).

### Variable not persisting
Add to shell profile:
```bash
# ~/.bashrc or ~/.zshrc
export OPENROUTER_API_KEY=your_key_here
```

Then reload:
```bash
source ~/.bashrc
```

### Maven not seeing environment variable
Try running Maven with explicit env:
```bash
OPENROUTER_API_KEY=your_key mvn spring-boot:run
```
