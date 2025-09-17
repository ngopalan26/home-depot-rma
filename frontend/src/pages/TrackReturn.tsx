import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Button,
  Alert,
  CircularProgress,
  Container,
  TextField,
  Card,
  CardContent,
  Chip,
  Divider
} from '@mui/material';
import {
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineDot
} from '@mui/lab';
import { ArrowBack as ArrowBackIcon, Search as SearchIcon } from '@mui/icons-material';
import { ReturnResponse, ReturnStatus } from '../types';
import { apiService } from '../services/api';

const TrackReturn: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [rmaNumber, setRmaNumber] = useState(searchParams.get('rma') || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [returnData, setReturnData] = useState<ReturnResponse | null>(null);

  useEffect(() => {
    if (rmaNumber) {
      handleTrack();
    }
  }, []);

  const handleTrack = async (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (!rmaNumber) return;

    setLoading(true);
    setError('');
    setReturnData(null);

    try {
      const response = await apiService.getReturn(rmaNumber);
      setReturnData(response);
    } catch (err) {
      setError('Return request not found. Please check your RMA number and try again.');
    } finally {
      setLoading(false);
    }
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

  const getStatusTimeline = (status: ReturnStatus) => {
    const timeline = [
      { status: 'PENDING', label: 'Return Request Submitted', completed: true },
      { status: 'APPROVED', label: 'Return Approved', completed: ['APPROVED', 'SHIPPED', 'RECEIVED', 'INSPECTED', 'PROCESSING_REFUND', 'COMPLETED'].includes(status) },
      { status: 'SHIPPED', label: 'Item Shipped/Received', completed: ['SHIPPED', 'RECEIVED', 'INSPECTED', 'PROCESSING_REFUND', 'COMPLETED'].includes(status) },
      { status: 'RECEIVED', label: 'Item Received', completed: ['RECEIVED', 'INSPECTED', 'PROCESSING_REFUND', 'COMPLETED'].includes(status) },
      { status: 'INSPECTED', label: 'Item Inspected', completed: ['INSPECTED', 'PROCESSING_REFUND', 'COMPLETED'].includes(status) },
      { status: 'PROCESSING_REFUND', label: 'Processing Refund', completed: ['PROCESSING_REFUND', 'COMPLETED'].includes(status) },
      { status: 'COMPLETED', label: 'Return Completed', completed: status === 'COMPLETED' }
    ];

    return timeline.filter(step => 
      step.status === 'PENDING' || 
      step.status === 'APPROVED' || 
      step.status === status ||
      timeline.findIndex(t => t.status === status) >= timeline.findIndex(t => t.status === step.status)
    );
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
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
          Track Return
        </Typography>

        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Enter your RMA number to track the status of your return
        </Typography>

        <Paper sx={{ p: 3, mb: 3 }}>
          <Box component="form" onSubmit={handleTrack}>
            <TextField
              fullWidth
              label="RMA Number"
              value={rmaNumber}
              onChange={(e) => setRmaNumber(e.target.value)}
              placeholder="e.g., RMA-ABC12345"
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
              disabled={loading || !rmaNumber}
              fullWidth
            >
              {loading ? 'Tracking...' : 'Track Return'}
            </Button>
          </Box>
        </Paper>

        {returnData && (
          <Paper sx={{ p: 3 }}>
            <Typography variant="h5" component="h2" gutterBottom>
              Return Details
            </Typography>

            <Box display="flex" flexWrap="wrap" gap={2} sx={{ mb: 3 }}>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  RMA Number
                </Typography>
                <Typography variant="body1" fontWeight="bold">
                  {returnData.rmaNumber}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Order Number
                </Typography>
                <Typography variant="body1" fontWeight="bold">
                  {returnData.orderNumber}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Status
                </Typography>
                <Chip
                  label={returnData.status}
                  color={getStatusColor(returnData.status) as any}
                  size="small"
                />
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Return Method
                </Typography>
                <Typography variant="body1">
                  {returnData.method === 'DROP_OFF_STORE' ? 'Store Drop-off' : 'Ship to Warehouse'}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Reason
                </Typography>
                <Typography variant="body1">
                  {returnData.reason}
                </Typography>
              </Box>
              <Box flex="1" minWidth="200px">
                <Typography variant="body2" color="text.secondary">
                  Requested Date
                </Typography>
                <Typography variant="body1">
                  {formatDate(returnData.requestedDate)}
                </Typography>
              </Box>
            </Box>

            {returnData.notes && (
              <>
                <Divider sx={{ my: 2 }} />
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Notes
                </Typography>
                <Typography variant="body1">
                  {returnData.notes}
                </Typography>
              </>
            )}

            {returnData.trackingNumber && (
              <>
                <Divider sx={{ my: 2 }} />
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Tracking Number
                </Typography>
                <Typography variant="body1" fontWeight="bold">
                  {returnData.trackingNumber}
                </Typography>
              </>
            )}

            <Divider sx={{ my: 3 }} />

            <Typography variant="h6" component="h3" gutterBottom>
              Return Progress
            </Typography>

            <Timeline>
              {getStatusTimeline(returnData.status).map((step, index) => (
                <TimelineItem key={step.status}>
                  <TimelineSeparator>
                    <TimelineDot
                      color={step.completed ? 'primary' : 'grey'}
                      variant={step.completed ? 'filled' : 'outlined'}
                    />
                    {index < getStatusTimeline(returnData.status).length - 1 && (
                      <TimelineConnector />
                    )}
                  </TimelineSeparator>
                  <TimelineContent>
                    <Typography
                      variant="body1"
                      color={step.completed ? 'primary' : 'text.secondary'}
                      fontWeight={step.completed ? 'bold' : 'normal'}
                    >
                      {step.label}
                    </Typography>
                    {step.completed && (
                      <Typography variant="body2" color="text.secondary">
                        {step.status === 'PENDING' && formatDate(returnData.requestedDate)}
                        {step.status === 'APPROVED' && returnData.processedDate && formatDate(returnData.processedDate)}
                        {step.status === 'COMPLETED' && returnData.completedDate && formatDate(returnData.completedDate)}
                      </Typography>
                    )}
                  </TimelineContent>
                </TimelineItem>
              ))}
            </Timeline>

            {returnData.qrCodeData && (
              <>
                <Divider sx={{ my: 3 }} />
                <Typography variant="h6" component="h3" gutterBottom>
                  Store Return QR Code
                </Typography>
                <Box display="flex" justifyContent="center" mb={2}>
                  <img
                    src={returnData.qrCodeData}
                    alt="QR Code"
                    style={{ width: 200, height: 200 }}
                  />
                </Box>
                <Typography variant="body2" color="text.secondary" textAlign="center">
                  Show this QR code to a store associate when dropping off your items.
                </Typography>
              </>
            )}

            {returnData.shippingLabelUrl && (
              <>
                <Divider sx={{ my: 3 }} />
                <Typography variant="h6" component="h3" gutterBottom>
                  Shipping Label
                </Typography>
                <Button
                  variant="contained"
                  href={returnData.shippingLabelUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Download Shipping Label
                </Button>
              </>
            )}

            <Box sx={{ mt: 4, textAlign: 'center' }}>
              <Button
                variant="contained"
                onClick={() => navigate('/dashboard')}
                sx={{ mr: 2 }}
              >
                Back to Dashboard
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/create-return')}
              >
                Create Another Return
              </Button>
            </Box>
          </Paper>
        )}
      </Box>
    </Container>
  );
};

export default TrackReturn;
