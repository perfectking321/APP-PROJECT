# 🏠 Interior Design Room Layout Planner

A modern full-stack application that uses AI to generate intelligent furniture layouts for interior design planning. Built with React frontend and Spring Boot backend, featuring a beautiful glassmorphism UI with real-time budget tracking and interactive floor plan visualization.

![Interior Design Planner](https://img.shields.io/badge/React-18.2-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green) ![Vite](https://img.shields.io/badge/Vite-5.0-purple) ![Tailwind](https://img.shields.io/badge/Tailwind-3.4-cyan)

## ✨ Features

- 🎨 **Modern Glassmorphism Design** - Frosted glass effects with warm, cozy color scheme
- 🤖 **AI-Powered Layout Generation** - Connects to Spring Boot backend for intelligent furniture placement
- 📐 **Interactive Floor Plan Visualization** - 2D view with scaled furniture pieces
- 💰 **Budget Tracking** - Real-time cost summary with visual progress indicators
- ✅ **Form Validation** - Real-time validation with user-friendly error messages
- 📱 **Responsive Design** - Works on desktop and mobile devices
- 🎭 **Smooth Animations** - Polished transitions and loading states

## 🎨 Design System

### Color Scheme (Warm & Cozy)
- **Background**: Neutral grays (#F5F5F5, #E8E8E8)
- **Primary**: Warm wood tones (#8B7355, #A0826D)
- **Accents**: Soft gold (#F4D03F)
- **Text**: Dark charcoal (#2C3E50)

### UI Style
- Centered card with glassmorphism effect
- Gradient backgrounds (beige to soft peach)
- Minimalist icons (Lucide React)
- Large, prominent buttons with hover effects
- Smooth animations throughout

## 📦 Tech Stack

- **React 18.2** - UI library
- **Vite 5.0** - Fast build tool
- **Tailwind CSS 3.4** - Utility-first CSS framework
- **Axios 1.6** - HTTP client for API calls
- **Lucide React** - Beautiful icon library

## 🚀 Getting Started

### Prerequisites

- Node.js 16+ and npm/yarn
- Spring Boot backend running on `http://localhost:8080`

### Installation

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Start development server:**
   ```bash
   npm run dev
   ```

3. **Open browser:**
   Navigate to `http://localhost:3000`

### Build for Production

```bash
npm run build
npm run preview
```

## 📁 Project Structure

```
src/
├── App.jsx                    # Main component with state management
├── main.jsx                   # React entry point
├── index.css                  # Tailwind imports + custom styles
├── components/
│   ├── RoomForm.jsx           # Input form with validation
│   ├── LayoutDisplay.jsx      # Floor plan visualization
│   ├── FurnitureItem.jsx      # Individual furniture piece
│   ├── CostSummary.jsx        # Budget breakdown
│   └── LoadingSpinner.jsx     # Loading state
├── services/
│   └── api.js                 # Axios API calls
└── utils/
    └── constants.js           # API URLs, colors, validation
```

## 🔌 API Integration

### Backend API Endpoints

The app expects these endpoints from the Spring Boot backend:

#### 1. Generate Layout
```
POST http://localhost:8080/api/layout
Content-Type: application/json

Request Body:
{
  "length": 10.0,
  "width": 8.0,
  "budget": 5000.0
}

Response:
{
  "furniture": [
    {
      "name": "Sofa",
      "x": 1.5,
      "y": 2.0,
      "width": 2.0,
      "length": 0.9,
      "price": 800.0
    },
    // ... more furniture items
  ],
  "totalCost": 4500.0,
  "reasoning": "AI explanation of the layout choices"
}
```

#### 2. Get All Furniture (Optional)
```
GET http://localhost:8080/api/furniture

Response:
[
  {
    "id": 1,
    "name": "Sofa",
    "width": 2.0,
    "length": 0.9,
    "price": 800.0,
    "category": "Seating"
  },
  // ... more furniture items
]
```

### Configuring API URL

Update the API base URL in `src/utils/constants.js`:

```javascript
export const API_BASE_URL = 'http://localhost:8080/api';
```

## 🎯 Component Details

### RoomForm.jsx
- Centered glassmorphism card design
- Three input fields: Room Length, Room Width, Budget
- Real-time validation (3-15m for dimensions, $500-$10,000 for budget)
- Icons next to each input
- Large gradient button with loading state
- Error messages with smooth animations

### LayoutDisplay.jsx
- **Floor Plan**: 2D visualization with grid background
- **Furniture Rendering**: Scaled pieces with colors, icons, and labels
- **Furniture List**: Cards showing details and prices
- **Reset Button**: "Design Another Room" to start over

### CostSummary.jsx
- Total items count
- Budget usage progress bar (color-coded)
- Cost breakdown (spent/budget/remaining)
- AI reasoning display
- Over-budget warning

### LoadingSpinner.jsx
- Full-screen overlay with blur effect
- Centered spinner with message
- Glassmorphism card design

## 🎨 Customization

### Changing Colors

Edit `tailwind.config.js`:

```javascript
colors: {
  warm: {
    100: '#F5F5F5',
    200: '#E8E8E8',
    300: '#D4A574',
    400: '#A0826D',
    500: '#8B7355',
    600: '#6B5D4F',
  },
  accent: '#F4D03F',
}
```

### Adding New Furniture Types

Edit `src/utils/constants.js`:

```javascript
export const FURNITURE_COLORS = {
  'New Furniture': '#HEXCOLOR',
  // ...
};

export const FURNITURE_ICONS = {
  'New Furniture': '🛋️',
  // ...
};
```

### Adjusting Validation Rules

Edit `src/utils/constants.js`:

```javascript
export const VALIDATION = {
  ROOM_LENGTH: { MIN: 3, MAX: 15 },
  ROOM_WIDTH: { MIN: 3, MAX: 15 },
  BUDGET: { MIN: 500, MAX: 10000 },
};
```

## 🐛 Troubleshooting

### API Connection Issues

**Problem**: "Unable to reach server" error

**Solutions**:
1. Ensure Spring Boot backend is running on port 8080
2. Check CORS configuration in backend
3. Verify API_BASE_URL in `src/utils/constants.js`

### Build Errors

**Problem**: Module not found errors

**Solution**:
```bash
rm -rf node_modules package-lock.json
npm install
```

### Styling Issues

**Problem**: Tailwind styles not applying

**Solutions**:
1. Ensure `index.css` imports Tailwind directives
2. Check `tailwind.config.js` content paths
3. Restart dev server after config changes

## 📝 Development Notes

### Form Validation Flow
1. User types → onChange validates if field touched
2. User leaves field → onBlur marks field as touched and validates
3. Submit → Validates all fields, shows errors if any
4. Only submits if all fields valid

### API Error Handling
- Network errors: "Cannot connect to server"
- Timeout (30s): "Request took too long"
- Server errors (5xx): Display server message or generic error
- Validation errors (4xx): Display specific error message

### Furniture Positioning
- Backend provides furniture with x, y coordinates in meters
- Frontend scales using `PIXELS_PER_METER` constant (default: 60)
- Furniture rendered with absolute positioning
- Hover effects show details via tooltip

## 🚀 Future Enhancements

- [ ] Drag-and-drop furniture repositioning
- [ ] 3D visualization mode
- [ ] Save/load layout functionality
- [ ] Export to PDF/image
- [ ] Multiple room types (bedroom, kitchen, etc.)
- [ ] Furniture rotation
- [ ] Custom furniture catalog
- [ ] Dark mode toggle

## 📄 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

Built with ❤️ using React, Vite, and Tailwind CSS
