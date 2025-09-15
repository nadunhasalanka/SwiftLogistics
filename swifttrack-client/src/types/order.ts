export interface Order {
  id: number;
  clientName: string;
  packageDetails: string;
  deliveryAddress: string;
  status:
    | "SUBMITTED"
    | "COMPLETED"
    | "FAILED";
  cmsStatus?: string;
  wmsStatus?: string;
  rosStatus?: string;
  userId: string;
  createdAt?: string;
  updatedAt?: string;
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
