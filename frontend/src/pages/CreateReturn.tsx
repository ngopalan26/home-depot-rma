import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Button,
  Alert,
  CircularProgress,
  Container,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Card,
  CardContent,
  Checkbox,
  FormControlLabel,
  Stepper,
  Step,
  StepLabel,
  Divider
} from '@mui/material';
import Grid from '@mui/material/Grid';
import { ArrowBack as ArrowBackIcon, CheckCircle as CheckCircleIcon } from '@mui/icons-material';
import { Order, OrderItem, ReturnRequest, ReturnReason, ReturnMethod, ReturnResponse } from '../types';
import { apiService } from '../services/api';

const CreateReturn: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [order, setOrder] = useState<Order | null>(null);
  const [selectedItems, setSelectedItems] = useState<Set<string>>(new Set());
  const [returnQuantities, setReturnQuantities] = useState<Record<string, number>>({});
  const [reason, setReason] = useState<ReturnReason | ''>('');
  const [method, setMethod] = useState<ReturnMethod | ''>('');
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState<ReturnResponse | null>(null);
  const [activeStep, setActiveStep] = useState(0);

  const steps = ['Select Items', 'Choose Reason & Method', 'Review & Submit'];

  useEffect(() => {
    if (location.state?.order) {
      setOrder(location.state.order);
    } else {
      navigate('/order-lookup');
    }
  }, [location.state, navigate]);

  const handleItemSelection = (itemId: string, selected: boolean) => {
    const newSelected = new Set(selectedItems);
    if (selected) {
      newSelected.add(itemId);
      const item = order?.orderItems.find(i => i.id === itemId);
      if (item) {
        setReturnQuantities(prev => ({
          ...prev,
          [itemId]: item.quantity
        }));
      }
    } else {
      newSelected.delete(itemId);
      setReturnQuantities(prev => {
        const newQuantities = { ...prev };
        delete newQuantities[itemId];
        return newQuantities;
      });
    }
    setSelectedItems(newSelected);
  };

  const handleQuantityChange = (itemId: string, quantity: number) => {
    setReturnQuantities(prev => ({
      ...prev,
      [itemId]: quantity
    }));
  };

  const handleNext = () => {
    if (activeStep === 0 && selectedItems.size === 0) {
      setError('Please select at least one item to return');
      return;
    }
    if (activeStep === 1 && (!reason || !method)) {
      setError('Please select both reason and return method');
      return;
    }
    setError('');
    setActiveStep(prev => prev + 1);
  };

  const handleBack = () => {
    setActiveStep(prev => prev - 1);
  };

  const handleSubmit = async () => {
    if (!order) return;

    setLoading(true);
    setError('');

    try {
      const returnRequest: ReturnRequest = {
        orderNumber: order.orderNumber,
        reason: reason as ReturnReason,
        method: method as ReturnMethod,
        notes,
        returnItems: Array.from(selectedItems).map(itemId => {
          const item = order.orderItems.find(i => i.id === itemId);
          return {
            orderItemId: itemId,
            quantityToReturn: returnQuantities[itemId] || 1,
            condition: 'Good',
            notes: ''
          };
        })
      };

      const customerId = localStorage.getItem('customerId');
      if (!customerId) {
        navigate('/');
        return;
      }

      const response = await apiService.createReturn(returnRequest, customerId);
      setSuccess(response);
    } catch (err) {
      setError('Failed to create return request. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const getEligibleItems = () => {
    return order?.orderItems.filter(item => !item.isLargeItem && !item.isHazardous) || [];
  };

  if (!order) {
    return (
      <Container maxWidth="md">
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (success) {
    return (
      <Container maxWidth="md">
        <Box sx={{ mt: 4, textAlign: 'center' }}>
          <CheckCircleIcon sx={{ fontSize: 80, color: 'success.main', mb: 2 }} />
          <Typography variant="h4" component="h1" gutterBottom>
            Return Request Created!
          </Typography>
          <Typography variant="h6" color="text.secondary" sx={{ mb: 3 }}>
            RMA Number: {success.rmaNumber}
          </Typography>

          {success.method === 'DROP_OFF_STORE' && success.qrCodeData && (
            <Paper sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                QR Code for Store Return
              </Typography>
              <Box display="flex" justifyContent="center" mb={2}>
                <img
                  src={success.qrCodeData}
                  alt="QR Code"
                  style={{ width: 200, height: 200 }}
                />
              </Box>
              <Typography variant="body2" color="text.secondary">
                Show this QR code to a store associate when dropping off your items.
              </Typography>
            </Paper>
          )}

          {success.method === 'SHIP_TO_WAREHOUSE' && success.shippingLabelUrl && (
            <Paper sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Shipping Label
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Your shipping label has been generated and is ready for download.
              </Typography>
              <Button
                variant="contained"
                href={success.shippingLabelUrl}
                target="_blank"
                rel="noopener noreferrer"
              >
                Download Shipping Label
              </Button>
            </Paper>
          )}

          <Box sx={{ mt: 4 }}>
            <Button
              variant="contained"
              onClick={() => navigate('/dashboard')}
              sx={{ mr: 2 }}
            >
              Back to Dashboard
            </Button>
            <Button
              variant="outlined"
              onClick={() => navigate(`/track-return?rma=${success.rmaNumber}`)}
            >
              Track Return
            </Button>
          </Box>
        </Box>
      </Container>
    );
  }

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
          Create Return Request
        </Typography>

        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Order: {order.orderNumber} • {order.orderItems.length} items
        </Typography>

        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <Paper sx={{ p: 3 }}>
          {activeStep === 0 && (
            <Box>
              <Typography variant="h6" component="h2" gutterBottom>
                Select Items to Return
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                Choose the items you want to return from this order.
              </Typography>

              <Box>
                {getEligibleItems().map((item) => (
                  <Box key={item.id} sx={{ mb: 2 }}>
                    <Card variant="outlined">
                      <CardContent>
                        <Box display="flex" alignItems="flex-start" gap={2}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={selectedItems.has(item.id)}
                                onChange={(e) => handleItemSelection(item.id, e.target.checked)}
                              />
                            }
                            label=""
                          />
                          <Box flex={1}>
                            <Typography variant="h6" component="h3">
                              {item.productName}
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                              SKU: {item.sku}
                            </Typography>
                            <Typography variant="body2" sx={{ mb: 1 }}>
                              Quantity: {item.quantity} × {formatCurrency(item.unitPrice)} = {formatCurrency(item.totalPrice)}
                            </Typography>

                            {selectedItems.has(item.id) && (
                              <Box sx={{ mt: 2 }}>
                                <Typography variant="body2" sx={{ mb: 1 }}>
                                  Quantity to return:
                                </Typography>
                                <TextField
                                  type="number"
                                  size="small"
                                  value={returnQuantities[item.id] || 1}
                                  onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value) || 1)}
                                  inputProps={{ min: 1, max: item.quantity }}
                                  sx={{ width: 120 }}
                                />
                              </Box>
                            )}
                          </Box>
                        </Box>
                      </CardContent>
                    </Card>
                  </Box>
                ))}
              </Box>

              {getEligibleItems().length === 0 && (
                <Alert severity="warning">
                  No items in this order are eligible for self-service returns.
                </Alert>
              )}
            </Box>
          )}

          {activeStep === 1 && (
            <Box>
              <Typography variant="h6" component="h2" gutterBottom>
                Return Details
              </Typography>

              <Box>
                <Box sx={{ mb: 3 }}>
                  <FormControl fullWidth>
                    <InputLabel>Reason for Return</InputLabel>
                    <Select
                      value={reason}
                      onChange={(e) => setReason(e.target.value as ReturnReason)}
                      label="Reason for Return"
                    >
                      <MenuItem value="DEFECTIVE">Defective</MenuItem>
                      <MenuItem value="DAMAGED">Damaged</MenuItem>
                      <MenuItem value="WRONG_ITEM">Wrong Item</MenuItem>
                      <MenuItem value="NOT_AS_DESCRIBED">Not as Described</MenuItem>
                      <MenuItem value="CHANGED_MIND">Changed Mind</MenuItem>
                      <MenuItem value="TOO_SMALL">Too Small</MenuItem>
                      <MenuItem value="TOO_LARGE">Too Large</MenuItem>
                      <MenuItem value="ARRIVED_LATE">Arrived Late</MenuItem>
                      <MenuItem value="DUPLICATE_ORDER">Duplicate Order</MenuItem>
                      <MenuItem value="OTHER">Other</MenuItem>
                    </Select>
                  </FormControl>
                </Box>

                <Box sx={{ mb: 3 }}>
                  <FormControl fullWidth>
                    <InputLabel>Return Method</InputLabel>
                    <Select
                      value={method}
                      onChange={(e) => setMethod(e.target.value as ReturnMethod)}
                      label="Return Method"
                    >
                      <MenuItem value="DROP_OFF_STORE">Drop off at Store</MenuItem>
                      <MenuItem value="SHIP_TO_WAREHOUSE">Ship to Warehouse</MenuItem>
                    </Select>
                  </FormControl>
                </Box>

                <Box>
                  <TextField
                    fullWidth
                    label="Additional Notes (Optional)"
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    multiline
                    rows={3}
                    placeholder="Please provide any additional details about your return..."
                  />
                </Box>
              </Box>
            </Box>
          )}

          {activeStep === 2 && (
            <Box>
              <Typography variant="h6" component="h2" gutterBottom>
                Review Your Return Request
              </Typography>

              <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                Please review your return request before submitting.
              </Typography>

              <Box>
                <Box display="flex" justifyContent="space-between" sx={{ mb: 2 }}>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Order Number
                    </Typography>
                    <Typography variant="body1" fontWeight="bold">
                      {order.orderNumber}
                    </Typography>
                  </Box>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Reason
                    </Typography>
                    <Typography variant="body1">
                      {reason}
                    </Typography>
                  </Box>
                </Box>
                <Box display="flex" justifyContent="space-between" sx={{ mb: 2 }}>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Return Method
                    </Typography>
                    <Typography variant="body1">
                      {method === 'DROP_OFF_STORE' ? 'Store Drop-off' : 'Ship to Warehouse'}
                    </Typography>
                  </Box>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Items to Return
                    </Typography>
                    <Typography variant="body1">
                      {selectedItems.size} item(s)
                    </Typography>
                  </Box>
                </Box>
              </Box>

              <Divider sx={{ my: 3 }} />

              <Typography variant="h6" component="h3" gutterBottom>
                Items Being Returned
              </Typography>

              {Array.from(selectedItems).map((itemId) => {
                const item = order.orderItems.find(i => i.id === itemId);
                if (!item) return null;
                return (
                  <Card key={itemId} variant="outlined" sx={{ mb: 2 }}>
                    <CardContent>
                      <Typography variant="body1" fontWeight="bold">
                        {item.productName}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Quantity to return: {returnQuantities[itemId] || 1}
                      </Typography>
                    </CardContent>
                  </Card>
                );
              })}

              {notes && (
                <>
                  <Divider sx={{ my: 3 }} />
                  <Typography variant="h6" component="h3" gutterBottom>
                    Notes
                  </Typography>
                  <Typography variant="body1">
                    {notes}
                  </Typography>
                </>
              )}
            </Box>
          )}

          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
            <Button
              disabled={activeStep === 0}
              onClick={handleBack}
            >
              Back
            </Button>

            <Box>
              {activeStep === steps.length - 1 ? (
                <Button
                  variant="contained"
                  onClick={handleSubmit}
                  disabled={loading}
                  startIcon={loading ? <CircularProgress size={20} /> : null}
                >
                  {loading ? 'Creating Return...' : 'Submit Return Request'}
                </Button>
              ) : (
                <Button variant="contained" onClick={handleNext}>
                  Next
                </Button>
              )}
            </Box>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default CreateReturn;
