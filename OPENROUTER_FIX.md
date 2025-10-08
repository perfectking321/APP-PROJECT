# OpenRouter API 404 Error - Fix Applied

## Problem
The OpenRouter API was returning a 404 error with the message:
```
OpenRouter API error: 404 Not Found from POST https://openrouter.ai/api/v1/chat/completions
```

## Root Causes
1. **Invalid model name**: `deepseek/deepseek-chat-v3.1:free` doesn't exist or isn't available
2. **Missing required headers**: OpenRouter requires `HTTP-Referer` and `X-Title` headers

## Changes Made

### 1. Updated `application.properties`
Changed the model to a valid free model and added required configuration:
```properties
openrouter.model=google/gemini-2.0-flash-exp:free
openrouter.app.name=Interior Design App
openrouter.site.url=http://localhost:3000
```

### 2. Updated `AIService.java`
- Added configuration fields for app name and site URL
- Added required HTTP headers in the API call:
  - `HTTP-Referer`: Your site URL
  - `X-Title`: Your app name
- Improved error message to show the full error details

## How to Test

1. **Rebuild the backend:**
   ```powershell
   cd backend
   mvn clean package -DskipTests
   ```

2. **Restart the backend server:**
   ```powershell
   java -jar target/interior-design-api-1.0.0.jar
   ```

3. **Test from the frontend** (should already be running on localhost:3000)

## Alternative Free Models
If `google/gemini-2.0-flash-exp:free` doesn't work, try these in `application.properties`:

```properties
# Option 1: Gemini Flash (Recommended)
openrouter.model=google/gemini-2.0-flash-exp:free

# Option 2: Meta Llama
openrouter.model=meta-llama/llama-3.2-3b-instruct:free

# Option 3: Mistral
openrouter.model=mistralai/mistral-7b-instruct:free

# Option 4: Qwen
openrouter.model=qwen/qwen-2-7b-instruct:free
```

## Verify API Key
Make sure your API key is valid:
1. Go to https://openrouter.ai/
2. Sign in and check your API keys
3. Update the key in `application.properties` or set the `OPENROUTER_API_KEY` environment variable

## Next Steps
1. Rebuild and restart the backend
2. Try generating a layout from the frontend
3. Check the backend console logs for any errors
4. If you still get errors, check the backend logs for the full error message
