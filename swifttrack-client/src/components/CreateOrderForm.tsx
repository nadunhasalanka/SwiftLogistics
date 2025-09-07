import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Package, MapPin, AlertCircle, User } from 'lucide-react';
import { orderService,type OrderFormData, ApiError } from '../service/orderService';
import { useNavigate } from 'react-router-dom';

// Note: In a real app, you would have a proper toast notification library configured.
// This is a placeholder for demonstration.
const toast = {
    success: (message: string) => console.log(`SUCCESS: ${message}`),
    error: (message: string) => console.error(`ERROR: ${message}`),
};

export function CreateOrderForm() {
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
        reset,
    } = useForm<OrderFormData>();

    const onSubmit = async (data: OrderFormData) => {
        setIsSubmitting(true);

        try {
            console.log('Submitting order payload:', data);
            
            const response = await orderService.createOrder(data);
            
            toast.success('Order created successfully!');
            console.log('Order response:', response);
            reset();

            navigate('/orders');
            
        } catch (error) {
            console.error('Order creation failed:', error);
            
            if (error instanceof ApiError) {
                toast.error(error.message);
            } else {
                toast.error('An unexpected error occurred while creating the order.');
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    // Basic styling for a standalone component demonstration
    const formLabelClass = "block text-sm font-medium text-gray-700 mb-1";
    const formInputClass = "block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm";
    const btnPrimaryClass = "inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50";
    const btnSecondaryClass = "py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500";

    return (
        <div className="bg-gray-100 min-h-screen flex items-center justify-center p-4">
            <div className="max-w-xl w-full mx-auto">
                <div className="bg-white rounded-xl shadow-md border border-gray-200">
                    <div className="px-6 py-5 border-b border-gray-200">
                        <h1 className="text-xl font-bold text-gray-800 flex items-center">
                            <Package className="h-6 w-6 mr-3 text-indigo-600" />
                            Create New SwiftLogistics Order
                        </h1>
                        <p className="mt-1 text-sm text-gray-500">
                            Enter the details below to dispatch a new package.
                        </p>
                    </div>

                    <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-5">
                        {/* Client Name */}
                        <div>
                            <label htmlFor="clientName" className={formLabelClass + " flex items-center"}>
                                <User className="h-4 w-4 mr-2 text-gray-500" />
                                Client Name *
                            </label>
                            <input
                                id="clientName"
                                type="text"
                                {...register('clientName', { required: 'Client name is required' })}
                                className={formInputClass}
                                placeholder="e.g., Central City Hospital"
                            />
                            {errors.clientName && (
                                <p className="mt-1.5 text-xs text-red-600 flex items-center">
                                    <AlertCircle className="h-4 w-4 mr-1" />
                                    {errors.clientName.message}
                                </p>
                            )}
                        </div>

                        {/* Package Details */}
                        <div>
                            <label htmlFor="packageDetails" className={formLabelClass}>Package Details *</label>
                            <input
                                id="packageDetails"
                                type="text"
                                {...register('packageDetails', { required: 'Package details are required' })}
                                className={formInputClass}
                                placeholder="e.g., Box of sterile surgical gloves"
                            />
                            {errors.packageDetails && (
                                <p className="mt-1.5 text-xs text-red-600 flex items-center">
                                    <AlertCircle className="h-4 w-4 mr-1" />
                                    {errors.packageDetails.message}
                                </p>
                            )}
                        </div>

                        {/* Delivery Address */}
                        <div>
                            <label htmlFor="deliveryAddress" className={formLabelClass + " flex items-center"}>
                                <MapPin className="h-4 w-4 mr-2 text-red-600" />
                                Delivery Address *
                            </label>
                            <textarea
                                id="deliveryAddress"
                                {...register('deliveryAddress', { required: 'Delivery address is required' })}
                                className={formInputClass}
                                rows={3}
                                placeholder="e.g., Surgical Wing, 4th Floor, Loading Dock B, Central City"
                            />
                            {errors.deliveryAddress && (
                                <p className="mt-1.5 text-xs text-red-600 flex items-center">
                                    <AlertCircle className="h-4 w-4 mr-1" />
                                    {errors.deliveryAddress.message}
                                </p>
                            )}
                        </div>

                        {/* Submit Button */}
                        <div className="flex justify-end items-center pt-4 space-x-3">
                            <button
                                type="button"
                                onClick={() => reset()}
                                className={btnSecondaryClass}
                                disabled={isSubmitting}
                            >
                                Reset
                            </button>
                            <button
                                type="submit"
                                disabled={isSubmitting}
                                className={btnPrimaryClass + " min-w-[120px]"}
                            >
                                {isSubmitting ? (
                                    <div className="flex items-center justify-center">
                                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                        Creating...
                                    </div>
                                ) : (
                                    "Create Order"
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}