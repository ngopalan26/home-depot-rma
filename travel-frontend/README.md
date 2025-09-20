# Travel Packing Assistant - Frontend

A React TypeScript application for creating travel plans and viewing intelligent packing suggestions based on weather conditions.

## Features

- **Intuitive Travel Planning**: Easy-to-use form for creating travel plans
- **Weather Integration**: Real-time weather display for each location
- **Smart Packing Suggestions**: Visual display of packing recommendations with priorities
- **Responsive Design**: Works on desktop and mobile devices
- **Date Validation**: Ensures travel duration doesn't exceed 14 days
- **Location Management**: Support for multiple locations per day (up to 3)

## Tech Stack

- **React 18**
- **TypeScript**
- **Material-UI (MUI)**
- **React Router**
- **Axios** for API calls
- **date-fns** for date utilities
- **Day.js** for date picker

## Setup

### Prerequisites
- Node.js 16+ and npm
- Backend service running on port 8081

### Installation

```bash
# Navigate to frontend directory
cd travel-frontend

# Install dependencies
npm install

# Start development server
npm start
```

The application will start on port 3000 and automatically proxy API requests to the backend.

### Environment Variables

Create a `.env` file in the frontend root directory:

```env
REACT_APP_API_URL=http://localhost:8081/api
```

## Usage

### Creating a Travel Plan

1. **Basic Information**:
   - Enter your name
   - Select start and end dates (max 14 days)
   - Dates must be in the future

2. **Daily Locations**:
   - For each day, enter the cities you'll visit
   - Up to 3 locations per day
   - Country is optional but recommended for accuracy

3. **Submit**: Click "Get Packing Suggestions" to create your plan

### Viewing Results

The results page displays:

- **Weather Information**: Temperature, conditions, humidity, wind speed
- **Packing Suggestions**: Organized by priority (High, Medium, Low)
- **Daily Breakdown**: Weather and suggestions for each day
- **Summary View**: Consolidated packing list across all days

## Components

### Pages
- `TravelPlanForm`: Main form for creating travel plans
- `TravelPlanResults`: Results display with weather and packing suggestions

### Components
- `Header`: Navigation header with app branding

### Services
- `api.ts`: API client for backend communication

### Utils
- `dateUtils.ts`: Date formatting and validation utilities

### Types
- TypeScript interfaces for all API data structures

## Styling

- **Material-UI Theme**: Custom theme with primary blue color scheme
- **Responsive Design**: Mobile-first approach with breakpoints
- **Card-based Layout**: Clean, organized information display
- **Priority Indicators**: Color-coded priority levels for packing items

## API Integration

The frontend communicates with the backend through:

- `POST /api/travel/plans` - Create travel plan
- `GET /api/travel/plans/{id}` - Get travel plan results
- `GET /api/travel/health` - Health check

## Error Handling

- Form validation with helpful error messages
- API error handling with user-friendly messages
- Loading states for better user experience
- Graceful fallbacks for missing data

## Development

### Project Structure
```
src/
├── components/    # Reusable UI components
├── pages/        # Page components
├── services/     # API and external services
├── types/        # TypeScript type definitions
├── utils/        # Utility functions
└── App.tsx       # Main application component
```

### Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

## Features in Detail

### Date Validation
- Start date cannot be in the past
- End date cannot be more than 14 days after start date
- Automatic day generation based on selected date range

### Location Input
- City is required, country is optional
- Dynamic add/remove location functionality
- Maximum 3 locations per day validation

### Weather Display
- Temperature in Celsius and Fahrenheit
- Weather condition icons
- Additional weather details (humidity, wind speed)

### Packing Suggestions
- Priority-based organization (High, Medium, Low)
- Quantity recommendations
- Detailed reasoning for each suggestion
- Consolidated summary view

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
# Test automatic deployment - Sat Sep 20 00:14:18 CDT 2025
