export interface Order {
  id: string;
  trackingNumber: string;
  customerName: string;
  customerEmail: string;
  pickupAddress: string;
  deliveryAddress: string;
  packageDescription: string;
  serviceType: "economy" | "standard" | "express";
  priorityLevel: "low" | "medium" | "high" | "urgent";
  status:
    | "pending"
    | "confirmed"
    | "picked_up"
    | "in_transit"
    | "delivered"
    | "cancelled";
  createdAt: string;
  updatedAt: string;
}

export interface OrderFormData {
  customerName: string;
  customerEmail: string;
  pickupAddress: string;
  deliveryAddress: string;
  packageDescription: string;
  serviceType: "economy" | "standard" | "express";
  priorityLevel: "low" | "medium" | "high" | "urgent";
}

export interface DashboardStats {
  totalOrders: number;
  pendingOrders: number;
  inTransitOrders: number;
  deliveredOrders: number;
}
