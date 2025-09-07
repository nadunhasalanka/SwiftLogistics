export type OrderFormData = {
    clientName: string;
    deliveryAddress: string;
    packageDetails: string;
}

export type OrderResponse = {
    id?: string;
    message?: string;
    status?: string;
}

export type BackendOrder = {
    id: number;
    clientName: string;
    packageDetails: string;
    deliveryAddress: string;
    status: string;
    cmsStatus?: string;
    wmsStatus?: string;
    rosStatus?: string;
    userId: string;
}

export class ApiError extends Error {
    status?: number;
    
    constructor(message: string, status?: number) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
    }
}

class OrderService {
    private readonly baseUrl = 'http://localhost:8080/api/orders';

    private getAuthHeaders(): HeadersInit {
        try {
            // Get the user object from localStorage
            const userStr = localStorage.getItem('user');
            if (userStr) {
                const user = JSON.parse(userStr);
                const token = user.token;
                console.log('Token from user object:', token ? 'Token found' : 'No token in user object');
                console.log('User object:', user);
                
                return {
                    'Content-Type': 'application/json',
                    ...(token && { 'Authorization': `Bearer ${token}` })
                };
            } else {
                console.log('No user object found in localStorage');
            }
        } catch (error) {
            console.error('Error parsing user from localStorage:', error);
        }

        return {
            'Content-Type': 'application/json'
        };
    }

    private async handleResponse<T>(response: Response): Promise<T> {
        console.log('Response status:', response.status);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ 
                message: `Request failed with status: ${response.status}` 
            }));
            console.log('Error data:', errorData);
            throw new ApiError(errorData.message || 'Request failed', response.status);
        }
        return response.json();
    }

    async createOrder(orderData: OrderFormData): Promise<OrderResponse> {
        try {
            console.log('Making request to:', this.baseUrl);
            console.log('Request payload:', orderData);
            console.log('Request headers:', this.getAuthHeaders());
            
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: this.getAuthHeaders(),
                body: JSON.stringify(orderData)
            });

            return await this.handleResponse<OrderResponse>(response);
        } catch (error) {
            console.error('Order creation failed:', error);
            throw error;
        }
    }

    async getOrder(orderId: string): Promise<OrderResponse> {
        try {
            const response = await fetch(`${this.baseUrl}/${orderId}`, {
                method: 'GET',
                headers: this.getAuthHeaders()
            });

            return await this.handleResponse<OrderResponse>(response);
        } catch (error) {
            console.error('Failed to fetch order:', error);
            throw error;
        }
    }

    async getAllOrders(): Promise<BackendOrder[]> {
        try {
            console.log('Fetching orders from:', this.baseUrl);
            const response = await fetch(this.baseUrl, {
                method: 'GET',
                headers: this.getAuthHeaders()
            });

            return await this.handleResponse<BackendOrder[]>(response);
        } catch (error) {
            console.error('Failed to fetch orders:', error);
            throw error;
        }
    }
}

export const orderService = new OrderService();