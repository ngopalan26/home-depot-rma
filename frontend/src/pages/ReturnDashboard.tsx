import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Button,
  Card,
  CardContent,
  Chip,
  Alert,
  CircularProgress,
  IconButton
} from '@mui/material';
import {
  Add as AddIcon,
  Search as SearchIcon,
  ExitToApp as LogoutIcon,
  AssignmentReturn as ReturnIcon
} from '@mui/icons-material';
import { ReturnResponse, ReturnStatus } from '../types';
import { apiService } from '../services/api';

const ReturnDashboard: React.FC = () => {
  const [returns, setReturns] = useState<ReturnResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const customerId = localStorage.getItem('customerId');

  useEffect(() => {
    if (!customerId) {
      navigate('/');
      return;
    }

    loadReturns();
  }, [customerId, navigate]);

  const loadReturns = async () => {
    try {
      setLoading(true);
      const customerReturns = await apiService.getCustomerReturns(customerId!);
      setReturns(customerReturns);
    } catch (err) {
      setError('Failed to load returns');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('customerId');
    navigate('/');
  };

  const getStatusColor = (status: ReturnStatus) => {
    switch (status) {
      case 'PENDING':
        return 'warning';
      case 'APPROVED':
        return 'success';
      case 'COMPLETED':
        return 'primary';
      case 'REJECTED':
        return 'error';
      default:
        return 'default';
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4" component="h1">
          Return Dashboard
        </Typography>
        <IconButton onClick={handleLogout} color="primary">
          <LogoutIcon />
        </IconButton>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}

      <Box display="flex" gap={2} sx={{ mb: 3 }}>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          fullWidth
          size="large"
          onClick={() => navigate('/create-return')}
        >
          Create New Return
        </Button>
        <Button
          variant="outlined"
          startIcon={<SearchIcon />}
          fullWidth
          size="large"
          onClick={() => navigate('/track-return')}
        >
          Track Return
        </Button>
      </Box>

      <Typography variant="h6" component="h2" gutterBottom>
        Recent Returns
      </Typography>

      {returns.length === 0 ? (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <ReturnIcon sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No returns found
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Start by creating a new return request
          </Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => navigate('/create-return')}
          >
            Create Return
          </Button>
        </Paper>
      ) : (
        <Box>
          {returns.map((returnItem) => (
            <Box key={returnItem.rmaNumber} sx={{ mb: 2 }}>
              <Card>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                    <Box>
                      <Typography variant="h6" component="h3">
                        RMA: {returnItem.rmaNumber}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Order: {returnItem.orderNumber}
                      </Typography>
                    </Box>
                    <Chip
                      label={returnItem.status}
                      color={getStatusColor(returnItem.status) as any}
                      size="small"
                    />
                  </Box>

                  <Typography variant="body2" sx={{ mb: 1 }}>
                    <strong>Reason:</strong> {returnItem.reason}
                  </Typography>
                  <Typography variant="body2" sx={{ mb: 1 }}>
                    <strong>Method:</strong> {returnItem.method === 'DROP_OFF_STORE' ? 'Store Drop-off' : 'Ship to Warehouse'}
                  </Typography>
                  <Typography variant="body2" sx={{ mb: 2 }}>
                    <strong>Requested:</strong> {formatDate(returnItem.requestedDate)}
                  </Typography>

                  {returnItem.qrCodeData && (
                    <Box display="flex" alignItems="center" gap={1} mb={1}>
                      <Typography variant="body2" color="success.main">
                        ✓ QR Code Generated
                      </Typography>
                    </Box>
                  )}

                  {returnItem.shippingLabelUrl && (
                    <Box display="flex" alignItems="center" gap={1} mb={1}>
                      <Typography variant="body2" color="success.main">
                        ✓ Shipping Label Ready
                      </Typography>
                    </Box>
                  )}

                  <Button
                    variant="outlined"
                    size="small"
                    onClick={() => navigate(`/track-return?rma=${returnItem.rmaNumber}`)}
                  >
                    View Details
                  </Button>
                </CardContent>
              </Card>
            </Box>
          ))}
        </Box>
      )}
    </Box>
  );
};

export default ReturnDashboard;
