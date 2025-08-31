import type { Order, OrderFormData } from "../types/order";

// Simulate API delay
const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

// Generate a random tracking number
const generateTrackingNumber = (): string => {
  const prefix = "SL";
  const number = Math.random().toString(36).substring(2, 15).toUpperCase();
  return `${prefix}${number}`;
};

// Calculate estimated delivery date
const calculateEstimatedDelivery = (serviceType: string): string => {
  const now = new Date();
  let days = 0;

  switch (serviceType) {
    case "express":
      days = 1;
      break;
    case "standard":
      days = 3;
      break;
    case "economy":
      days = 7;
      break;
    default:
      days = 3;
  }

  const deliveryDate = new Date(now.getTime() + days * 24 * 60 * 60 * 1000);
  return deliveryDate.toISOString();
};

// Calculate shipping cost
const calculateCost = (formData: OrderFormData): number => {
  const baseRate = {
    express: 25,
    standard: 15,
    economy: 8,
  };

  const priorityMultiplier = {
    low: 1,
    medium: 1.2,
    high: 1.5,
    urgent: 2,
  };

  const weight = formData.weight;
  const volume = formData.length * formData.width * formData.height;
  const volumeWeight = volume / 5000; // Dimensional weight factor

  const chargeableWeight = Math.max(weight, volumeWeight);
  const baseCost = baseRate[formData.serviceType] * chargeableWeight;
  const finalCost = baseCost * priorityMultiplier[formData.priority];

  return Math.round(finalCost * 100) / 100; // Round to 2 decimal places
};

// Convert form data to order
export const createOrderFromForm = (formData: OrderFormData): Order => {
  const order: Order = {
    id: Date.now().toString(),
    customerName: formData.customerName,
    customerEmail: formData.customerEmail,
    customerPhone: formData.customerPhone,
    pickupAddress: {
      street: formData.pickupStreet,
      city: formData.pickupCity,
      state: formData.pickupState,
      zipCode: formData.pickupZipCode,
      country: formData.pickupCountry,
    },
    deliveryAddress: {
      street: formData.deliveryStreet,
      city: formData.deliveryCity,
      state: formData.deliveryState,
      zipCode: formData.deliveryZipCode,
      country: formData.deliveryCountry,
    },
    packageDetails: {
      weight: formData.weight,
      dimensions: {
        length: formData.length,
        width: formData.width,
        height: formData.height,
      },
      description: formData.description,
      value: formData.value,
    },
    serviceType: formData.serviceType,
    priority: formData.priority,
    status: "pending",
    createdAt: new Date().toISOString(),
    estimatedDelivery: calculateEstimatedDelivery(formData.serviceType),
    trackingNumber: generateTrackingNumber(),
    cost: calculateCost(formData),
  };

  return order;
};

// Local storage functions
export const saveOrder = async (order: Order): Promise<Order> => {
  await delay(500); // Simulate API call

  const existingOrders = getStoredOrders();
  const updatedOrders = [...existingOrders, order];
  localStorage.setItem("swifttrack_orders", JSON.stringify(updatedOrders));

  return order;
};

export const getStoredOrders = (): Order[] => {
  const stored = localStorage.getItem("swifttrack_orders");
  return stored ? JSON.parse(stored) : [];
};

export const getOrderById = (id: string): Order | undefined => {
  const orders = getStoredOrders();
  return orders.find((order) => order.id === id);
};

export const updateOrderStatus = async (
  id: string,
  status: Order["status"]
): Promise<Order | null> => {
  await delay(300);

  const orders = getStoredOrders();
  const orderIndex = orders.findIndex((order) => order.id === id);

  if (orderIndex === -1) return null;

  orders[orderIndex].status = status;
  localStorage.setItem("swifttrack_orders", JSON.stringify(orders));

  return orders[orderIndex];
};

// Generate sample orders for demo
export const generateSampleOrders = (): Order[] => {
  const sampleOrders: Order[] = [
    {
      id: "1",
      customerName: "John Doe",
      customerEmail: "john@example.com",
      customerPhone: "+1-555-0123",
      pickupAddress: {
        street: "123 Main St",
        city: "New York",
        state: "NY",
        zipCode: "10001",
        country: "USA",
      },
      deliveryAddress: {
        street: "456 Oak Ave",
        city: "Los Angeles",
        state: "CA",
        zipCode: "90210",
        country: "USA",
      },
      packageDetails: {
        weight: 2.5,
        dimensions: { length: 30, width: 20, height: 15 },
        description: "Electronics",
        value: 299.99,
      },
      serviceType: "express",
      priority: "high",
      status: "in_transit",
      createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
      estimatedDelivery: new Date(
        Date.now() + 1 * 24 * 60 * 60 * 1000
      ).toISOString(),
      trackingNumber: "SL7K9M2N5P8",
      cost: 89.99,
    },
    {
      id: "2",
      customerName: "Jane Smith",
      customerEmail: "jane@example.com",
      customerPhone: "+1-555-0456",
      pickupAddress: {
        street: "789 Pine St",
        city: "Chicago",
        state: "IL",
        zipCode: "60601",
        country: "USA",
      },
      deliveryAddress: {
        street: "321 Elm St",
        city: "Miami",
        state: "FL",
        zipCode: "33101",
        country: "USA",
      },
      packageDetails: {
        weight: 1.2,
        dimensions: { length: 25, width: 15, height: 10 },
        description: "Books",
        value: 89.99,
      },
      serviceType: "standard",
      priority: "medium",
      status: "delivered",
      createdAt: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
      estimatedDelivery: new Date(
        Date.now() - 1 * 24 * 60 * 60 * 1000
      ).toISOString(),
      trackingNumber: "SL3X8Y1Z4W6",
      cost: 23.5,
    },
  ];

  return sampleOrders;
};
