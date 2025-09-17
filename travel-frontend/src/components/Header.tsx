import React from 'react';
import { AppBar, Toolbar, Typography, Box } from '@mui/material';
import { FlightTakeoff } from '@mui/icons-material';

const Header: React.FC = () => {
  return (
    <AppBar position="static" sx={{ backgroundColor: 'rgba(25, 118, 210, 0.95)', backdropFilter: 'blur(10px)' }}>
      <Toolbar>
        <FlightTakeoff sx={{ mr: 2, fontSize: 32 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 600 }}>
          Travel Packing Assistant
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Typography variant="body2" sx={{ opacity: 0.9 }}>
            Smart packing based on weather
          </Typography>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
