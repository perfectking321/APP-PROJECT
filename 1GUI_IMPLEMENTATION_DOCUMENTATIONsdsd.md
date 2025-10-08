# GUI Implementation Documentation
## AI-Powered Interior Design Layout Planner

---

**Project Name:** Interior Design Layout Planner  
**Version:** 1.0.0  
**Author:** [Your Name]  
**Date:** October 9, 2025  
**Prepared for:** College Professor Evaluation

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [GUI Architecture Overview](#2-gui-architecture-overview)
3. [User Interface Components](#3-user-interface-components)
4. [Code Structure and Implementation](#4-code-structure-and-implementation)
5. [Conclusion and References](#5-conclusion-and-references)

---

<div style="page-break-after: always;"></div>

## 1. Introduction

### 1.1 Project Overview

The Interior Design Layout Planner is an **AI-powered web application** that generates intelligent room layouts with optimized furniture placement and cost estimations. The system analyzes room dimensions and budget constraints to recommend suitable furniture arrangements, making professional interior design accessible to everyone. The application features a modern, responsive GUI built with React and a robust Spring Boot backend.

### 1.2 Purpose and Scope

**Primary Purpose:** Help users design rooms without professional assistance while providing cost-effective solutions within budget constraints.

**Key Capabilities:**
- Input room dimensions and budget
- Generate AI-powered furniture layouts
- View 2D floor plans with accurate positioning
- Receive detailed cost breakdowns

### 1.3 Document Objectives

This documentation provides a comprehensive guide to the GUI implementation, specifically prepared for academic evaluation. It demonstrates technical competency in modern web application development, including frontend framework usage, API integration, state management, and user experience design principles.

---

<div style="page-break-after: always;"></div>

## 2. GUI Architecture Overview

### 2.1 Design Pattern Used

The application implements the **Model-View-Controller (MVC)** architectural pattern:

**Model (Backend):** `Room`, `Furniture`, `RoomLayout`, `FurniturePosition` - Data representation  
**View (Frontend):** React components - User interface  
**Controller (Backend):** `LayoutRestController`, `LayoutService`, `AIService` - Business logic

This separation ensures maintainability, scalability, and clear responsibility boundaries.

### 2.2 GUI Framework/Technology

**Frontend:** React 18.2.0, Vite 5.0.8, Tailwind CSS 3.4.0  
**Backend:** Spring Boot 3.2.2, Java 17

React provides component reusability, Vite offers fast development experience, and Spring Boot delivers enterprise-grade backend capabilities.

### 2.3 Component Hierarchy

```
App (Root Component)
│
├── LoadingSpinner (when loading)
├── RoomForm (Initial state)
│   └── Input Fields (Length, Width, Budget)
└── LayoutDisplay (After generation)
    ├── Floor Plan → FurnitureItem (multiple)
    ├── Furniture List
    └── CostSummary
```

### 2.4 System Architecture Diagram

```
USER (Web Browser)
         ↓
FRONTEND (React + Vite - Port 3000)
  RoomForm → App → LayoutDisplay
              ↓
        API Service (axios)
              ↓ HTTP REST (JSON)
BACKEND (Spring Boot - Port 8080)
  LayoutRestController
         ↓
   LayoutService
    ↓         ↓
FurnitureDAO  AIService
    ↓            ↓
H2 Database  External AI API
```

**Data Flow:** User input → Frontend validation → API call → Backend processing → AI generation → Database query → Response → UI rendering

---

<div style="page-break-after: always;"></div>

## 3. User Interface Components

### 3.1 Main Application Layout

Full-screen, single-page layout with three states: Form View (initial), Loading View (processing), and Results View (displaying layout). Responsive design adapts from mobile (320px) to desktop (1920px+).

### 3.2 Room Form Component

**Purpose:** Captures user input for room specifications and budget.

Input fields with real-time validation:
- **Length Input:** 2-20 meters range with number validation
- **Width Input:** 2-20 meters range with number validation  
- **Budget Input:** $100-$50,000 range with currency formatting
- **Validation:** Error messages appear after field loses focus
- **Submit Button:** Disabled until all fields are valid

### 3.3 Layout Display Component

**Purpose:** Visualizes the AI-generated room layout.

Features:
- **Header:** Room dimensions, budget, and reset button
- **Floor Plan:** 2D representation with scaled furniture placement
- **Furniture Tooltips:** Hover to see name, dimensions, and cost
- **Scale Indicator:** Shows scale ratio for accuracy
- **Grid Background:** Subtle grid pattern for spatial reference

### 3.4 Furniture Item Component

**Purpose:** Represents individual furniture on the floor plan.

Each item rendered with:
- Emoji icon (🛋️ sofa, 🪑 chair, etc.)
- Accurate sizing based on dimensions × scale
- Absolute positioning from x, y coordinates
- Interactive tooltip with details on hover
- Color-coded backgrounds by category

### 3.5 Cost Summary Component

**Purpose:** Displays financial breakdown.

Shows:
- Total furniture cost (large, prominent display)
- Budget comparison with remaining/exceeded amount
- Status indicator (green checkmark or red warning)
- Individual item costs in a list
- Visual progress bar for budget utilization

### 3.6 Loading States and Spinners

**Purpose:** Visual feedback during API calls.

Full-screen overlay with centered spinner appears during layout generation, preventing user interaction while processing. Uses CSS animations for smooth rotation.

---

<div style="page-break-after: always;"></div>

## 4. Code Structure and Implementation

### 4.1 Main GUI Files Structure

```
src/
├── main.jsx                   # Application entry point
├── App.jsx                    # Root component, manages state
├── index.css                  # Global styles and Tailwind imports
├── components/
│   ├── RoomForm.jsx          # Input form with validation
│   ├── LayoutDisplay.jsx     # Layout visualization
│   ├── FurnitureItem.jsx     # Individual furniture renderer
│   ├── CostSummary.jsx       # Cost breakdown display
│   └── LoadingSpinner.jsx    # Loading indicator
├── services/
│   └── api.js                # API methods and error handling
└── utils/
    └── constants.js          # Validation rules, API URLs
```

### 4.2 Key Functions and Methods

**handleFormSubmit (App.jsx)** - Orchestrates layout generation, manages loading state and API calls

**generateLayout (api.js)** - Sends POST request to backend with room specifications

**validateField (RoomForm.jsx)** - Validates individual form fields against rules

**renderFurniture (LayoutDisplay.jsx)** - Iterates and renders furniture items on floor plan

### 4.3 Event Handlers

| Handler | Location | Description |
|---------|----------|-------------|
| `handleChange` | RoomForm.jsx | Updates form field state on input |
| `handleBlur` | RoomForm.jsx | Validates field on blur |
| `handleSubmit` | RoomForm.jsx | Validates and triggers submission |
| `handleReset` | App.jsx | Clears state, returns to form |

### 4.4 State Management

**App.jsx State:**
- `roomData` - Submitted room specifications
- `layoutResult` - API response with layout
- `loading` - Boolean for API call status
- `error` - Error message if API fails

**RoomForm.jsx State:**
- `formData` - Object with length, width, budget
- `errors` - Validation error messages
- `touched` - Tracks field interactions

### 4.5 Code Snippets

**Form Submission with Validation:**
```javascript
const handleSubmit = (e) => {
  e.preventDefault();
  const newErrors = {};
  Object.keys(formData).forEach((key) => {
    const error = validateField(key, formData[key]);
    if (error) newErrors[key] = error;
  });
  if (Object.keys(newErrors).length === 0) {
    onSubmit({
      length: parseFloat(formData.length),
      width: parseFloat(formData.width),
      budget: parseFloat(formData.budget),
    });
  }
};
```

**API Call with Error Handling:**
```javascript
const handleFormSubmit = async (formData) => {
  setLoading(true);
  try {
    const result = await api.generateLayout(formData);
    setLayoutResult(result);
  } catch (err) {
    setError(err);
  } finally {
    setLoading(false);
  }
};
```

**Layout Rendering with Scaling:**
```javascript
const scale = PIXELS_PER_METER;
const containerWidth = roomWidthM * scale;
const containerHeight = roomLengthM * scale;

return (
  <div style={{ width: containerWidth, height: containerHeight }}>
    {furniture.map((item, index) => (
      <FurnitureItem key={index} furniture={item} scale={scale} />
    ))}
  </div>
);
```

---

<div style="page-break-after: always;"></div>

## 5. Conclusion and References

### 5.1 Summary

The Interior Design Layout Planner successfully demonstrates proficiency in modern web development through overcoming several technical challenges:

**Key Technical Challenges:**

1. **Complex State Management** - Managing interdependent states (form data, validation, API responses) required careful coordination to prevent inconsistent UI states.

2. **Responsive Scaling Algorithm** - Implementing accurate floor plan scaling across different room sizes while maintaining furniture proportions demanded precise calculations.

3. **Real-time Form Validation** - Creating instant feedback without overwhelming users required intelligent touch-state tracking and validation timing.

4. **Robust API Error Handling** - Developing comprehensive error handling for network failures, timeouts, and server errors while providing meaningful user feedback.

5. **CORS Configuration** - Properly configuring secure cross-origin communication between frontend and backend.

6. **Rendering Performance** - Ensuring smooth 60fps rendering of floor plans with multiple furniture items through React optimization techniques.

**Demonstrated Competencies:**
- Modern React development with functional components and hooks
- RESTful API design and integration
- Responsive UI with Tailwind CSS
- Component-based architecture
- MVC design pattern implementation

### 5.2 Technical References

1. **React Documentation** - https://react.dev/ - React concepts, hooks, and best practices
2. **Spring Boot Documentation** - https://spring.io/projects/spring-boot - Spring Boot reference and API docs
3. **Vite Documentation** - https://vitejs.dev/ - Build tool configuration guide
4. **Tailwind CSS Documentation** - https://tailwindcss.com/docs - Utility class reference
5. **Axios Documentation** - https://axios-http.com/docs/intro - HTTP client API reference
6. **Lucide React Icons** - https://lucide.dev/ - Icon library documentation
7. **Java 17 Documentation** - https://docs.oracle.com/en/java/javase/17/ - Java API documentation

---

## Appendix

**Document Metadata:**
- Total Pages: 10
- Sections: 5 major sections
- Code Snippets: 3
- Diagrams: 2

**Revision History:**

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | October 9, 2025 | Initial documentation release |

---

**End of Document**
