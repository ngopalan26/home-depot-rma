import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  Alert,
  CircularProgress,
  Container
} from '@mui/material';
import { Home as HomeIcon } from '@mui/icons-material';
import { apiService } from '../services/api';

const LoginPage: React.FC = () => {
  const [customerId, setCustomerId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await apiService.login(customerId);
      // Store customer ID in localStorage for demo purposes
      localStorage.setItem('customerId', customerId);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid customer ID. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDemoLogin = () => {
    setCustomerId('CUST001');
  };

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          minHeight: '80vh',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Paper
          elevation={3}
          sx={{
            p: 4,
            width: '100%',
            textAlign: 'center',
          }}
        >
          <HomeIcon sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
          
          <Typography variant="h4" component="h1" gutterBottom>
            Home Depot
          </Typography>
          
          <Typography variant="h6" component="h2" gutterBottom color="text.secondary">
            Self-Service Returns
          </Typography>

          <Box component="form" onSubmit={handleLogin} sx={{ mt: 3 }}>
            <TextField
              fullWidth
              label="Customer ID"
              value={customerId}
              onChange={(e) => setCustomerId(e.target.value)}
              margin="normal"
              required
              placeholder="Enter your Customer ID"
              disabled={loading}
            />

            {error && (
              <Alert severity="error" sx={{ mt: 2 }}>
                {error}
              </Alert>
            )}

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2, py: 1.5 }}
              disabled={loading || !customerId}
            >
              {loading ? <CircularProgress size={24} /> : 'Sign In'}
            </Button>

            <Button
              fullWidth
              variant="outlined"
              onClick={handleDemoLogin}
              sx={{ mb: 2 }}
              disabled={loading}
            >
              Use Demo Account (CUST001)
            </Button>
          </Box>

          <Typography variant="body2" color="text.secondary" sx={{ mt: 3 }}>
            Demo Customer IDs: CUST001, CUST002, CUST003
          </Typography>
        </Paper>
      </Box>
    </Container>
  );
};

export default LoginPage;
