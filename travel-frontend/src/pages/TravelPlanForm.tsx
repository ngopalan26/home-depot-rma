import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Alert,
  CircularProgress,
  Divider,
  Grid,
  Card,
  CardContent,
  IconButton,
  Chip,
  MenuItem,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { Add, Delete, LocationOn } from '@mui/icons-material';
import dayjs, { Dayjs } from 'dayjs';
import { travelApi } from '../services/api';
import { TravelPlanRequest, TravelDayRequest, LocationRequest } from '../types';
import { formatDate, generateDateRange, getMaxEndDate } from '../utils/dateUtils';

// State/Province options by country
const STATES_BY_COUNTRY: Record<string, string[]> = {
  'United States': [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware',
    'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
    'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
    'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico',
    'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania',
    'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
    'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
  ],
  'Canada': [
    'Alberta', 'British Columbia', 'Manitoba', 'New Brunswick', 'Newfoundland and Labrador',
    'Northwest Territories', 'Nova Scotia', 'Nunavut', 'Ontario', 'Prince Edward Island',
    'Quebec', 'Saskatchewan', 'Yukon'
  ],
  'Australia': [
    'Australian Capital Territory', 'New South Wales', 'Northern Territory', 'Queensland',
    'South Australia', 'Tasmania', 'Victoria', 'Western Australia'
  ],
  'Germany': [
    'Baden-Württemberg', 'Bavaria', 'Berlin', 'Brandenburg', 'Bremen', 'Hamburg', 'Hesse',
    'Lower Saxony', 'Mecklenburg-Vorpommern', 'North Rhine-Westphalia', 'Rhineland-Palatinate',
    'Saarland', 'Saxony', 'Saxony-Anhalt', 'Schleswig-Holstein', 'Thuringia'
  ],
  'United Kingdom': [
    'England', 'Scotland', 'Wales', 'Northern Ireland'
  ],
  'India': [
    'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh', 'Goa', 'Gujarat',
    'Haryana', 'Himachal Pradesh', 'Jharkhand', 'Karnataka', 'Kerala', 'Madhya Pradesh',
    'Maharashtra', 'Manipur', 'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab',
    'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura', 'Uttar Pradesh',
    'Uttarakhand', 'West Bengal'
  ]
};

