export interface Notification {
  id: number;
  orderId: string;
  trackingNumber: string;
  customerName: string;
  message: string;
  type: NotificationType;
  orderStatus: OrderStatus;
  timestamp: string;
  isRead: boolean;
  deliveryAddress?: string;
  serviceType?: string;
}

export enum NotificationType {
  ORDER_CREATED = 'ORDER_CREATED',
  ORDER_CONFIRMED = 'ORDER_CONFIRMED',
  ORDER_PICKED_UP = 'ORDER_PICKED_UP',
  ORDER_IN_TRANSIT = 'ORDER_IN_TRANSIT',
  ORDER_DELIVERED = 'ORDER_DELIVERED',
  ORDER_CANCELLED = 'ORDER_CANCELLED',
  ORDER_DELAYED = 'ORDER_DELAYED',
  DELIVERY_ATTEMPT_FAILED = 'DELIVERY_ATTEMPT_FAILED'
}

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PICKED_UP = 'PICKED_UP',
  IN_TRANSIT = 'IN_TRANSIT',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}
