import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { Container, AppBar, Toolbar, Typography, Box } from '@mui/material';
import { orange } from '@mui/material/colors';

import LoginPage from './pages/LoginPage';
import ReturnDashboard from './pages/ReturnDashboard';
import CreateReturn from './pages/CreateReturn';
import TrackReturn from './pages/TrackReturn';
import OrderLookup from './pages/OrderLookup';

const theme = createTheme({
  palette: {
    primary: {
      main: '#f96302', // Home Depot Orange
    },
    secondary: {
      main: '#ffffff', // White
    },
    background: {
      default: '#f5f5f5',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Box sx={{ flexGrow: 1 }}>
          <AppBar position="static" sx={{ backgroundColor: '#f96302' }}>
            <Toolbar>
              <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                Home Depot Self-Service Returns
              </Typography>
            </Toolbar>
          </AppBar>
          
          <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Routes>
              <Route path="/" element={<LoginPage />} />
              <Route path="/dashboard" element={<ReturnDashboard />} />
              <Route path="/create-return" element={<CreateReturn />} />
              <Route path="/track-return" element={<TrackReturn />} />
              <Route path="/order-lookup" element={<OrderLookup />} />
            </Routes>
          </Container>
        </Box>
      </Router>
    </ThemeProvider>
  );
}

export default App;