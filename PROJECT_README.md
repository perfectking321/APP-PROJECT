# 🏠 Interior Design AI Room Layout Planner

A full-stack application that uses AI to generate optimal furniture layouts for interior design. Built with React (frontend) and Spring Boot (backend), powered by OpenRouter's Llama 3.1 70B model.

![Project Status](https://img.shields.io/badge/status-active-success)
![React](https://img.shields.io/badge/React-18.2-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-green)
![Java](https://img.shields.io/badge/Java-17-orange)

## 🎯 Features

### Frontend (React)
- ✨ Modern glassmorphism UI with warm color scheme
- 📱 Responsive design (desktop & mobile)
- 🎨 Interactive 2D floor plan visualization
- 💰 Real-time budget tracking and cost summary
- ✅ Form validation with instant feedback
- 🔄 Smooth animations and loading states

### Backend (Spring Boot)
- 🤖 AI-powered layout generation (Llama 3.1 70B)
- 📏 Rule-based validation engine
- 🗄️ H2 in-memory database with 25+ furniture items
- 🔒 CORS-enabled REST API
- ⚡ Retry logic and timeout handling
- 📊 Comprehensive error handling and logging

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    React Frontend                           │
│                   (Port 3000)                               │
│  - RoomForm: Input collection                              │
│  - LayoutDisplay: 2D visualization                         │
│  - CostSummary: Budget tracking                            │
└─────────────────────┬───────────────────────────────────────┘
                      │ REST API (HTTP)
                      ↓
┌─────────────────────────────────────────────────────────────┐
│               Spring Boot Backend                           │
│                  (Port 8080)                                │
│                                                             │
│  LayoutRestController                                       │
│         ↓                                                   │
│  LayoutService                                              │
│         ↓                                                   │
│  ┌─────────────┬─────────────┬─────────────┐              │
│  ↓             ↓             ↓              │              │
│  FurnitureDAO  AIService     RuleEngine     │              │
│  ↓             ↓             ↓              │              │
│  H2 Database   OpenRouter    Validation     │              │
└─────────────────────────────────────────────┴──────────────┘
```

## 🚀 Quick Start

### Prerequisites
- **Node.js** 16+ and npm
- **Java** 17+
- **Maven** 3.6+
- **OpenRouter API Key** ([Get one here](https://openrouter.ai/))

### 1. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Set API key (Windows PowerShell)
$env:OPENROUTER_API_KEY="your_api_key_here"

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start on http://localhost:8080

**Verify backend:**
```bash
curl http://localhost:8080/api/health
```

### 2. Frontend Setup

```bash
# Navigate to frontend directory (in a new terminal)
cd APP PROJECT

# Install dependencies
npm install

# Start dev server
npm run dev
```

Frontend will start on http://localhost:3000

### 3. Test the Application

1. Open http://localhost:3000 in your browser
2. Fill in the form:
   - Room Length: 8m
   - Room Width: 6m
   - Budget: $3000
3. Click "Generate Layout"
4. View your AI-generated room layout!

## 📁 Project Structure

```
APP PROJECT/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/interiordesign/
│   │   ├── InteriorDesignApplication.java
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── dao/               # Database access
│   │   ├── model/             # Data models
│   │   ├── service/           # Business logic
│   │   └── ai/                # AI integration
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── schema.sql
│   │   └── data.sql
│   ├── pom.xml
│   └── README.md
│
├── src/                       # React frontend
│   ├── components/
│   │   ├── RoomForm.jsx       # Input form
│   │   ├── LayoutDisplay.jsx  # Floor plan view
│   │   ├── FurnitureItem.jsx  # Individual furniture
│   │   ├── CostSummary.jsx    # Budget summary
│   │   └── LoadingSpinner.jsx # Loading state
│   ├── services/
│   │   └── api.js             # Axios API client
│   ├── utils/
│   │   ├── constants.js       # Configuration
│   │   └── mockData.js        # Test data
│   ├── App.jsx
│   └── index.css
│
├── package.json
├── vite.config.js
├── tailwind.config.js
└── README.md
```

## 🔌 API Endpoints

### Backend REST API (Port 8080)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/health` | GET | Health check |
| `/api/furniture` | GET | Get all furniture |
| `/api/layout` | POST | Generate layout |

**Example Request:**
```bash
curl -X POST http://localhost:8080/api/layout \
  -H "Content-Type: application/json" \
  -d '{
    "length": 5.0,
    "width": 4.0,
    "budget": 2000
  }'
```

**Example Response:**
```json
{
  "furniture": [
    {
      "furniture": {
        "id": 1,
        "name": "Sofa",
        "width": 2.0,
        "depth": 0.9,
        "price": 800,
        "category": "sofa"
      },
      "x": 1.0,
      "y": 2.5
    }
  ],
  "totalCost": 1800,
  "warnings": [],
  "reasoning": "AI explanation of layout decisions"
}
```

## 🎨 Design System

### Color Palette
- **Background**: `#F5F5F5`, `#E8E8E8` (neutral grays)
- **Primary**: `#8B7355`, `#A0826D` (warm wood tones)
- **Accent**: `#F4D03F` (soft gold)
- **Text**: `#2C3E50` (dark charcoal)

### UI Components
- **Glassmorphism Cards**: Frosted glass effect with backdrop blur
- **Gradient Buttons**: Warm tone gradients with hover effects
- **Smooth Animations**: 0.2s ease-in-out transitions
- **Icons**: Lucide React for consistent iconography

## 🧠 AI Integration

### How It Works

1. **User Input** → React form collects room dimensions and budget
2. **API Call** → Frontend sends POST request to backend
3. **AI Prompt** → Backend builds detailed prompt with furniture catalog
4. **OpenRouter API** → Calls Llama 3.1 70B model
5. **Response Parsing** → Extracts JSON from AI response
6. **Validation** → RuleEngine validates and adjusts positions
7. **Return Layout** → Backend sends validated layout to frontend
8. **Visualization** → React renders 2D floor plan

### AI Model
- **Provider**: OpenRouter
- **Model**: Llama 3.1 70B Instruct (Free tier)
- **Timeout**: 30 seconds
- **Retry**: Up to 3 attempts

### Validation Rules
- ✅ Furniture must fit within room boundaries
- ✅ Minimum 0.5m clearance from walls
- ✅ No overlapping furniture (0.3m min gap)
- ✅ Total cost must not exceed budget
- ✅ Auto-adjustment for invalid positions

## 📊 Database Schema

### Furniture Table
```sql
CREATE TABLE furniture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    width DOUBLE NOT NULL,
    depth DOUBLE NOT NULL,
    price INT NOT NULL,
    category VARCHAR(50) NOT NULL
);
```

**Sample Data**: 25+ furniture items including:
- Sofas, loveseats, sectionals
- Coffee tables, side tables
- TV stands, media consoles
- Bookshelves, cabinets
- Armchairs, recliners, ottomans
- Dining tables and chairs
- Beds, nightstands, dressers
- Desks and office chairs

## 🧪 Testing

### Backend Testing
```bash
# Test health endpoint
curl http://localhost:8080/api/health

# Test furniture catalog
curl http://localhost:8080/api/furniture

# Test layout generation
curl -X POST http://localhost:8080/api/layout \
  -H "Content-Type: application/json" \
  -d '{"length":5.0,"width":4.0,"budget":2000}'
```

### Frontend Testing
1. Test form validation (enter invalid values)
2. Test loading state (watch spinner during generation)
3. Test error handling (stop backend and try generating)
4. Test layout display (verify furniture placement)
5. Test responsive design (resize browser window)

### Test Scenarios

**Small Room, Low Budget:**
```json
{ "length": 4.0, "width": 3.0, "budget": 800 }
```

**Medium Room, Medium Budget:**
```json
{ "length": 8.0, "width": 6.0, "budget": 3000 }
```

**Large Room, High Budget:**
```json
{ "length": 12.0, "width": 10.0, "budget": 8000 }
```

## 🐛 Troubleshooting

### Backend Issues

**"OpenRouter API Key Not Configured"**
```bash
# Set environment variable
export OPENROUTER_API_KEY=your_key_here
```

**"Port 8080 already in use"**
```bash
# Change port
mvn spring-boot:run -Dserver.port=9090
```

### Frontend Issues

**"Unable to reach server"**
- Ensure backend is running on port 8080
- Check CORS configuration in `application.properties`
- Verify `API_BASE_URL` in `src/utils/constants.js`

**Styles not loading**
```bash
# Restart dev server
npm run dev
```

## 📚 Documentation

- [Backend README](./backend/README.md) - Complete backend documentation
- [Backend API Docs](./backend/API_DOCUMENTATION.md) - API reference
- [Backend Quick Start](./backend/QUICKSTART.md) - Quick setup guide
- [Environment Setup](./backend/ENVIRONMENT_SETUP.md) - Configuration guide
- [Frontend Components](./COMPONENTS.md) - Component specifications
- [Frontend Quick Start](./QUICKSTART.md) - Frontend setup guide

## 🔒 Security

- ✅ API keys stored as environment variables
- ✅ Input validation on frontend and backend
- ✅ CORS properly configured
- ✅ SQL injection protection (JDBC templates)
- ✅ Sanitized error messages

## 📈 Performance

- **Frontend Build**: Vite (faster than CRA)
- **Backend Response**: 2-5 seconds average
- **Database**: In-memory H2 (no disk I/O)
- **API Timeout**: 30 seconds (configurable)
- **Retry Logic**: Exponential backoff

## 🚀 Deployment

### Frontend (Vercel/Netlify)
```bash
npm run build
# Deploy dist/ folder
```

### Backend (Heroku/AWS/Azure)
```bash
mvn clean package
# Deploy target/*.jar file
```

### Docker
See `backend/QUICKSTART.md` for Docker instructions

## 🛠️ Technologies Used

### Frontend
- React 18.2
- Vite 5.0
- Tailwind CSS 3.4
- Axios 1.6
- Lucide React 0.263

### Backend
- Spring Boot 3.2.2
- Java 17
- Spring Web (REST)
- Spring WebFlux (WebClient)
- Spring JDBC
- H2 Database
- Maven

### AI
- OpenRouter API
- Llama 3.1 70B Instruct

## 🎯 Future Enhancements

- [ ] Furniture rotation support
- [ ] 3D visualization
- [ ] User authentication
- [ ] Save/load layouts
- [ ] Multiple room types
- [ ] Custom furniture catalog
- [ ] Drag-and-drop furniture editing
- [ ] Export to PDF/image
- [ ] Dark mode

## 📄 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 💬 Support

For issues or questions:
- Check documentation in `/backend/` and root README files
- Review troubleshooting sections
- Check application logs for errors
- Create an issue on GitHub

---

**Built with ❤️ using React, Spring Boot, and AI**

**Happy Designing! 🎨🏠**