// City options by state
const CITIES_BY_STATE: Record<string, string[]> = {
  // US States
  'California': ['Los Angeles', 'San Francisco', 'San Diego', 'San Jose', 'Fresno', 'Sacramento', 'Long Beach', 'Oakland'],
  'New York': ['New York City', 'Buffalo', 'Rochester', 'Yonkers', 'Syracuse', 'Albany', 'New Rochelle', 'Mount Vernon'],
  'Texas': ['Houston', 'San Antonio', 'Dallas', 'Austin', 'Fort Worth', 'El Paso', 'Arlington', 'Corpus Christi'],
  'Florida': ['Jacksonville', 'Miami', 'Tampa', 'Orlando', 'St. Petersburg', 'Hialeah', 'Tallahassee', 'Fort Lauderdale'],
  'Illinois': ['Chicago', 'Aurora', 'Rockford', 'Joliet', 'Naperville', 'Springfield', 'Peoria', 'Elgin'],
  'Pennsylvania': ['Philadelphia', 'Pittsburgh', 'Allentown', 'Erie', 'Reading', 'Scranton', 'Bethlehem', 'Lancaster'],
  'Ohio': ['Columbus', 'Cleveland', 'Cincinnati', 'Toledo', 'Akron', 'Dayton', 'Parma', 'Canton'],
  'Georgia': ['Atlanta', 'Augusta', 'Columbus', 'Savannah', 'Athens', 'Sandy Springs', 'Roswell', 'Macon'],
  'North Carolina': ['Charlotte', 'Raleigh', 'Greensboro', 'Durham', 'Winston-Salem', 'Fayetteville', 'Cary', 'Wilmington'],
  'Michigan': ['Detroit', 'Grand Rapids', 'Warren', 'Sterling Heights', 'Lansing', 'Ann Arbor', 'Flint', 'Dearborn'],
  
  // Canadian Provinces
  'Ontario': ['Toronto', 'Ottawa', 'Mississauga', 'Hamilton', 'Brampton', 'London', 'Markham', 'Vaughan'],
  'Quebec': ['Montreal', 'Quebec City', 'Laval', 'Gatineau', 'Longueuil', 'Sherbrooke', 'Saguenay', 'Levis'],
  'British Columbia': ['Vancouver', 'Surrey', 'Burnaby', 'Richmond', 'Abbotsford', 'Coquitlam', 'Saanich', 'Delta'],
  'Alberta': ['Calgary', 'Edmonton', 'Red Deer', 'Lethbridge', 'St. Albert', 'Medicine Hat', 'Grande Prairie', 'Airdrie'],
  
  // Australian States
  'New South Wales': ['Sydney', 'Newcastle', 'Wollongong', 'Maitland', 'Albury', 'Wagga Wagga', 'Tamworth', 'Orange'],
  'Victoria': ['Melbourne', 'Geelong', 'Ballarat', 'Bendigo', 'Shepparton', 'Warrnambool', 'Latrobe', 'Traralgon'],
  'Queensland': ['Brisbane', 'Gold Coast', 'Townsville', 'Cairns', 'Toowoomba', 'Rockhampton', 'Mackay', 'Bundaberg'],
  'Western Australia': ['Perth', 'Rockingham', 'Mandurah', 'Bunbury', 'Geraldton', 'Albany', 'Kalgoorlie', 'Broome'],
  
  // German States
  'Bavaria': ['Munich', 'Nuremberg', 'Augsburg', 'Regensburg', 'Würzburg', 'Ingolstadt', 'Fürth', 'Erlangen'],
  'North Rhine-Westphalia': ['Cologne', 'Düsseldorf', 'Dortmund', 'Essen', 'Duisburg', 'Bochum', 'Wuppertal', 'Bielefeld'],
  'Baden-Württemberg': ['Stuttgart', 'Mannheim', 'Karlsruhe', 'Freiburg', 'Heidelberg', 'Heilbronn', 'Ulm', 'Pforzheim'],
  
  // UK Countries
  'England': ['London', 'Birmingham', 'Manchester', 'Liverpool', 'Leeds', 'Sheffield', 'Bristol', 'Nottingham'],
  'Scotland': ['Glasgow', 'Edinburgh', 'Aberdeen', 'Dundee', 'Stirling', 'Perth', 'Inverness', 'Dumfries'],
  'Wales': ['Cardiff', 'Swansea', 'Newport', 'Wrexham', 'Barry', 'Caerphilly', 'Rhondda', 'Port Talbot'],
  
  // Indian States
  'Maharashtra': ['Mumbai', 'Pune', 'Nagpur', 'Nashik', 'Aurangabad', 'Solapur', 'Amravati', 'Kolhapur'],
  'Karnataka': ['Bangalore', 'Mysore', 'Hubli', 'Mangalore', 'Belgaum', 'Gulbarga', 'Davanagere', 'Bellary'],
  'Tamil Nadu': ['Chennai', 'Coimbatore', 'Madurai', 'Tiruchirappalli', 'Salem', 'Tirunelveli', 'Tiruppur', 'Vellore'],
  'Gujarat': ['Ahmedabad', 'Surat', 'Vadodara', 'Rajkot', 'Bhavnagar', 'Junagadh', 'Gandhinagar', 'Nadiad'],
  'West Bengal': ['Kolkata', 'Asansol', 'Siliguri', 'Durgapur', 'Bardhaman', 'Malda', 'Baharampur', 'Habra']
};

// Country options
const COUNTRIES = [
  'United States', 'Canada', 'Australia', 'Germany', 'United Kingdom', 'India',
  'Mexico', 'France', 'Italy', 'Spain', 'Netherlands', 'Belgium', 'Switzerland', 
  'Austria', 'Portugal', 'Ireland', 'Denmark', 'Sweden', 'Norway', 'Finland', 
  'Poland', 'Czech Republic', 'Hungary', 'Greece', 'Turkey', 'Russia',
  'Japan', 'China', 'South Korea', 'Thailand', 'Singapore', 'Malaysia', 
  'Indonesia', 'Philippines', 'Vietnam', 'New Zealand', 'Brazil', 'Argentina', 
  'Chile', 'Peru', 'Colombia', 'South Africa', 'Egypt', 'Morocco', 'Israel', 
  'United Arab Emirates', 'Saudi Arabia', 'Other'
];

