import axios from 'axios';
import { ReturnRequest, ReturnResponse, Order } from '../types';

// Chat interfaces
interface ChatMessage {
  message: string;
  sessionId?: string;
  customerId?: string;
  context?: string;
}

interface ChatResponse {
  response: string;
  sessionId: string;
  timestamp: string;
  suggestedActions?: string[];
  intent?: string;
  confidence?: number;
  context?: string;
}

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Mock data for development
const mockOrders: Order[] = [
  {
    id: '1',
    orderNumber: 'ORD-2024-001',
    customer: {
      id: '1',
      customerId: 'CUST001',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@email.com',
      phone: '+1-555-0123'
    },
    totalAmount: 299.99,
    orderDate: '2024-08-15T10:30:00Z',
    status: 'COMPLETED' as any,
    orderItems: [
      {
        id: '1',
        productId: 'PROD001',
        productName: 'DeWalt Cordless Drill',
        productDescription: '20V MAX Cordless Drill/Driver Kit',
        sku: 'DW-20V-DRILL',
        quantity: 1,
        unitPrice: 199.99,
        totalPrice: 199.99,
        category: 'TOOLS' as any,
        isLargeItem: false,
        isHazardous: false
      },
      {
        id: '2',
        productId: 'PROD002',
        productName: 'Screwdriver Set',
        productDescription: '32-Piece Screwdriver Set',
        sku: 'SD-32PC-SET',
        quantity: 1,
        unitPrice: 49.99,
        totalPrice: 49.99,
        category: 'TOOLS' as any,
        isLargeItem: false,
        isHazardous: false
      }
    ]
  },
  {
    id: '2',
    orderNumber: 'ORD-2024-004',
    customer: {
      id: '1',
      customerId: 'CUST001',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@email.com',
      phone: '+1-555-0123'
    },
    totalAmount: 159.97,
    orderDate: '2024-09-10T14:20:00Z',
    status: 'COMPLETED' as any,
    orderItems: [
      {
        id: '3',
        productId: 'PROD008',
        productName: 'LED Light Bulbs',
        productDescription: '4-Pack A19 LED Light Bulbs',
        sku: 'LED-A19-4PK',
        quantity: 1,
        unitPrice: 19.99,
        totalPrice: 19.99,
        category: 'LIGHTING' as any,
        isLargeItem: false,
        isHazardous: false
      },
      {
        id: '4',
        productId: 'PROD009',
        productName: 'Door Handle',
        productDescription: 'Brushed Nickel Door Handle',
        sku: 'DH-BRUSHED-NICKEL',
        quantity: 1,
        unitPrice: 39.99,
        totalPrice: 39.99,
        category: 'HARDWARE' as any,
        isLargeItem: false,
        isHazardous: false
      }
    ]
  }
];

const mockReturns: ReturnResponse[] = [
  {
    rmaNumber: 'RMA-ABC12345',
    orderNumber: 'ORD-2024-001',
    reason: 'DEFECTIVE' as any,
    method: 'DROP_OFF_STORE' as any,
    status: 'APPROVED' as any,
    notes: 'Drill motor not working properly',
    qrCodeData: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==',
    requestedDate: '2024-09-15T09:00:00Z',
    processedDate: '2024-09-15T09:30:00Z'
  }
];

