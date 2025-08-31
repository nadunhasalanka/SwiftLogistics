import { useOrderContext } from "../context/OrderContext";

export const useOrders = () => {
  return useOrderContext();
};
