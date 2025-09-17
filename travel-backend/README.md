# Travel Packing Assistant - Backend

A Spring Boot application that provides intelligent packing suggestions based on weather conditions for travel plans.

## Features

- **Travel Plan Management**: Create and manage travel plans with multiple locations per day
- **Weather Integration**: Real-time weather data from OpenWeatherMap API
- **Smart Packing Suggestions**: AI-powered recommendations based on weather conditions
- **Duration Validation**: Maximum 14-day travel duration limit
- **RESTful API**: Clean REST endpoints for frontend integration

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory for development)
- **Maven**
- **Jackson** for JSON processing

## API Endpoints

### Travel Plans
- `POST /api/travel/plans` - Create a new travel plan
- `GET /api/travel/plans/{id}` - Get travel plan with packing suggestions
- `GET /api/travel/health` - Health check endpoint

### Request Format

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

## Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- OpenWeatherMap API key

### Configuration

1. Set your OpenWeatherMap API key:
   ```bash
   export WEATHER_API_KEY=your-api-key-here
   ```

2. Or update `application.yml`:
   ```yaml
   weather:
     api:
       api-key: your-api-key-here
   ```

### Running the Application

```bash
# Navigate to backend directory
cd travel-backend

# Run with Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/travel-packing-assistant-0.0.1-SNAPSHOT.jar
```

The application will start on port 8081.

### Database

- H2 in-memory database is used for development
- Access H2 console at: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:traveldb`
- Username: `sa`
- Password: `password`

## Data Models

### TravelPlan
- Basic travel information (dates, traveler name)
- Collection of TravelDays

### TravelDay
- Date and day number
- Collection of Locations and PackingSuggestions

### Location
- City and country information
- Associated WeatherData

### WeatherData
- Temperature, humidity, wind speed
- Weather conditions and descriptions
- Precipitation and UV index

### PackingSuggestion
- Item name and category
- Reasoning and priority
- Quantity recommendations

## Packing Logic

The system generates packing suggestions based on:

1. **Temperature-based clothing**:
   - Cold weather: Warm jacket, thermal underwear, hat, gloves
   - Hot weather: Lightweight clothing, sun hat, sunglasses
   - Moderate weather: Layered clothing

2. **Weather condition-based items**:
   - Rain: Rain jacket, umbrella, waterproof shoes
   - Snow: Snow boots, winter coat, snow gloves
   - Wind: Windbreaker

3. **Environmental factors**:
   - High UV index: Sunscreen
   - High humidity: Moisture-wicking clothing

## Error Handling

- Comprehensive validation for travel plans
- Graceful fallback for weather API failures
- Detailed error messages for client applications

## Development

### Project Structure
```
src/main/java/com/homedepot/travel/
├── controller/     # REST controllers
├── dto/           # Data transfer objects
├── model/         # JPA entities
├── repository/    # Data access layer
├── service/       # Business logic
└── config/        # Configuration classes
```
