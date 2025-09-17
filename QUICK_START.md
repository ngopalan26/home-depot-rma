# 🚀 Travel Packing Assistant - Quick Start Guide

## 📋 Prerequisites
- Java 17 or higher
- Node.js 16 or higher  
- npm (comes with Node.js)
- Maven (optional - Maven wrapper included)

## ⚡ Quick Start (Recommended)

```bash
# Clone or navigate to project directory
cd /path/to/travel-packing-assistant

# Run the automated startup script
./start-app.sh
```

This script will:
- Check prerequisites
- Start the backend (Spring Boot)
- Start the frontend (React)
- Open the application in your browser

## 🔧 Manual Setup

### Backend Setup
```bash
cd travel-backend

# Set weather API key (optional - uses fallback without it)
export WEATHER_API_KEY=your-openweathermap-api-key

# Start the backend
./mvnw spring-boot:run
```

### Frontend Setup
```bash
# In a new terminal
cd travel-frontend

# Install dependencies (first time only)
npm install

# Start the frontend
npm start
```

## 🌐 Access Points
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081
- **Health Check**: http://localhost:8081/api/travel/health
- **H2 Database Console**: http://localhost:8081/h2-console

## 🧪 Quick Test

1. Open http://localhost:3000
2. Enter your name
3. Select travel dates (max 14 days)
4. Add locations (e.g., "Paris, France")
5. Click "Get Packing Suggestions"
6. View weather and packing recommendations

## 📝 Sample Travel Plan

**Traveler**: John Doe  
**Dates**: Next week (3 days)  
**Locations**:
- Day 1: Paris, France
- Day 2: London, UK  
- Day 3: Amsterdam, Netherlands

## 🛠️ Troubleshooting

### Backend Issues
```bash
# Check if port 8081 is available
lsof -i :8081

# Kill existing process if needed
kill -9 <PID>
```

### Frontend Issues
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### API Key Issues
- Get free API key from [OpenWeatherMap](https://openweathermap.org/api)
- Set environment variable: `export WEATHER_API_KEY=your-key`
- Without API key, app uses fallback weather data

## 📚 Documentation
- **Full Documentation**: README.md
- **Demo Guide**: demo-script.md
- **Project Summary**: PROJECT_SUMMARY.md
- **Implementation Plan**: TODO.md

## 🎯 Key Features to Try

1. **Date Validation**: Try past dates or >14 day trips
2. **Multiple Locations**: Add up to 3 locations per day
3. **Weather Display**: See temperature, conditions, and details
4. **Packing Suggestions**: Review priority-based recommendations
5. **Responsive Design**: Test on mobile devices

## 🆘 Need Help?
- Check the comprehensive README.md
- Review the demo-script.md for detailed scenarios
- All documentation is in the project root directory
