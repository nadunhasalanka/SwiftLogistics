import { useState } from 'react';
import { Package, Search, Filter, Eye, MapPin, Calendar, Zap } from 'lucide-react';
import { useOrders } from '../hooks/useOrders';
import type { Order } from '../types/order';

export function OrdersList() {
  const { orders, loading } = useOrders();
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [serviceFilter, setServiceFilter] = useState<string>('all');
  const [priorityFilter, setPriorityFilter] = useState<string>('all');

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  const filteredOrders = orders.filter(order => {
    const matchesSearch = 
      order.trackingNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.customerEmail.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.pickupAddress.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.deliveryAddress.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesStatus = statusFilter === 'all' || order.status === statusFilter;
    const matchesService = serviceFilter === 'all' || order.serviceType === serviceFilter;
    const matchesPriority = priorityFilter === 'all' || order.priorityLevel === priorityFilter;
    
    return matchesSearch && matchesStatus && matchesService && matchesPriority;
  });

  const getStatusColor = (status: Order['status']) => {
    switch (status) {
      case 'pending':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'confirmed':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'picked_up':
        return 'bg-purple-100 text-purple-800 border-purple-200';
      case 'in_transit':
        return 'bg-orange-100 text-orange-800 border-orange-200';
      case 'delivered':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'cancelled':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getPriorityColor = (priority: Order['priorityLevel']) => {
    switch (priority) {
      case 'urgent':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'high':
        return 'bg-orange-100 text-orange-800 border-orange-200';
      case 'medium':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'low':
        return 'bg-gray-100 text-gray-800 border-gray-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const formatStatus = (status: Order['status']) => {
    return status.split('_').map(word => 
      word.charAt(0).toUpperCase() + word.slice(1)
    ).join(' ');
  };

  const formatPriority = (priority: Order['priorityLevel']) => {
    return priority.charAt(0).toUpperCase() + priority.slice(1);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  const getServiceTypeColor = (serviceType: Order['serviceType']) => {
    switch (serviceType) {
      case 'express':
        return 'bg-red-100 text-red-800';
      case 'standard':
        return 'bg-blue-100 text-blue-800';
      case 'economy':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatAddress = (address: string) => {
    // Extract city/state from the full address string
    const parts = address.split(',');
    if (parts.length >= 2) {
      return parts[parts.length - 2].trim() + ', ' + parts[parts.length - 1].trim();
    }
    return address;
  };

  return (
    <div className="max-w-7xl mx-auto p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">My Orders</h1>
        <p className="mt-2 text-gray-600">Track and manage all your shipments</p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <input
              type="text"
              placeholder="Search orders..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input pl-10"
            />
          </div>

          {/* Status Filter */}
          <div className="relative">
            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="form-input pl-10"
            >
              <option value="all">All Statuses</option>
              <option value="pending">Pending</option>
              <option value="confirmed">Confirmed</option>
              <option value="picked_up">Picked Up</option>
              <option value="in_transit">In Transit</option>
              <option value="delivered">Delivered</option>
              <option value="cancelled">Cancelled</option>
            </select>
          </div>

          {/* Service Filter */}
          <div>
            <select
              value={serviceFilter}
              onChange={(e) => setServiceFilter(e.target.value)}
              className="form-input"
            >
              <option value="all">All Services</option>
              <option value="express">Express</option>
              <option value="standard">Standard</option>
              <option value="economy">Economy</option>
            </select>
          </div>

          {/* Priority Filter */}
          <div>
            <select
              value={priorityFilter}
              onChange={(e) => setPriorityFilter(e.target.value)}
              className="form-input"
            >
              <option value="all">All Priorities</option>
              <option value="urgent">Urgent</option>
              <option value="high">High</option>
              <option value="medium">Medium</option>
              <option value="low">Low</option>
            </select>
          </div>
        </div>
      </div>

      {/* Orders List */}
      {filteredOrders.length === 0 ? (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
          <Package className="mx-auto h-16 w-16 text-gray-400 mb-4" />
          <h3 className="text-xl font-semibold text-gray-900 mb-2">
            {orders.length === 0 ? 'No orders yet' : 'No orders match your filters'}
          </h3>
          <p className="text-gray-600 mb-6">
            {orders.length === 0 
              ? 'Create your first order to start tracking shipments.'
              : 'Try adjusting your search or filter criteria.'
            }
          </p>
          {orders.length === 0 && (
            <a
              href="/create-order"
              className="btn btn-primary inline-flex items-center"
            >
              <Package className="h-4 w-4 mr-2" />
              Create Your First Order
            </a>
          )}
        </div>
      ) : (
        <div className="space-y-4">
          {filteredOrders.map((order) => (
            <div
              key={order.id}
              className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow duration-200"
            >
              <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between">
                {/* Left Section - Main Info */}
                <div className="flex-1 mb-4 lg:mb-0">
                  <div className="flex items-start justify-between mb-3">
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900 flex items-center">
                        <Package className="h-5 w-5 mr-2 text-primary-600" />
                        {order.trackingNumber}
                      </h3>
                      <p className="text-sm text-gray-600">{order.customerName}</p>
                      <p className="text-xs text-gray-500">{order.customerEmail}</p>
                    </div>
                    <div className="flex flex-col gap-2">
                      <div className="flex flex-wrap gap-2">
                        <span
                          className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium border ${getStatusColor(order.status)}`}
                        >
                          {formatStatus(order.status)}
                        </span>
                        <span
                          className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getServiceTypeColor(order.serviceType)}`}
                        >
                          {order.serviceType.toUpperCase()}
                        </span>
                      </div>
                      <span
                        className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium border ${getPriorityColor(order.priorityLevel)}`}
                      >
                        <Zap className="h-3 w-3 mr-1" />
                        {formatPriority(order.priorityLevel)}
                      </span>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                    {/* Pickup Address */}
                    <div className="space-y-2">
                      <div className="flex items-start space-x-2">
                        <MapPin className="h-4 w-4 text-green-600 mt-0.5" />
                        <div>
                          <p className="font-medium text-gray-900">From</p>
                          <p className="text-gray-600">
                            {formatAddress(order.pickupAddress)}
                          </p>
                        </div>
                      </div>
                    </div>

                    {/* Delivery Address */}
                    <div className="space-y-2">
                      <div className="flex items-start space-x-2">
                        <MapPin className="h-4 w-4 text-red-600 mt-0.5" />
                        <div>
                          <p className="font-medium text-gray-900">To</p>
                          <p className="text-gray-600">
                            {formatAddress(order.deliveryAddress)}
                          </p>
                        </div>
                      </div>
                    </div>

                    {/* Created Date */}
                    <div className="space-y-2">
                      <div className="flex items-start space-x-2">
                        <Calendar className="h-4 w-4 text-blue-600 mt-0.5" />
                        <div>
                          <p className="font-medium text-gray-900">Created</p>
                          <p className="text-gray-600">
                            {formatDate(order.createdAt)}
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Right Section - Actions */}
                <div className="lg:ml-6 flex items-center">
                  <button className="btn btn-secondary flex items-center text-sm">
                    <Eye className="h-4 w-4 mr-2" />
                    View Details
                  </button>
                </div>
              </div>

              {/* Package Details */}
              <div className="mt-4 pt-4 border-t border-gray-200">
                <div className="text-sm text-gray-600">
                  <span className="font-medium text-gray-700">Package:</span>{' '}
                  <span className="text-gray-600">{order.packageDescription}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Results Summary */}
      {filteredOrders.length > 0 && (
        <div className="mt-6 text-center text-sm text-gray-600">
          Showing {filteredOrders.length} of {orders.length} orders
        </div>
      )}
    </div>
  );
}