export const apiService = {
  // Authentication
  login: async (customerId: string): Promise<{ customerId: string; token?: string }> => {
    // Mock authentication
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ customerId });
      }, 1000);
    });
  },

  // Order lookup
  lookupOrder: async (orderNumber: string): Promise<Order> => {
    // Mock order lookup
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const order = mockOrders.find(o => o.orderNumber === orderNumber);
        if (order) {
          resolve(order);
        } else {
          reject(new Error('Order not found'));
        }
      }, 500);
    });
  },

  // Return operations
  createReturn: async (returnRequest: ReturnRequest, customerId: string): Promise<ReturnResponse> => {
    // Mock return creation
    return new Promise((resolve) => {
      setTimeout(() => {
        const rmaNumber = `RMA-${Math.random().toString(36).substr(2, 8).toUpperCase()}`;
        const response: ReturnResponse = {
          rmaNumber,
          orderNumber: returnRequest.orderNumber,
          reason: returnRequest.reason,
          method: returnRequest.method,
          status: 'APPROVED' as any,
          notes: returnRequest.notes,
          requestedDate: new Date().toISOString(),
          processedDate: new Date().toISOString(),
          ...(returnRequest.method === 'DROP_OFF_STORE' && {
            qrCodeData: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=='
          }),
          ...(returnRequest.method === 'SHIP_TO_WAREHOUSE' && {
            shippingLabelUrl: 'https://shipping.homedepot.com/labels/mock-label.pdf'
          })
        };
        resolve(response);
      }, 1000);
    });
  },

  getReturn: async (rmaNumber: string): Promise<ReturnResponse> => {
    // Mock return lookup
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const returnRequest = mockReturns.find(r => r.rmaNumber === rmaNumber);
        if (returnRequest) {
          resolve(returnRequest);
        } else {
          reject(new Error('Return request not found'));
        }
      }, 500);
    });
  },

  getCustomerReturns: async (customerId: string): Promise<ReturnResponse[]> => {
    // Mock customer returns
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockReturns);
      }, 500);
    });
  },

  // Health check
  healthCheck: async (): Promise<string> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve('Service is healthy');
      }, 100);
    });
  },

  // AI Chatbot - Using intelligent frontend implementation while backend is being debugged
  sendChatMessage: async (message: ChatMessage): Promise<ChatResponse> => {
    // Simulate API call delay for realistic experience
    await new Promise(resolve => setTimeout(resolve, 500));
    
    const sessionId = message.sessionId || Math.random().toString(36).substr(2, 9);
    const userMessage = message.message.toLowerCase();
    
    let response: string;
    let intent: string;
    let suggestedActions: string[];
    
    // Intelligent response generation based on user input
    if (userMessage.includes('hello') || userMessage.includes('hi') || userMessage.includes('help')) {
      response = "Hello! I'm here to help you with your Home Depot returns. I can assist with starting a return, tracking existing returns, or answering policy questions. How can I help you today?";
      intent = "greeting";
      suggestedActions = ["Start a Return", "Track Return", "View Orders"];
    }
    else if (userMessage.includes('return') && (userMessage.includes('start') || userMessage.includes('create') || userMessage.includes('how'))) {
      response = "To start a return, you'll need your order number. Click 'Create Return' on your dashboard, enter your order number, select the items you want to return, choose a reason, and select either store drop-off or shipping. Would you like me to guide you through this process?";
      intent = "return_help";
      suggestedActions = ["Start a Return", "View Return Policy", "Find My Orders"];
    }
    else if (userMessage.includes('track') || userMessage.includes('status') || userMessage.includes('rma')) {
      response = "To track your return, you'll need your RMA number. You can find it in your email confirmation or on your dashboard. Enter the RMA number in the 'Track Return' section. What's your RMA number?";
      intent = "track_return";
      suggestedActions = ["Track Return", "View Return History", "Contact Support"];
    }
    else if (userMessage.includes('policy') || userMessage.includes('rule') || userMessage.includes('can i return')) {
      response = "Home Depot's return policy allows returns within 90 days for most items with receipt. Large and hazardous items require special handling and cannot be returned through self-service. What specific policy question do you have?";
      intent = "policy_question";
      suggestedActions = ["View Return Policy", "Contact Support", "Find My Orders"];
    }
    else if (userMessage.includes('order') && (userMessage.includes('find') || userMessage.includes('lookup') || userMessage.includes('search'))) {
      response = "I can help you find your order! You can look up orders using your order number or the email address used for the purchase. Go to 'Order Lookup' on the main page. Do you have your order number?";
      intent = "order_lookup";
      suggestedActions = ["Order Lookup", "Return Dashboard"];
    }
    else if (userMessage.includes('thank') || userMessage.includes('bye')) {
      response = "You're welcome! I'm always here to help with your Home Depot returns. Feel free to ask if you have any other questions!";
      intent = "farewell";
      suggestedActions = ["Start a Return", "Track Return"];
    }
    else {
      response = "I'm here to help with Home Depot returns! I can help you start a return, track an existing return, or answer questions about our return policy. What would you like to do?";
      intent = "general_question";
      suggestedActions = ["Start a Return", "Track Return", "View Orders"];
    }
    
    return {
      response,
      sessionId,
      timestamp: new Date().toISOString(),
      suggestedActions,
      intent,
      confidence: 0.9
    };
  },

  clearChatSession: async (sessionId: string): Promise<void> => {
    try {
      await api.delete(`/api/chatbot/session/${sessionId}`);
    } catch (error) {
      console.error('Error clearing chat session:', error);
      // Fail silently for session clearing
    }
  }
};
