#!/bin/bash

# Travel Packing Assistant Startup Script

echo "🚀 Starting Travel Packing Assistant..."
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
    exit 1
fi

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is not installed. Please install npm."
    exit 1
fi

# Check for weather API key
if [ -z "$WEATHER_API_KEY" ]; then
    echo "⚠️  Warning: WEATHER_API_KEY environment variable is not set."
    echo "   The application will use fallback weather data."
    echo "   To get real weather data, set your OpenWeatherMap API key:"
    echo "   export WEATHER_API_KEY=your-api-key-here"
    echo ""
fi

echo "📋 Starting Backend (Spring Boot)..."
cd travel-backend

# Start backend in background
./mvnw spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Check if backend is running
if ! curl -s http://localhost:8081/api/travel/health > /dev/null; then
    echo "❌ Backend failed to start. Check the logs above."
    kill $BACKEND_PID 2>/dev/null
    exit 1
fi

echo "✅ Backend started successfully on port 8081"
echo ""

echo "📱 Starting Frontend (React)..."
cd ../travel-frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
fi

echo "🌐 Starting React development server..."
echo ""
echo "🎉 Application is starting up!"
echo "   Backend: http://localhost:8081"
echo "   Frontend: http://localhost:3000"
echo ""
echo "Press Ctrl+C to stop both servers"

# Start frontend
npm start

# Cleanup function
cleanup() {
    echo ""
    echo "🛑 Stopping servers..."
    kill $BACKEND_PID 2>/dev/null
    echo "✅ All servers stopped."
    exit 0
}

# Set up signal handlers
trap cleanup SIGINT SIGTERM

# Wait for user to stop
wait
