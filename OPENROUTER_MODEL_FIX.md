# OpenRouter API 404 Error Fix

## Problem
The error occurs because the model `deepseek/deepseek-chat-v3.1:free` is not available or doesn't match your OpenRouter data policy settings.

Error message:
```
"No endpoints found matching your data policy (Free model publication)"
```

## Solution

### Option 1: Use a Different Free Model (Recommended)
I've updated `application.properties` to use `meta-llama/llama-3.2-3b-instruct:free`

**Other working free models you can try:**
```properties
# Llama 3.2 (Fast, good for structured output)
openrouter.model=meta-llama/llama-3.2-3b-instruct:free

# Llama 3.1 8B (More capable)
openrouter.model=meta-llama/llama-3.1-8b-instruct:free

# Google Gemma 2 (Google's model)
openrouter.model=google/gemma-2-9b-it:free

# Mistral 7B
openrouter.model=mistralai/mistral-7b-instruct:free

# Qwen 2.5 7B (Alibaba's model, good at structured tasks)
openrouter.model=qwen/qwen-2.5-7b-instruct:free
```

### Option 2: Configure OpenRouter Privacy Settings
1. Go to https://openrouter.ai/settings/privacy
2. Enable "Free model publication" if you want to use free models
3. Keep the current model in application.properties

### Option 3: Use a Paid Model (Best Quality)
If you have credits, use a more capable model:
```properties
# GPT-4o Mini (OpenAI - best quality/price)
openrouter.model=openai/gpt-4o-mini

# Claude 3.5 Haiku (Anthropic - fast and cheap)
openrouter.model=anthropic/claude-3.5-haiku

# Gemini Flash (Google - very fast)
openrouter.model=google/gemini-flash-1.5
```

## Steps to Apply Fix

### Quick Fix (Already Applied)
1. The model has been changed to `meta-llama/llama-3.2-3b-instruct:free` in `application.properties`
2. Restart your backend server:
   ```powershell
   cd backend
   mvn spring-boot:run
   ```

### To Test Different Models
Edit `backend/src/main/resources/application.properties` line 36:
```properties
openrouter.model=<model-name>:free
```

## Verify the Fix

1. **Start backend** (if not running):
   ```powershell
   cd backend
   mvn spring-boot:run
   ```

2. **Test the API** in your browser at http://localhost:3000

3. **Check backend logs** - you should see:
   ```
   Successfully generated AI layout with X furniture items
   ```
   Instead of:
   ```
   OpenRouter API error: 404 NOT_FOUND
   ```

## Additional Notes

- **API Key**: Your API key is hardcoded in `application.properties`. For security, consider using environment variables:
  ```powershell
  $env:OPENROUTER_API_KEY="sk-or-v1-your-key-here"
  ```

- **Model Availability**: Free models can change. Check available models at:
  https://openrouter.ai/docs#models

- **Rate Limits**: Free models have rate limits. If you hit them, wait a minute or upgrade to a paid plan.

## Current Configuration

File: `backend/src/main/resources/application.properties`
```properties
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.api.key=${OPENROUTER_API_KEY:sk-or-v1-...}
openrouter.model=meta-llama/llama-3.2-3b-instruct:free
```
