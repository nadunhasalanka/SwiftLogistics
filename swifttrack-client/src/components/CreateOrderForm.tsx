import { useForm } from 'react-hook-form';
import { useState } from 'react';
import { Package, MapPin, AlertCircle, Zap } from 'lucide-react';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

interface SimpleOrderFormData {
  customerName: string;
  customerEmail: string;
  pickupAddress: string;
  deliveryAddress: string;
  packageDescription: string;
  serviceType: 'economy' | 'standard' | 'express';
  priorityLevel: 'low' | 'medium' | 'high' | 'urgent';
}

export function CreateOrderForm() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<SimpleOrderFormData>({
    defaultValues: {
      serviceType: 'standard',
      priorityLevel: 'medium',
    },
  });

  const onSubmit = async (data: SimpleOrderFormData) => {
    setIsSubmitting(true);
    try {
      // Simple JSON payload for backend
      const orderPayload = {
        customerName: data.customerName,
        customerEmail: data.customerEmail,
        pickupAddress: data.pickupAddress,
        deliveryAddress: data.deliveryAddress,
        packageDescription: data.packageDescription,
        serviceType: data.serviceType,
        priorityLevel: data.priorityLevel
      };

      console.log('Order payload:', orderPayload);
      
      // TODO: Replace with actual API call
      // const response = await fetch('/api/orders', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify(orderPayload)
      // });
      
      toast.success('Order created successfully!');
      reset();
      navigate('/orders');
    } catch {
      toast.error('Failed to create order. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6">
      <div className="bg-white rounded-xl shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h1 className="text-2xl font-bold text-gray-900 flex items-center">
            <Package className="h-6 w-6 mr-2 text-primary-600" />
            Create New Order
          </h1>
          <p className="mt-1 text-sm text-gray-600">
            Fill in the essential details to create a shipping order
          </p>
        </div>

        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-6">
          {/* Customer Name */}
          <div>
            <label className="form-label">Customer Name *</label>
            <input
              type="text"
              {...register('customerName', { required: 'Customer name is required' })}
              className="form-input"
              placeholder="John Doe"
            />
            {errors.customerName && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {errors.customerName.message}
              </p>
            )}
          </div>

          {/* Customer Email */}
          <div>
            <label className="form-label">Customer Email *</label>
            <input
              type="email"
              {...register('customerEmail', {
                required: 'Email is required',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Invalid email address',
                },
              })}
              className="form-input"
              placeholder="john@example.com"
            />
            {errors.customerEmail && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {errors.customerEmail.message}
              </p>
            )}
          </div>

          {/* Pickup Address */}
          <div>
            <label className="form-label flex items-center">
              <MapPin className="h-4 w-4 mr-1 text-green-600" />
              Pickup Address *
            </label>
            <textarea
              {...register('pickupAddress', { required: 'Pickup address is required' })}
              className="form-input"
              rows={3}
              placeholder="123 Main Street, New York, NY 10001, USA"
            />
            {errors.pickupAddress && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {errors.pickupAddress.message}
              </p>
            )}
          </div>

          {/* Delivery Address */}
          <div>
            <label className="form-label flex items-center">
              <MapPin className="h-4 w-4 mr-1 text-red-600" />
              Delivery Address *
            </label>
            <textarea
              {...register('deliveryAddress', { required: 'Delivery address is required' })}
              className="form-input"
              rows={3}
              placeholder="456 Oak Avenue, Los Angeles, CA 90210, USA"
            />
            {errors.deliveryAddress && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {errors.deliveryAddress.message}
              </p>
            )}
          </div>

          {/* Package Description */}
          <div>
            <label className="form-label">Package Description *</label>
            <input
              type="text"
              {...register('packageDescription', { required: 'Package description is required' })}
              className="form-input"
              placeholder="Electronics, Books, Clothing, etc."
            />
            {errors.packageDescription && (
              <p className="mt-1 text-sm text-red-600 flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {errors.packageDescription.message}
              </p>
            )}
          </div>

          {/* Service Type and Priority Level Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Service Type */}
            <div>
              <label className="form-label">Service Type *</label>
              <select {...register('serviceType')} className="form-input">
                <option value="economy">Economy (7-10 days)</option>
                <option value="standard">Standard (3-5 days)</option>
                <option value="express">Express (1-2 days)</option>
              </select>
            </div>

            {/* Priority Level */}
            <div>
              <label className="form-label flex items-center">
                <Zap className="h-4 w-4 mr-1 text-orange-600" />
                Priority Level *
              </label>
              <select {...register('priorityLevel')} className="form-input">
                <option value="low">Low Priority</option>
                <option value="medium">Medium Priority</option>
                <option value="high">High Priority</option>
                <option value="urgent">Urgent</option>
              </select>
            </div>
          </div>

          {/* Submit Button */}
          <div className="flex justify-end space-x-4">
            <button
              type="button"
              onClick={() => reset()}
              className="btn btn-secondary"
              disabled={isSubmitting}
            >
              Reset
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="btn btn-primary flex items-center"
            >
              {isSubmitting ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  Creating...
                </>
              ) : (
                <>
                  <Package className="h-4 w-4 mr-2" />
                  Create Order
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}