const TravelPlanForm: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // Form state
  const [travelerName, setTravelerName] = useState('');
  const [startDate, setStartDate] = useState<Dayjs | null>(dayjs());
  const [endDate, setEndDate] = useState<Dayjs | null>(dayjs().add(3, 'day'));
  const [travelDays, setTravelDays] = useState<TravelDayRequest[]>([]);

  // Initialize travel days when dates change
  React.useEffect(() => {
    if (startDate && endDate) {
      const days = generateDateRange(startDate.toDate(), endDate.toDate());
      const newTravelDays = days.map((date, index) => ({
        date: formatDate(date),
        dayNumber: index + 1,
        locations: [{ city: '', state: '', country: 'United States' }],
      }));
      setTravelDays(newTravelDays);
    }
  }, [startDate, endDate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      // Validate form
      if (!travelerName.trim()) {
        throw new Error('Please enter your name');
      }
      if (!startDate || !endDate) {
        throw new Error('Please select travel dates');
      }
      if (startDate.isAfter(endDate)) {
        throw new Error('End date must be after start date');
      }
      if (endDate.diff(startDate, 'day') > 13) {
        throw new Error('Travel duration cannot exceed 14 days');
      }

      // Validate all locations
      for (const day of travelDays) {
        for (const location of day.locations) {
          if (!location.city.trim()) {
            throw new Error(`Please enter a city for Day ${day.dayNumber}`);
          }
        }
      }

      // Create request
      const request: TravelPlanRequest = {
        startDate: formatDate(startDate.toDate()),
        endDate: formatDate(endDate.toDate()),
        travelerName: travelerName.trim(),
        travelDays: travelDays.map(day => ({
          ...day,
          locations: day.locations.filter(loc => loc.city.trim() !== ''),
        })),
      };

      // Submit request
      const response = await travelApi.createTravelPlan(request);
      
      // Navigate to results
      navigate(`/results/${response.id}`);
      
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred while creating your travel plan');
    } finally {
      setLoading(false);
    }
  };

  const addLocation = (dayIndex: number) => {
    if (travelDays[dayIndex].locations.length < 3) {
      const newTravelDays = [...travelDays];
      newTravelDays[dayIndex].locations.push({ city: '', state: '', country: 'United States' });
      setTravelDays(newTravelDays);
    }
  };

  const removeLocation = (dayIndex: number, locationIndex: number) => {
    const newTravelDays = [...travelDays];
    newTravelDays[dayIndex].locations.splice(locationIndex, 1);
    setTravelDays(newTravelDays);
  };

  const updateLocation = (dayIndex: number, locationIndex: number, field: keyof LocationRequest, value: string) => {
    const newTravelDays = [...travelDays];
    const location = newTravelDays[dayIndex].locations[locationIndex];
    
    // Handle cascading dropdowns
    if (field === 'country') {
      // Reset state and city when country changes
      location.state = '';
      location.city = '';
    } else if (field === 'state') {
      // Reset city when state changes
      location.city = '';
    }
    
    // Update the field
    (location as any)[field] = value;
    setTravelDays(newTravelDays);
  };

  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center" color="primary">
          Create Your Travel Plan
        </Typography>
        <Typography variant="subtitle1" align="center" color="text.secondary" sx={{ mb: 4 }}>
          Enter your travel details and get smart clothing packing suggestions based on weather conditions
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Basic Information */}
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Basic Information
                  </Typography>
                  <Grid container spacing={2}>
                    <Grid item xs={12}>
                      <TextField
                        fullWidth
                        label="Your Name"
                        value={travelerName}
                        onChange={(e) => setTravelerName(e.target.value)}
                        required
                        variant="outlined"
                      />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <DatePicker
                        label="Start Date"
                        value={startDate}
                        onChange={(newValue) => setStartDate(newValue as Dayjs | null)}
                        minDate={dayjs()}
                        maxDate={dayjs().add(1, 'year')}
                        slotProps={{
                          textField: {
                            fullWidth: true,
                            required: true,
                          },
                        }}
                      />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <DatePicker
                        label="End Date"
                        value={endDate}
                        onChange={(newValue) => setEndDate(newValue as Dayjs | null)}
                        minDate={startDate || dayjs()}
                        maxDate={startDate ? getMaxEndDate(startDate.toDate()) : dayjs().add(14, 'day')}
                        slotProps={{
                          textField: {
                            fullWidth: true,
                            required: true,
                          },
                        }}
                      />
                    </Grid>
                  </Grid>
                </CardContent>
              </Card>
            </Grid>

            {/* Travel Days */}
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Daily Locations
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                    Enter the cities you'll visit each day. Include state/province and country for better weather accuracy. You can add up to 3 locations per day.
                  </Typography>

                  {travelDays.map((day, dayIndex) => (
                    <Box key={dayIndex} sx={{ mb: 3 }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <Chip
                          label={`Day ${day.dayNumber}`}
                          color="primary"
                          variant="outlined"
                          sx={{ mr: 2 }}
                        />
                        <Typography variant="body2" color="text.secondary">
                          {dayjs(day.date).format('MMM DD, YYYY')}
                        </Typography>
                      </Box>

                      {day.locations.map((location, locationIndex) => (
                        <Box key={locationIndex} sx={{ mb: 2 }}>
                          <Grid container spacing={2}>
                            <Grid item xs={12} sm={4}>
                              <TextField
                                fullWidth
                                select
                                label="City"
                                value={location.city || ''}
                                onChange={(e) => updateLocation(dayIndex, locationIndex, 'city', e.target.value)}
                                required
                                variant="outlined"
                                size="small"
                                disabled={!location.state || !CITIES_BY_STATE[location.state]}
                              >
                                <MenuItem value="">
                                  <em>Select City</em>
                                </MenuItem>
                                {location.state && CITIES_BY_STATE[location.state] ? 
                                  CITIES_BY_STATE[location.state].map((city: string) => (
                                    <MenuItem key={city} value={city}>
                                      {city}
                                    </MenuItem>
                                  )) : null
                                }
                              </TextField>
                            </Grid>
                            <Grid item xs={12} sm={3}>
                              <TextField
                                fullWidth
                                select
                                label="State/Province"
                                value={location.state || ''}
                                onChange={(e) => updateLocation(dayIndex, locationIndex, 'state', e.target.value)}
                                variant="outlined"
                                size="small"
                                disabled={!location.country || !STATES_BY_COUNTRY[location.country]}
                              >
                                <MenuItem value="">
                                  <em>Select State/Province</em>
                                </MenuItem>
                                {location.country && STATES_BY_COUNTRY[location.country] ? 
                                  STATES_BY_COUNTRY[location.country].map((state: string) => (
                                    <MenuItem key={state} value={state}>
                                      {state}
                                    </MenuItem>
                                  )) : null
                                }
                              </TextField>
                            </Grid>
                            <Grid item xs={12} sm={3}>
                              <TextField
                                fullWidth
                                select
                                label="Country"
                                value={location.country || ''}
                                onChange={(e) => updateLocation(dayIndex, locationIndex, 'country', e.target.value)}
                                variant="outlined"
                                size="small"
                              >
                                <MenuItem value="">
                                  <em>Select Country</em>
                                </MenuItem>
                                {COUNTRIES.map((country) => (
                                  <MenuItem key={country} value={country}>
                                    {country}
                                  </MenuItem>
                                ))}
                              </TextField>
                            </Grid>
                            <Grid item xs={12} sm={2}>
                              <Box sx={{ display: 'flex', gap: 1, height: '100%', alignItems: 'center' }}>
                                {day.locations.length < 3 && (
                                  <IconButton
                                    onClick={() => addLocation(dayIndex)}
                                    color="primary"
                                    size="small"
                                    title="Add another location"
                                  >
                                    <Add />
                                  </IconButton>
                                )}
                                {day.locations.length > 1 && (
                                  <IconButton
                                    onClick={() => removeLocation(dayIndex, locationIndex)}
                                    color="error"
                                    size="small"
                                    title="Remove this location"
                                  >
                                    <Delete />
                                  </IconButton>
                                )}
                              </Box>
                            </Grid>
                          </Grid>
                        </Box>
                      ))}

                      {dayIndex < travelDays.length - 1 && <Divider sx={{ mt: 2 }} />}
                    </Box>
                  ))}
                </CardContent>
              </Card>
            </Grid>

            {/* Submit Button */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
                <Button
                  type="submit"
                  variant="contained"
                  size="large"
                  disabled={loading}
                  sx={{ minWidth: 200, py: 1.5 }}
                >
                  {loading ? (
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <CircularProgress size={20} color="inherit" />
                      Creating Plan...
                    </Box>
                  ) : (
                    <>
                      <LocationOn sx={{ mr: 1 }} />
                      Get Clothing Packing Suggestions
                    </>
                  )}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Container>
  );
};

export default TravelPlanForm;
