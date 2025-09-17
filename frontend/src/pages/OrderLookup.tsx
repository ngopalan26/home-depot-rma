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
  Container,
  Card,
  CardContent,
  Chip,
  Divider
} from '@mui/material';
import { Search as SearchIcon, ArrowBack as ArrowBackIcon } from '@mui/icons-material';
import { Order, OrderItem, ProductCategory } from '../types';
import { apiService } from '../services/api';

const OrderLookup: React.FC = () => {
  const [orderNumber, setOrderNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [order, setOrder] = useState<Order | null>(null);
  const navigate = useNavigate();

  const handleLookup = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setOrder(null);

    try {
      const foundOrder = await apiService.lookupOrder(orderNumber);
      setOrder(foundOrder);
    } catch (err) {
      setError('Order not found. Please check your order number and try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectOrder = () => {
    if (order) {
      navigate('/create-return', { state: { order } });
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const getCategoryColor = (category: ProductCategory) => {
    const colors: Record<string, string> = {
      'TOOLS': '#f96302',
      'HARDWARE': '#2e7d32',
      'ELECTRICAL': '#1976d2',
      'PLUMBING': '#0288d1',
      'PAINT': '#ed6c02',
      'GARDEN': '#388e3c',
      'APPLIANCES': '#d32f2f',
      'FURNITURE': '#7b1fa2',
      'FLOORING': '#5d4037',
      'LIGHTING': '#f57c00',
      'OUTDOOR': '#689f38',
      'AUTOMOTIVE': '#303f9f',
      'STORAGE': '#455a64',
      'SAFETY': '#d32f2f',
      'OTHER': '#616161'
    };
    return colors[category] || '#616161';
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 4 }}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/dashboard')}
          sx={{ mb: 3 }}
        >
          Back to Dashboard
        </Button>

        <Typography variant="h4" component="h1" gutterBottom>
          Order Lookup
        </Typography>

        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Enter your order number to view order details and start a return
        </Typography>

        <Paper sx={{ p: 3, mb: 3 }}>
          <Box component="form" onSubmit={handleLookup}>
            <TextField
              fullWidth
              label="Order Number"
              value={orderNumber}
              onChange={(e) => setOrderNumber(e.target.value)}
              placeholder="e.g., ORD-2024-001"
              margin="normal"
              required
              disabled={loading}
            />

            {error && (
              <Alert severity="error" sx={{ mt: 2 }}>
                {error}
              </Alert>
            )}

            <Button
              type="submit"
              variant="contained"
              startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
              sx={{ mt: 2 }}
              disabled={loading || !orderNumber}
              fullWidth
            >
              {loading ? 'Looking up order...' : 'Look Up Order'}
            </Button>
          </Box>
        </Paper>

        {order && (
          <Paper sx={{ p: 3 }}>
            <Typography variant="h5" component="h2" gutterBottom>
              Order Details
            </Typography>

            <Box display="flex" flexWrap="wrap" gap={2} sx={{ mb: 3 }}>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Order Number
                </Typography>
                <Typography variant="body1" fontWeight="bold">
                  {order.orderNumber}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Order Date
                </Typography>
                <Typography variant="body1">
                  {formatDate(order.orderDate)}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Total Amount
                </Typography>
                <Typography variant="body1" fontWeight="bold" color="primary.main">
                  {formatCurrency(order.totalAmount)}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Status
                </Typography>
                <Chip label={order.status} color="success" size="small" />
              </Box>
            </Box>

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" component="h3" gutterBottom>
              Order Items
            </Typography>

            <Box>
              {order.orderItems.map((item: OrderItem) => (
                <Box key={item.id} sx={{ mb: 2 }}>
                  <Card variant="outlined">
                    <CardContent>
                      <Box display="flex" justifyContent="space-between" alignItems="flex-start">
                        <Box flex={1}>
                          <Typography variant="h6" component="h4" gutterBottom>
                            {item.productName}
                          </Typography>
                          {item.productDescription && (
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                              {item.productDescription}
                            </Typography>
                          )}
                          <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                            SKU: {item.sku}
                          </Typography>
                          <Box display="flex" gap={1} sx={{ mb: 1 }}>
                            <Chip
                              label={item.category}
                              size="small"
                              sx={{
                                backgroundColor: getCategoryColor(item.category),
                                color: 'white',
                                fontSize: '0.75rem'
                              }}
                            />
                            {item.isLargeItem && (
                              <Chip label="Large Item" size="small" color="warning" />
                            )}
                            {item.isHazardous && (
                              <Chip label="Hazardous" size="small" color="error" />
                            )}
                          </Box>
                        </Box>
                        <Box textAlign="right">
                          <Typography variant="body2" color="text.secondary">
                            Qty: {item.quantity}
                          </Typography>
                          <Typography variant="body1" fontWeight="bold">
                            {formatCurrency(item.unitPrice)} each
                          </Typography>
                          <Typography variant="body1" fontWeight="bold" color="primary.main">
                            {formatCurrency(item.totalPrice)} total
                          </Typography>
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Box>
              ))}
            </Box>

            <Box sx={{ mt: 3, textAlign: 'center' }}>
              <Button
                variant="contained"
                size="large"
                onClick={handleSelectOrder}
                disabled={order.orderItems.some(item => item.isLargeItem || item.isHazardous)}
              >
                Start Return for This Order
              </Button>
              {order.orderItems.some(item => item.isLargeItem || item.isHazardous) && (
                <Alert severity="warning" sx={{ mt: 2 }}>
                  This order contains large or hazardous items that are not eligible for self-service returns.
                  Please contact customer service for assistance.
                </Alert>
              )}
            </Box>
          </Paper>
        )}
      </Box>
    </Container>
  );
};

export default OrderLookup;
