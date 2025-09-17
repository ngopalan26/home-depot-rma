export interface LocationRequest {
  city: string;
  state?: string;
  country?: string;
  latitude?: number;
  longitude?: number;
}

export interface TravelDayRequest {
  date: string;
  dayNumber: number;
  locations: LocationRequest[];
}

export interface TravelPlanRequest {
  startDate: string;
  endDate: string;
  travelerName: string;
  travelDays: TravelDayRequest[];
}

export interface WeatherDataResponse {
  id?: number;
  temperatureCelsius: number;
  temperatureFahrenheit: number;
  humidityPercentage?: number;
  windSpeedKmh?: number;
  precipitationMm?: number;
  weatherCondition: string;
  weatherDescription: string;
  feelsLikeCelsius?: number;
  visibilityKm?: number;
  uvIndex?: number;
}

export interface LocationResponse {
  id?: number;
  city: string;
  state?: string;
  country?: string;
  latitude?: number;
  longitude?: number;
  fullName: string;
  weatherData?: WeatherDataResponse;
}

export interface PackingSuggestionResponse {
  id?: number;
  itemName: string;
  itemCategory: string;
  reasoning: string;
  priority: string;
  quantity: number;
}

export interface TravelDayResponse {
  id?: number;
  date: string;
  dayNumber: number;
  locations: LocationResponse[];
  packingSuggestions: PackingSuggestionResponse[];
}

export interface TravelPlanResponse {
  id: number;
  startDate: string;
  endDate: string;
  travelerName: string;
  travelDays: TravelDayResponse[];
  createdAt: string;
  durationInDays: number;
}

export interface ApiError {
  error: string;
  message: string;
  timestamp: string;
}

export interface HealthResponse {
  status: string;
  service: string;
  timestamp: number;
}
