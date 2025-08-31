import React, { createContext, useContext, useState, ReactNode } from 'react';

export interface Order {
  id: string;
  trackingNumber: string;
  customerName: string;
  customerEmail: string;
  pickupAddress: string;
  deliveryAddress: string;
  packageDescription: string;
  serviceType: 'economy' | 'standard' | 'express';
  priorityLevel: 'low' | 'medium' | 'high' | 'urgent';
  status: 'pending' | 'confirmed' | 'picked_up' | 'in_transit' | 'delivered' | 'cancelled';
  createdAt: string;
  updatedAt: string;
}

interface OrderContextType {
  orders: Order[];
  loading: boolean;
  createOrder: (orderData: Omit<Order, 'id' | 'trackingNumber' | 'status' | 'createdAt' | 'updatedAt'>) => Promise<void>;
  updateOrderStatus: (orderId: string, status: Order['status']) => Promise<void>;
  getOrderById: (orderId: string) => Order | undefined;
}

const OrderContext = createContext<OrderContextType | undefined>(undefined);

export const useOrderContext = () => {
  const context = useContext(OrderContext);
  if (context === undefined) {
    throw new Error('useOrderContext must be used within an OrderProvider');
  }
  return context;
};

// Mock data for development
const mockOrders: Order[] = [
  {
    id: '1',
    trackingNumber: 'SW001234567',
    customerName: 'John Doe',
    customerEmail: 'john@example.com',
    pickupAddress: '123 Main Street, New York, NY 10001, USA',
    deliveryAddress: '456 Oak Avenue, Los Angeles, CA 90210, USA',
    packageDescription: 'Electronics - Laptop',
    serviceType: 'express',
    priorityLevel: 'high',
    status: 'in_transit',
    createdAt: '2024-12-01T10:00:00Z',
    updatedAt: '2024-12-01T14:30:00Z',
  },
  {
    id: '2',
    trackingNumber: 'SW001234568',
    customerName: 'Jane Smith',
    customerEmail: 'jane@example.com',
    pickupAddress: '789 Pine Street, Chicago, IL 60601, USA',
    deliveryAddress: '321 Elm Drive, Miami, FL 33101, USA',
    packageDescription: 'Books and Documents',
    serviceType: 'standard',
    priorityLevel: 'medium',
    status: 'pending',
    createdAt: '2024-12-02T09:15:00Z',
    updatedAt: '2024-12-02T09:15:00Z',
  },
  {
    id: '3',
    trackingNumber: 'SW001234569',
    customerName: 'Mike Johnson',
    customerEmail: 'mike@example.com',
    pickupAddress: '555 Broadway, Seattle, WA 98101, USA',
    deliveryAddress: '777 Market Street, San Francisco, CA 94102, USA',
    packageDescription: 'Clothing and Accessories',
    serviceType: 'economy',
    priorityLevel: 'low',
    status: 'delivered',
    createdAt: '2024-11-28T16:20:00Z',
    updatedAt: '2024-12-01T11:45:00Z',
  },
];

interface OrderProviderProps {
  children: ReactNode;
}

export const OrderProvider: React.FC<OrderProviderProps> = ({ children }) => {
  const [orders, setOrders] = useState<Order[]>(mockOrders);
  const [loading, setLoading] = useState(false);

  const generateTrackingNumber = (): string => {
    const timestamp = Date.now().toString().slice(-6);
    const random = Math.random().toString(36).substring(2, 5).toUpperCase();
    return `SW${timestamp}${random}`;
  };

  const createOrder = async (orderData: Omit<Order, 'id' | 'trackingNumber' | 'status' | 'createdAt' | 'updatedAt'>) => {
    setLoading(true);
    try {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      const newOrder: Order = {
        ...orderData,
        id: (orders.length + 1).toString(),
        trackingNumber: generateTrackingNumber(),
        status: 'pending',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      setOrders(prevOrders => [newOrder, ...prevOrders]);
    } catch (error) {
      throw new Error('Failed to create order');
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (orderId: string, status: Order['status']) => {
    setLoading(true);
    try {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 500));
      
      setOrders(prevOrders =>
        prevOrders.map(order =>
          order.id === orderId
            ? { ...order, status, updatedAt: new Date().toISOString() }
            : order
        )
      );
    } catch (error) {
      throw new Error('Failed to update order status');
    } finally {
      setLoading(false);
    }
  };

  const getOrderById = (orderId: string): Order | undefined => {
    return orders.find(order => order.id === orderId);
  };

  const value: OrderContextType = {
    orders,
    loading,
    createOrder,
    updateOrderStatus,
    getOrderById,
  };

  return <OrderContext.Provider value={value}>{children}</OrderContext.Provider>;
};

export { OrderContext };