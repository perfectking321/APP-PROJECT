# 🎨 Component Specifications & Design Guide

## Design System Overview

### Color Palette

```css
/* Warm & Cozy Theme */
--warm-100: #F5F5F5  /* Lightest gray background */
--warm-200: #E8E8E8  /* Light gray */
--warm-300: #D4A574  /* Light wood tone */
--warm-400: #A0826D  /* Medium wood tone */
--warm-500: #8B7355  /* Primary wood tone */
--warm-600: #6B5D4F  /* Dark wood tone */
--accent:   #F4D03F  /* Gold accent */
--charcoal: #2C3E50  /* Text color */
```

### Typography

- **Font Family**: System fonts (-apple-system, BlinkMacSystemFont, Segoe UI)
- **Headings**: Bold, warm-500 color
- **Body Text**: charcoal color
- **Labels**: Medium weight, charcoal color
- **Error Messages**: Red-600

### Spacing & Sizing

- **Padding**: 4px increments (p-1 to p-12)
- **Border Radius**: rounded-lg (0.5rem), rounded-xl (0.75rem), rounded-2xl (1rem), rounded-3xl (1.5rem)
- **Shadows**: shadow-md, shadow-lg, shadow-xl, shadow-2xl

---

## Component Breakdown

### 1. App.jsx (Main Component)

**Purpose**: Root component managing application state and flow

**State Variables**:
```javascript
const [roomData, setRoomData] = useState(null);      // Form input data
const [layoutResult, setLayoutResult] = useState(null); // API response
const [loading, setLoading] = useState(false);       // Loading state
const [error, setError] = useState(null);            // Error message
```

**Flow**:
1. Initial: Show `RoomForm`
2. On submit: Show `LoadingSpinner`, call API
3. On success: Show `LayoutDisplay`
4. On error: Show error message with retry button
5. On reset: Return to step 1

**Error Handling**:
- Network errors → "Unable to reach server"
- Timeout (30s) → "Request took too long"
- API errors → Display specific error message

---

### 2. RoomForm.jsx (Input Form)

**Purpose**: Collect room dimensions and budget with validation

**Design Specifications**:
```css
Container:
- Max width: max-w-md (28rem)
- Padding: p-8 to p-12
- Background: glass-card effect (rgba(255,255,255,0.8) with backdrop-blur)
- Border radius: rounded-3xl
- Shadow: shadow-2xl

Title:
- Font size: text-4xl (2.25rem)
- Font weight: font-bold
- Color: warm-500
- Icon: 🏠 emoji

Input Fields:
- Border: border-2
- Border color: warm-200 (default), red-500 (error), warm-500 (focus)
- Border radius: rounded-lg
- Padding: px-4 py-3
- Focus ring: ring-4 ring-warm-200 ring-opacity-50

Button:
- Width: full width
- Padding: py-4 px-8
- Background: gradient-to-r from-warm-500 to-warm-400
- Hover: scale-105, from-warm-600 to-warm-500
- Disabled: bg-gray-400
```

**Validation Rules**:
```javascript
Room Length/Width:
- Required
- Must be a number
- Range: 3 to 15 meters
- Error: "Length must be between 3-15 meters"

Budget:
- Required
- Must be a number
- Range: $500 to $10,000
- Error: "Budget must be between $500-$10,000"
- Format: Whole number (no decimals)
```

**Icons**:
- Room Length: `<Ruler />` from lucide-react
- Room Width: `<Square />` from lucide-react
- Budget: `<DollarSign />` from lucide-react
- Submit Button: `<Sparkles />` from lucide-react
- Error: `<AlertCircle />` from lucide-react

**User Experience**:
- Validation triggers on blur (after leaving field)
- Errors show only after field is touched
- Submit disabled until all fields valid
- Loading spinner in button during submission
- Smooth animations for error messages

---

### 3. LayoutDisplay.jsx (Floor Plan)

**Purpose**: Visualize the generated room layout

**Layout Structure**:
```
┌─────────────────────────────────────────┐
│ Header (glass card)                     │
│ - Room dimensions                       │
│ - Budget info                           │
│ - "Design Another Room" button         │
└─────────────────────────────────────────┘
┌──────────────────────────┬──────────────┐
│ Floor Plan (2/3 width)   │ Cost Summary │
│ - 2D visualization       │ (1/3 width)  │
│ - Furniture pieces       │ - Total cost │
│                          │ - Progress   │
├──────────────────────────┤ - Reasoning  │
│ Furniture Details Grid   │              │
│ - Cards with info        │              │
│ - 2 columns              │              │
└──────────────────────────┴──────────────┘
```

**Floor Plan Specifications**:
```css
Container:
- Background: warm-100 (light gray)
- Padding: p-4
- Border radius: rounded-xl
- Grid pattern background (optional)

Room:
- Background: white
- Border: 4px solid warm-600
- Border radius: rounded-lg
- Aspect ratio: matches room dimensions
- Scale: 60 pixels per meter (default)

Dimension Label:
- Position: Top center
- Font: text-sm font-semibold
- Color: warm-600
- Format: "8m × 10m"
```

**Furniture Rendering**:
- Position: Absolute based on x, y coordinates
- Size: Scaled based on width and length
- Color: Flat color based on furniture type
- Border: 2px solid (darker shade of base color)
- Icon: Emoji centered on piece
- Label: Furniture name below icon
- Hover effect: translateY(-4px) with shadow

