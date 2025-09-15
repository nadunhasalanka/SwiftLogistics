import { useState, useEffect } from "react";
import { orderService,type BackendOrder } from "../service/orderService";

export function useOrders() {
  const [orders, setOrders] = useState<BackendOrder[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      setError(null);
      const ordersData = await orderService.getAllOrders();
      setOrders(ordersData);
    } catch (error) {
      console.error("Failed to fetch orders:", error);
      setError(
        error instanceof Error ? error.message : "Failed to fetch orders"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  return {
    orders,
    loading,
    error,
    refetch: fetchOrders,
  };
}
