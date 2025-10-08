# DeepSeek Model Configuration Fix

## Problem
The model `deepseek/deepseek-chat-v3.1:free` returns a 404 error:
```
"No endpoints found matching your data policy (Free model publication)"
```

## Possible Causes & Solutions

### 1. Incorrect Model Name Format
The DeepSeek model might not be available with the `:free` suffix or the version number might be wrong.

**Try these model name variations:**

```properties
# Option 1: Without version suffix (CURRENTLY TRYING THIS)
openrouter.model=deepseek/deepseek-chat

# Option 2: Without :free suffix
openrouter.model=deepseek/deepseek-chat-v3

# Option 3: Different version
openrouter.model=deepseek/deepseek-chat-v3.1

# Option 4: Older stable version
openrouter.model=deepseek/deepseek-coder

# Option 5: R1 model (reasoning model)
openrouter.model=deepseek/deepseek-r1
```

### 2. Privacy Settings Issue
OpenRouter requires you to allow "Free model publication" for free models.

**Steps to fix:**
1. Visit: https://openrouter.ai/settings/privacy
2. Enable **"Allow free model publication"** or similar option
3. This allows your requests to be used for training (hence "free")
4. Restart your backend

### 3. API Key Permissions
Your API key might not have access to free models.

**Steps to verify:**
1. Visit: https://openrouter.ai/keys
2. Check your API key settings
3. Ensure it has access to free tier models
4. Generate a new key if needed

### 4. Check Model Availability
The model might have been removed or renamed.

**Steps to check:**
1. Visit: https://openrouter.ai/models
2. Search for "deepseek"
3. Look for available DeepSeek models
4. Check the exact model ID and copy it

## Testing Different Model Names

I've updated the configuration to try `deepseek/deepseek-chat` first. 

**To test other variations:**

1. Stop your backend (Ctrl+C in the terminal)

2. Edit `backend/src/main/resources/application.properties` line 36:
   ```properties
   openrouter.model=<model-name-from-above>
   ```

3. Restart backend:
   ```powershell
   cd backend
   mvn spring-boot:run
   ```

4. Test the API from your frontend

5. Check logs for success or try the next model name

## Current Configuration Attempts

### Attempt 1 (Now trying):
```properties
openrouter.model=deepseek/deepseek-chat
```

If this doesn't work, try the other options listed above one by one.

## Recommended: Verify on OpenRouter Website

The most reliable way to get the correct model name:

1. Go to https://openrouter.ai/models
2. Find DeepSeek in the list
3. Click on it to see details
4. Look for the **"Model ID"** or **"API Name"**
5. Copy the exact string (e.g., `deepseek/deepseek-chat`)
6. Paste it in `application.properties`

## Alternative: Contact OpenRouter Support

If DeepSeek free models are no longer available:
- Check OpenRouter Discord: https://discord.gg/openrouter
- Check their documentation: https://openrouter.ai/docs
- They may have deprecated the free tier for DeepSeek

## Fallback Option

If DeepSeek free models are unavailable, use these excellent free alternatives:

```properties
# Meta Llama 3.2 (Fast, good structured output)
openrouter.model=meta-llama/llama-3.2-3b-instruct:free

# Qwen 2.5 (Alibaba, excellent at structured tasks)
openrouter.model=qwen/qwen-2.5-7b-instruct:free

# Google Gemma 2
openrouter.model=google/gemma-2-9b-it:free
```

## Next Steps

1. Restart your backend to test the new configuration
2. If it fails, check the error message in logs
3. Try the model name variations above
4. Verify on OpenRouter website for the correct model ID
5. Check your privacy settings if you haven't already
