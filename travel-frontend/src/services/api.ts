import axios, { AxiosResponse } from 'axios';
import { 
  TravelPlanRequest, 
  TravelPlanResponse, 
  ApiError, 
  HealthResponse 
} from '../types';

// Determine the API base URL based on the current host
const getApiBaseUrl = () => {
  // For Cloud Run deployment, use the backend URL directly
  if (window.location.hostname.includes('travel-frontend-1090615745845.us-central1.run.app')) {
    return 'https://travel-backend-1090615745845.us-central1.run.app/api';
  }
  
  // Check for environment variable
  if (process.env.REACT_APP_API_BASE_URL) {
    return process.env.REACT_APP_API_BASE_URL;
  }
  
  // Check for local development environment variable
  if (process.env.REACT_APP_API_URL) {
    return process.env.REACT_APP_API_URL;
  }
  
  // If running through ngrok
  if (window.location.hostname.includes('ngrok')) {
    return 'http://localhost:8081/api';
  }
  
  // For local development, use localhost
  return 'http://localhost:8081/api';
};

const API_BASE_URL = getApiBaseUrl();

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging
api.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method?.toUpperCase()} request to ${config.url}`);
    console.log(`Full URL: ${API_BASE_URL}${config.url}`);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.error('Response error:', error);
    if (error.response) {
      // Server responded with error status
      const errorData: ApiError = error.response.data;
      throw new Error(errorData.message || 'An error occurred');
    } else if (error.request) {
      // Request was made but no response received
      throw new Error('Unable to connect to the server. Please check your connection.');
    } else {
      // Something else happened
      throw new Error('An unexpected error occurred');
    }
  }
);

export const travelApi = {
  // Create a new travel plan
  createTravelPlan: async (request: TravelPlanRequest): Promise<TravelPlanResponse> => {
    const response: AxiosResponse<TravelPlanResponse> = await api.post('/travel/plans', request);
    return response.data;
  },

  // Get travel plan by ID
  getTravelPlan: async (id: number): Promise<TravelPlanResponse> => {
    const response: AxiosResponse<TravelPlanResponse> = await api.get(`/travel/plans/${id}`);
    return response.data;
  },

  // Health check
  healthCheck: async (): Promise<HealthResponse> => {
    const response: AxiosResponse<HealthResponse> = await api.get('/travel/health');
    return response.data;
  },
};

export default api;
