export interface Customer {
  id: string;
  customerId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
}

export interface Order {
  id: string;
  orderNumber: string;
  customer: Customer;
  totalAmount: number;
  orderDate: string;
  status: OrderStatus;
  orderItems: OrderItem[];
}

export interface OrderItem {
  id: string;
  productId: string;
  productName: string;
  productDescription?: string;
  sku: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  category: ProductCategory;
  isLargeItem?: boolean;
  isHazardous?: boolean;
}

export interface ReturnRequest {
  orderNumber: string;
  reason: ReturnReason;
  method: ReturnMethod;
  notes?: string;
  returnItems: ReturnItem[];
}

export interface ReturnItem {
  orderItemId: string;
  quantityToReturn: number;
  condition?: string;
  notes?: string;
}

export interface ReturnResponse {
  rmaNumber: string;
  orderNumber: string;
  reason: ReturnReason;
  method: ReturnMethod;
  status: ReturnStatus;
  notes?: string;
  trackingNumber?: string;
  qrCodeData?: string;
  shippingLabelUrl?: string;
  requestedDate: string;
  processedDate?: string;
  completedDate?: string;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export enum ProductCategory {
  TOOLS = 'TOOLS',
  HARDWARE = 'HARDWARE',
  ELECTRICAL = 'ELECTRICAL',
  PLUMBING = 'PLUMBING',
  PAINT = 'PAINT',
  GARDEN = 'GARDEN',
  APPLIANCES = 'APPLIANCES',
  FURNITURE = 'FURNITURE',
  FLOORING = 'FLOORING',
  LIGHTING = 'LIGHTING',
  OUTDOOR = 'OUTDOOR',
  AUTOMOTIVE = 'AUTOMOTIVE',
  STORAGE = 'STORAGE',
  SAFETY = 'SAFETY',
  OTHER = 'OTHER'
}

export enum ReturnReason {
  DEFECTIVE = 'DEFECTIVE',
  DAMAGED = 'DAMAGED',
  WRONG_ITEM = 'WRONG_ITEM',
  NOT_AS_DESCRIBED = 'NOT_AS_DESCRIBED',
  CHANGED_MIND = 'CHANGED_MIND',
  TOO_SMALL = 'TOO_SMALL',
  TOO_LARGE = 'TOO_LARGE',
  ARRIVED_LATE = 'ARRIVED_LATE',
  DUPLICATE_ORDER = 'DUPLICATE_ORDER',
  OTHER = 'OTHER'
}

export enum ReturnMethod {
  SHIP_TO_WAREHOUSE = 'SHIP_TO_WAREHOUSE',
  DROP_OFF_STORE = 'DROP_OFF_STORE'
}

export enum ReturnStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  SHIPPED = 'SHIPPED',
  RECEIVED = 'RECEIVED',
  INSPECTED = 'INSPECTED',
  PROCESSING_REFUND = 'PROCESSING_REFUND',
  COMPLETED = 'COMPLETED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}