**Furniture Details Cards**:
```css
Card:
- Background: white
- Border radius: rounded-xl
- Padding: p-4
- Shadow: shadow
- Hover: scale-105, shadow-lg
- Cursor: pointer

Layout:
- Icon (emoji): text-3xl
- Name: font-bold text-lg
- Position: "At (2.5m, 1.8m)"
- Size: "2.0m × 0.9m"
- Price: font-bold text-warm-600
```

---

### 4. FurnitureItem.jsx (Individual Piece)

**Purpose**: Render a single furniture piece on the floor plan

**Props**:
```javascript
furniture: {
  name: string,      // "Sofa", "Coffee Table", etc.
  x: number,         // X position in meters
  y: number,         // Y position in meters
  width: number,     // Width in meters
  length: number,    // Length in meters
  price: number      // Price in dollars
}
scale: number        // Pixels per meter (default: 60)
```

**Styling**:
```css
Base:
- position: absolute
- border-radius: rounded-lg
- box-shadow: shadow-md
- cursor: grab
- display: flex
- flex-direction: column
- align-items: center
- justify-content: center

Colors (by furniture type):
- Sofa: #8B7355
- Coffee Table: #D4A574
- TV Stand: #6B5D4F
- Bookshelf: #A0826D
- Side Table: #E8C7A5
- Armchair: #9B8577
- Default: #A0826D

Hover Effect:
- transform: translateY(-4px)
- box-shadow: 0 10px 25px rgba(0,0,0,0.15)
```

**Icon Mapping**:
```javascript
Sofa: 🛋️
Coffee Table: ☕
TV Stand: 📺
Bookshelf: 📚
Side Table: 🪑
Armchair: 🪑
Dining Table: 🍽️
Chair: 🪑
Bed: 🛏️
Desk: 💼
Default: 🪑
```

---

### 5. CostSummary.jsx (Budget Breakdown)

**Purpose**: Display cost analysis and budget tracking

**Sections**:

1. **Total Items**
   - Icon: `<Package />`
   - Count: Number of furniture pieces
   - Style: bg-white bg-opacity-50 rounded-lg

2. **Budget Progress**
   - Progress bar with color coding:
     - Green: 0-80% used
     - Yellow: 80-95% used
     - Red: 95-100% used
   - Shows: Spent / Budget / Remaining
   - Percentage indicator

3. **AI Reasoning**
   - Icon: `<AlertCircle />`
   - Text: Italic, smaller font
   - Explanation from backend

4. **Over Budget Warning** (conditional)
   - Shows only if totalCost > budget
   - Red background with border
   - Amount over budget displayed

**Design**:
```css
Container:
- glass-card effect
- rounded-2xl
- padding: p-6
- shadow-xl
- sticky: top-8 (on desktop)

Progress Bar:
- Height: h-3
- Border radius: rounded-full
- Background: gray-200
- Fill: Gradient or solid based on percentage
- Transition: 500ms
```

---

### 6. LoadingSpinner.jsx (Loading State)

**Purpose**: Show loading state during API call

**Design**:
```css
Overlay:
- position: fixed
- inset: 0
- z-index: 50
- background: rgba(0,0,0,0.4) with backdrop-blur
- display: flex
- align-items: center
- justify-content: center

Card:
- glass-card effect
- rounded-3xl
- padding: p-8
- max-width: max-w-sm
- text-align: center

Spinner:
- Icon: <Loader2 /> from lucide-react
- Size: w-16 h-16
- Color: warm-500
- Animation: animate-spin

Text:
- Heading: text-xl font-semibold
- Subtext: text-sm text-gray-600
- Message: "Generating Your Perfect Layout"
```

---

## Animations

### Fade In
```css
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
/* Duration: 0.3s ease-in-out */
```

### Scale In
```css
@keyframes scaleIn {
  from { 
    transform: scale(0.9);
    opacity: 0;
  }
  to { 
    transform: scale(1);
    opacity: 1;
  }
}
/* Duration: 0.3s ease-in-out */
```

### Hover Effects
```css
/* Buttons */
hover: scale-105, brightness increase

/* Cards */
hover: translateY(-4px), shadow increase

/* Inputs */
focus: ring-4 with warm-200 color
```

---

## Responsive Breakpoints

```javascript
// Tailwind default breakpoints
sm: 640px   // Small tablets
md: 768px   // Tablets
lg: 1024px  // Desktop
xl: 1280px  // Large desktop
```

**Responsive Behavior**:
- Mobile: Stack all elements vertically
- Tablet: 1-2 column layout
- Desktop: 3-column layout with sidebar

---

## Accessibility Features

1. **Keyboard Navigation**: All interactive elements are keyboard accessible
2. **Focus States**: Clear focus rings on inputs and buttons
3. **Error Messages**: Associated with form fields using ARIA
4. **Alt Text**: Icons have descriptive titles
5. **Color Contrast**: WCAG AA compliant contrast ratios

---

## Performance Optimizations

1. **Lazy Loading**: Components load only when needed
2. **Memoization**: Use React.memo for FurnitureItem if needed
3. **Efficient Rendering**: Key props on list items
4. **Code Splitting**: Vite handles automatic code splitting
5. **Image Optimization**: Use emojis instead of image files

---

## Testing Checklist

- [ ] Form validation works for all fields
- [ ] API call shows loading spinner
- [ ] Success state displays layout correctly
- [ ] Error state shows appropriate message
- [ ] Reset button returns to form
- [ ] Responsive on mobile/tablet/desktop
- [ ] Hover effects work on all interactive elements
- [ ] Budget progress bar shows correct percentage
- [ ] Furniture pieces scale correctly
- [ ] Colors match design specification

---

## Browser Support

- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

**Required Features**:
- CSS Grid
- Flexbox
- CSS backdrop-filter
- ES6+ JavaScript
