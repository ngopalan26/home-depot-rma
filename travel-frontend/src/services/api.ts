import axios, { AxiosResponse } from 'axios';
import { 
  TravelPlanRequest, 
  TravelPlanResponse, 
  ApiError, 
  HealthResponse 
} from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

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
