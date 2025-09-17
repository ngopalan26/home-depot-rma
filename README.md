# Travel Packing Assistant

A full-stack web application that helps travelers create intelligent packing lists based on weather conditions at their destinations. The system analyzes weather data for each location and date, then provides smart packing recommendations with detailed reasoning.

## 🌟 Features

- **Smart Travel Planning**: Create travel plans with multiple locations per day
- **Weather-Integrated Packing**: Real-time weather data drives packing suggestions
- **Intelligent Recommendations**: AI-powered suggestions based on temperature, conditions, and environmental factors
- **Duration Limits**: Maximum 14-day travel duration with validation
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Priority-Based Suggestions**: High, Medium, and Low priority items with reasoning

## 🏗️ Architecture

### Backend (Spring Boot)
- **RESTful API** with comprehensive validation
- **Weather Integration** via OpenWeatherMap API
- **Smart Packing Logic** with weather-based recommendations
- **H2 Database** for development (easily configurable for production)
- **Error Handling** with graceful fallbacks

### Frontend (React + TypeScript)
- **Material-UI** for modern, responsive design
- **TypeScript** for type safety and better development experience
- **Date Management** with validation and user-friendly interfaces
- **Real-time Weather Display** with intuitive icons and metrics

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+
- OpenWeatherMap API key (free at [openweathermap.org](https://openweathermap.org/api))

### Backend Setup

```bash
# Navigate to backend directory
cd travel-backend

# Set your OpenWeatherMap API key
export WEATHER_API_KEY=your-api-key-here

# Run the application
./mvnw spring-boot:run
```

The backend will start on port 8081.

### Frontend Setup

```bash
# Navigate to frontend directory
cd travel-frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend will start on port 3000 and automatically connect to the backend.

## 📱 Usage

1. **Create Travel Plan**:
   - Enter your name and travel dates (max 14 days)
   - Add locations for each day (up to 3 per day)
   - Submit to generate packing suggestions

2. **View Results**:
   - See weather conditions for each location
   - Review priority-based packing suggestions
   - Access consolidated packing summary

## 🎯 Packing Intelligence

The system generates suggestions based on:

### Temperature-Based Clothing
- **Cold Weather** (< 10°C): Warm jackets, thermal wear, hats, gloves
- **Hot Weather** (> 30°C): Lightweight clothing, sun protection
- **Moderate Weather**: Layered clothing options

### Weather Condition Items
- **Rain**: Rain jackets, umbrellas, waterproof shoes
- **Snow**: Snow boots, winter coats, snow gloves
- **Wind**: Windbreakers for strong winds

### Environmental Factors
- **High UV Index**: Sunscreen recommendations
- **High Humidity**: Moisture-wicking clothing
- **Visibility**: Appropriate accessories

## 🔧 Configuration

### Backend Configuration
Update `travel-backend/src/main/resources/application.yml`:

```yaml
weather:
  api:
    base-url: https://api.openweathermap.org/data/2.5
    api-key: ${WEATHER_API_KEY:your-api-key-here}

travel:
  max-days: 14
  max-locations-per-day: 3
```

### Frontend Configuration
Create `travel-frontend/.env`:

```env
REACT_APP_API_URL=http://localhost:8081/api
```

## 📊 API Endpoints

### Travel Plans
- `POST /api/travel/plans` - Create travel plan with packing suggestions
- `GET /api/travel/plans/{id}` - Retrieve travel plan results
- `GET /api/travel/health` - Health check

### Request Example
```json
{
  "startDate": "2024-01-15",
  "endDate": "2024-01-18",
  "travelerName": "John Doe",
  "travelDays": [
    {
      "date": "2024-01-15",
      "dayNumber": 1,
      "locations": [
        {
          "city": "Paris",
          "country": "France"
        }
      ]
    }
  ]
}
```

## 🗂️ Project Structure

```
travel-packing-assistant/
├── travel-backend/           # Spring Boot application
│   ├── src/main/java/com/homedepot/travel/
│   │   ├── controller/       # REST controllers
│   │   ├── dto/             # Data transfer objects
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Data access layer
│   │   ├── service/         # Business logic
│   │   └── config/          # Configuration
│   ├── src/main/resources/
│   │   └── application.yml  # Application configuration
│   └── pom.xml              # Maven dependencies
├── travel-frontend/          # React application
│   ├── src/
│   │   ├── components/      # Reusable UI components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API client
│   │   ├── types/          # TypeScript definitions
│   │   └── utils/          # Utility functions
│   └── package.json         # NPM dependencies
└── TODO.md                  # Project documentation
```

## 🧪 Testing

### Backend Testing
```bash
cd travel-backend
./mvnw test
```

### Frontend Testing
```bash
cd travel-frontend
npm test
```

## 🚀 Deployment

### Backend Deployment
```bash
# Build JAR
./mvnw clean package

# Run JAR
java -jar target/travel-packing-assistant-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
```bash
# Build for production
npm run build

# Serve static files
npx serve -s build
```

## 🔒 Security Considerations

- API key management for weather service
- Input validation and sanitization
- CORS configuration for cross-origin requests
- Error handling without sensitive information exposure

## 🛠️ Development

### Adding New Packing Rules
1. Update `PackingService.java` in the backend
2. Add new weather condition checks
3. Create corresponding suggestion logic

### Extending Weather Data
1. Modify `WeatherData.java` model
2. Update `WeatherService.java` parsing logic
3. Extend frontend display components

## 📈 Future Enhancements

- **User Accounts**: Save and manage multiple travel plans
- **Weather Forecasts**: Extended weather predictions
- **Packing Categories**: Organized by clothing, accessories, etc.
- **Export Functionality**: PDF packing lists
- **Mobile App**: Native mobile application
- **Social Features**: Share travel plans with friends

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:
1. Check the existing documentation
2. Review the TODO.md for known limitations
3. Create an issue with detailed information

## 🙏 Acknowledgments

- OpenWeatherMap for weather data API
- Material-UI for React components
- Spring Boot community for excellent documentation
