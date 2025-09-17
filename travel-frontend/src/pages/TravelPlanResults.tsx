import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  Box,
  Alert,
  CircularProgress,
  Button,
  Grid,
  Card,
  CardContent,
  Chip,
  Divider,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
} from '@mui/material';
import {
  ExpandMore,
  LocationOn,
  Thermostat,
  Water,
  Air,
  WbSunny,
  Cloud,
  AcUnit,
  Umbrella,
  WbCloudy,
} from '@mui/icons-material';
import { travelApi } from '../services/api';
import { TravelPlanResponse } from '../types';
import { formatDisplayDate } from '../utils/dateUtils';

const TravelPlanResults: React.FC = () => {
  const { planId } = useParams<{ planId: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [travelPlan, setTravelPlan] = useState<TravelPlanResponse | null>(null);

  useEffect(() => {
    const fetchTravelPlan = async () => {
      if (!planId) {
        setError('Invalid travel plan ID');
        setLoading(false);
        return;
      }

      try {
        const response = await travelApi.getTravelPlan(parseInt(planId));
        setTravelPlan(response);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load travel plan');
      } finally {
        setLoading(false);
      }
    };

    fetchTravelPlan();
  }, [planId]);

  const getWeatherIcon = (condition: string) => {
    const conditionLower = condition.toLowerCase();
    if (conditionLower.includes('rain') || conditionLower.includes('drizzle')) {
      return <Umbrella />;
    } else if (conditionLower.includes('snow')) {
      return <AcUnit />;
    } else if (conditionLower.includes('sun') || conditionLower.includes('clear')) {
      return <WbSunny />;
    } else if (conditionLower.includes('cloud')) {
      return <WbCloudy />;
    } else {
      return <Cloud />;
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority.toLowerCase()) {
      case 'high':
        return 'error';
      case 'medium':
        return 'warning';
      case 'low':
        return 'success';
      default:
        return 'default';
    }
  };

  const getPriorityIcon = (priority: string) => {
    switch (priority.toLowerCase()) {
      case 'high':
        return '🔴';
      case 'medium':
        return '🟡';
      case 'low':
        return '🟢';
      default:
        return '⚪';
    }
  };

  if (loading) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '50vh' }}>
          <Box sx={{ textAlign: 'center' }}>
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Loading your travel plan...
            </Typography>
          </Box>
        </Box>
      </Container>
    );
  }

  if (error || !travelPlan) {
    return (
      <Container maxWidth="md">
        <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
          <Alert severity="error" sx={{ mb: 3 }}>
            {error || 'Travel plan not found'}
          </Alert>
          <Box sx={{ textAlign: 'center' }}>
            <Button variant="contained" onClick={() => navigate('/')}>
              Create New Travel Plan
            </Button>
          </Box>
        </Paper>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        {/* Header */}
        <Box sx={{ textAlign: 'center', mb: 4 }}>
          <Typography variant="h4" component="h1" gutterBottom color="primary">
            Travel Plan for {travelPlan.travelerName}
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            {formatDisplayDate(travelPlan.startDate)} - {formatDisplayDate(travelPlan.endDate)}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {travelPlan.durationInDays} day{travelPlan.durationInDays > 1 ? 's' : ''} • {travelPlan.travelDays.length} location{travelPlan.travelDays.length > 1 ? 's' : ''}
          </Typography>
        </Box>

        {/* Travel Days */}
        {travelPlan.travelDays.map((day, dayIndex) => (
          <Card key={dayIndex} sx={{ mb: 3 }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Chip
                  label={`Day ${day.dayNumber}`}
                  color="primary"
                  variant="outlined"
                  sx={{ mr: 2 }}
                />
                <Typography variant="h6">
                  {formatDisplayDate(day.date)}
                </Typography>
              </Box>

              <Grid container spacing={3}>
                {/* Locations and Weather */}
                <Grid item xs={12} md={6}>
                  <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center' }}>
                    <LocationOn sx={{ mr: 1 }} />
                    Locations & Weather
                  </Typography>
                  {day.locations.map((location, locationIndex) => (
                    <Box key={locationIndex} sx={{ mb: 2 }}>
                      <Typography variant="subtitle1" fontWeight="bold">
                        {location.fullName}
                      </Typography>
                      {location.weatherData && (
                        <Grid container spacing={1} sx={{ mt: 1 }}>
                          <Grid item xs={6}>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                              <Thermostat sx={{ mr: 1, fontSize: 16 }} />
                              <Typography variant="body2">
                                {Math.round(location.weatherData.temperatureCelsius)}°C
                              </Typography>
                            </Box>
                          </Grid>
                          <Grid item xs={6}>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                              {getWeatherIcon(location.weatherData.weatherCondition)}
                              <Typography variant="body2" sx={{ ml: 1 }}>
                                {location.weatherData.weatherDescription}
                              </Typography>
                            </Box>
                          </Grid>
                          {location.weatherData.humidityPercentage && (
                            <Grid item xs={6}>
                              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <Water sx={{ mr: 1, fontSize: 16 }} />
                                <Typography variant="body2">
                                  {location.weatherData.humidityPercentage}%
                                </Typography>
                              </Box>
                            </Grid>
                          )}
                          {location.weatherData.windSpeedKmh && (
                            <Grid item xs={6}>
                              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <Air sx={{ mr: 1, fontSize: 16 }} />
                                <Typography variant="body2">
                                  {Math.round(location.weatherData.windSpeedKmh)} km/h
                                </Typography>
                              </Box>
                            </Grid>
                          )}
                        </Grid>
                      )}
                      {locationIndex < day.locations.length - 1 && <Divider sx={{ mt: 2 }} />}
                    </Box>
                  ))}
                </Grid>

                {/* Packing Suggestions */}
                <Grid item xs={12} md={6}>
                  <Typography variant="h6" gutterBottom>
                    Clothing Packing Suggestions
                  </Typography>
                  {day.packingSuggestions.length > 0 ? (
                    <List dense>
                      {day.packingSuggestions.map((suggestion, suggestionIndex) => (
                        <ListItem key={suggestionIndex} sx={{ px: 0 }}>
                          <ListItemIcon sx={{ minWidth: 32 }}>
                            <Typography variant="body2">
                              {getPriorityIcon(suggestion.priority)}
                            </Typography>
                          </ListItemIcon>
                          <ListItemText
                            primary={
                              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                <Typography variant="body2" fontWeight="medium">
                                  {suggestion.itemName}
                                </Typography>
                                {suggestion.quantity > 1 && (
                                  <Chip
                                    label={`x${suggestion.quantity}`}
                                    size="small"
                                    variant="outlined"
                                  />
                                )}
                                <Chip
                                  label={suggestion.priority}
                                  size="small"
                                  color={getPriorityColor(suggestion.priority)}
                                  variant="outlined"
                                />
                              </Box>
                            }
                            secondary={
                              <Typography variant="caption" color="text.secondary">
                                {suggestion.reasoning}
                              </Typography>
                            }
                          />
                        </ListItem>
                      ))}
                    </List>
                  ) : (
                    <Typography variant="body2" color="text.secondary">
                      No clothing suggestions available for this day.
                    </Typography>
                  )}
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        ))}

        {/* Summary */}
        <Card sx={{ mt: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Clothing Packing Summary
            </Typography>
            <Accordion>
              <AccordionSummary expandIcon={<ExpandMore />}>
                <Typography>View All Items</Typography>
              </AccordionSummary>
              <AccordionDetails>
                {(() => {
                  const allSuggestions = travelPlan.travelDays.flatMap(day => day.packingSuggestions);
                  const consolidatedItems = new Map<string, { quantity: number; reasoning: string[]; priority: string }>();
                  
                  allSuggestions.forEach(suggestion => {
                    const existing = consolidatedItems.get(suggestion.itemName);
                    if (existing) {
                      existing.quantity += suggestion.quantity;
                      existing.reasoning.push(suggestion.reasoning);
                      if (suggestion.priority === 'HIGH') existing.priority = 'HIGH';
                      else if (suggestion.priority === 'MEDIUM' && existing.priority !== 'HIGH') existing.priority = 'MEDIUM';
                    } else {
                      consolidatedItems.set(suggestion.itemName, {
                        quantity: suggestion.quantity,
                        reasoning: [suggestion.reasoning],
                        priority: suggestion.priority
                      });
                    }
                  });

                  return (
                    <List>
                      {Array.from(consolidatedItems.entries()).map(([itemName, data]) => (
                        <ListItem key={itemName}>
                          <ListItemIcon sx={{ minWidth: 32 }}>
                            <Typography variant="body2">
                              {getPriorityIcon(data.priority)}
                            </Typography>
                          </ListItemIcon>
                          <ListItemText
                            primary={
                              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                <Typography variant="body2" fontWeight="medium">
                                  {itemName}
                                </Typography>
                                {data.quantity > 1 && (
                                  <Chip label={`x${data.quantity}`} size="small" variant="outlined" />
                                )}
                                <Chip
                                  label={data.priority}
                                  size="small"
                                  color={getPriorityColor(data.priority)}
                                  variant="outlined"
                                />
                              </Box>
                            }
                            secondary={data.reasoning.join('; ')}
                          />
                        </ListItem>
                      ))}
                    </List>
                  );
                })()}
              </AccordionDetails>
            </Accordion>
          </CardContent>
        </Card>

        {/* Actions */}
        <Box sx={{ textAlign: 'center', mt: 4 }}>
          <Button variant="contained" onClick={() => navigate('/')}>
            Create Another Travel Plan
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default TravelPlanResults;
