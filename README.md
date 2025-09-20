# Travel Packing Assistant

A smart travel packing assistant that suggests clothing based on weather conditions for your travel destinations.

## Features

- 📍 **Location Input**: Country, state, and city selection with cascading dropdowns
- 📅 **Travel Planning**: Plan trips up to 2 weeks
- 🌤️ **Weather Integration**: Real-time weather data from OpenWeatherMap API
- 👕 **Smart Suggestions**: Detailed clothing recommendations based on weather conditions
- 🌐 **Web Interface**: Modern React frontend with Material-UI components

## Architecture

- **Frontend**: React with TypeScript, Material-UI
- **Backend**: Spring Boot with Java
- **Database**: H2 in-memory database
- **Deployment**: Google Cloud Run
- **Weather API**: OpenWeatherMap

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Maven
- Google Cloud Project

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/ngopalan26/travel-packing-assistant.git
   cd travel-packing-assistant
   ```

2. **Start Backend**
   ```bash
   cd travel-backend
   ./mvnw spring-boot:run
   ```

3. **Start Frontend**
   ```bash
   cd travel-frontend
   npm install
   npm start
   ```

4. **Access the application**
   - Frontend: http://localhost:3000
   - Backend: http://localhost:8081

## Deployment

The application is deployed on Google Cloud Run:

- **Frontend**: https://travel-frontend-1090615745845.us-central1.run.app
- **Backend**: Configured with environment variables

## API Configuration

Set the following environment variable for the weather API:
- `WEATHER_API_KEY`: Your OpenWeatherMap API key

## Project Structure

```
├── travel-backend/          # Spring Boot backend
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── Dockerfile          # Backend container configuration
├── travel-frontend/        # React frontend
│   ├── src/               # React source code
│   ├── public/            # Static files
│   └── Dockerfile         # Frontend container configuration
└── deploy-with-api-key.sh # Deployment script
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is open source and available under the MIT License.