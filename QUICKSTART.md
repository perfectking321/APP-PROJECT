# 🚀 Quick Start Guide

## Step-by-Step Setup

### 1. Install Dependencies

Open PowerShell in the project directory and run:

```powershell
npm install
```

This will install:
- React 18.2
- Vite 5.0
- Tailwind CSS 3.4
- Axios 1.6
- Lucide React 0.263

### 2. Configure Backend API

Edit `src/utils/constants.js` if your backend is not running on the default URL:

```javascript
export const API_BASE_URL = 'http://localhost:8080/api';
```

### 3. Start Development Server

```powershell
npm run dev
```

The app will open automatically at `http://localhost:3000`

### 4. Test the Application

#### Option A: With Backend Running
1. Ensure your Spring Boot backend is running on port 8080
2. Fill in the form with valid values:
   - Room Length: 3-15 meters
   - Room Width: 3-15 meters
   - Budget: $500-$10,000
3. Click "Generate Layout"
4. View your AI-generated room layout!

#### Option B: Without Backend (Mock Data)
If you don't have the backend ready yet, you can test with mock data:

1. Open `src/App.jsx`
2. Replace the import line:
   ```javascript
   // Change this:
   import { api } from './services/api';
   
   // To this:
   import { mockApi as api } from './utils/mockData';
   ```
3. Save and test the app with sample data

## Common Commands

```powershell
# Install dependencies
npm install

# Start development server (with hot reload)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Testing Different Scenarios

### Small Room, Low Budget
- Length: 4m
- Width: 3m
- Budget: $800

### Medium Room, Medium Budget
- Length: 8m
- Width: 6m
- Budget: $3000

### Large Room, High Budget
- Length: 12m
- Width: 10m
- Budget: $8000

## Expected Backend Response Format

Your Spring Boot API should return:

```json
{
  "furniture": [
    {
      "name": "Sofa",
      "x": 1.5,
      "y": 2.0,
      "width": 2.0,
      "length": 0.9,
      "price": 800.0
    }
  ],
  "totalCost": 4500.0,
  "reasoning": "AI explanation text"
}
```

## Troubleshooting

### "Unable to reach server" Error
- ✅ Check if Spring Boot is running: `http://localhost:8080`
- ✅ Check CORS configuration in backend
- ✅ Verify API_BASE_URL in constants.js

### Styles Not Loading
- ✅ Clear browser cache
- ✅ Restart dev server (Ctrl+C, then `npm run dev`)
- ✅ Check console for errors

### Dependencies Installation Failed
```powershell
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
Remove-Item -Recurse -Force node_modules
npm install
```

## Next Steps

1. ✅ Test form validation (try invalid values)
2. ✅ Test with different room sizes
3. ✅ Check mobile responsiveness
4. ✅ Review error handling
5. ✅ Connect to your actual backend API

## Need Help?

Check the main README.md for detailed documentation and API specifications.
