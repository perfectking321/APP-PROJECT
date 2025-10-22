# 🏠 Interior Design Room Layout Planner

A modern React frontend application that connects to a Spring Boot REST API backend to display AI-generated furniture layouts for interior design planning.

![Interior Design Planner](https://img.shields.io/badge/React-18.2-blue) ![Vite](https://img.shields.io/badge/Vite-5.0-purple) ![Tailwind](https://img.shields.io/badge/Tailwind-3.4-cyan)

## ✨ Introduction

The Interior Design Room Layout Planner is an AI-powered application that helps you visualize and plan furniture layouts for your rooms. Simply input your room dimensions and budget, and the application will generate an intelligent furniture layout with cost breakdowns and placement recommendations.

**Key Features:**
- 🤖 AI-Powered furniture placement using OpenRouter's Llama 3.1 70B model
- 📐 Interactive 2D floor plan visualization
- 💰 Real-time budget tracking and cost summary
- 🎨 Modern glassmorphism design with warm, cozy aesthetics
- ✅ Form validation with user-friendly error messages
- 📱 Responsive design for desktop and mobile

## 📦 Requirements

### Frontend
- Node.js 16+ 
- npm or yarn

### Backend
- Java 17+
- Maven 3.6+
- OpenRouter API Key ([Get one here](https://openrouter.ai/))

### Tech Stack
- **Frontend**: React 18.2, Vite 5.0, Tailwind CSS 3.4, Axios 1.6
- **Backend**: Spring Boot, H2 Database, OpenRouter AI API

## 🚀 Steps to Run the Program

### 1. Setup Backend

Navigate to the backend directory:
```bash
cd backend
```

Set your OpenRouter API Key:
```bash
# Windows PowerShell
$env:OPENROUTER_API_KEY="your_api_key_here"

# Linux/Mac
export OPENROUTER_API_KEY=your_api_key_here
```

Build and run the Spring Boot application:
```bash
mvn clean install
mvn spring-boot:run
```

Verify the backend is running:
```bash
curl http://localhost:8080/api/health
```

The backend will run on `http://localhost:8080`

### 2. Setup Frontend

In a new terminal, navigate to the project root and install dependencies:
```bash
npm install
```

Start the development server:
```bash
npm run dev
```

Open your browser and navigate to `http://localhost:3000`

### 3. Using the Application

1. Enter your room dimensions (length and width in meters)
2. Set your budget (in dollars)
3. Click "Generate Layout" to see AI-powered furniture placement
4. View the interactive floor plan and cost breakdown
5. Click "Design Another Room" to start over

## 📸 Screenshots

*Screenshots will be added here*

---

Built with ❤️ using React, Vite, Tailwind CSS, Spring Boot, and OpenRouter AI
