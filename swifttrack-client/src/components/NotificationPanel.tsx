import { useState } from 'react';
import { Bell, X, Check, Clock, Package, Truck, CheckCircle } from 'lucide-react';
import { useNotifications } from '../hooks/useNotifications';
import { useWebSocket } from '../hooks/useWebSocket';
import type { Notification, NotificationType } from '../types/notification';

export function NotificationPanel() {
  const [isOpen, setIsOpen] = useState(false);
  const {
    notifications,
    unreadCount,
    loading,
    markAsRead,
    markAllAsRead,
    addNotification
  } = useNotifications();

  // Set up WebSocket connection for real-time notifications
  useWebSocket({
    onNotification: (notification: Notification) => {
      addNotification(notification);
      // You could also show a toast notification here
    },
    onUnreadCountUpdate: (count: number) => {
      console.log('Unread count updated:', count);
    },
    onConnect: () => {
      console.log('Connected to notification service');
    },
    onDisconnect: () => {
      console.log('Disconnected from notification service');
    },
    onError: (error) => {
      console.error('WebSocket error:', error);
    }
  });

  const getNotificationIcon = (type: NotificationType) => {
    switch (type) {
      case 'ORDER_CREATED':
        return <Package className="h-4 w-4 text-blue-500" />;
      case 'ORDER_CONFIRMED':
        return <Check className="h-4 w-4 text-green-500" />;
      case 'ORDER_PICKED_UP':
        return <Truck className="h-4 w-4 text-orange-500" />;
      case 'ORDER_IN_TRANSIT':
        return <Truck className="h-4 w-4 text-purple-500" />;
      case 'ORDER_DELIVERED':
        return <CheckCircle className="h-4 w-4 text-green-600" />;
      case 'ORDER_CANCELLED':
        return <X className="h-4 w-4 text-red-500" />;
      default:
        return <Clock className="h-4 w-4 text-gray-500" />;
    }
  };

  const getNotificationColor = (type: NotificationType) => {
    switch (type) {
      case 'ORDER_CREATED':
        return 'border-l-blue-500';
      case 'ORDER_CONFIRMED':
        return 'border-l-green-500';
      case 'ORDER_PICKED_UP':
        return 'border-l-orange-500';
      case 'ORDER_IN_TRANSIT':
        return 'border-l-purple-500';
      case 'ORDER_DELIVERED':
        return 'border-l-green-600';
      case 'ORDER_CANCELLED':
        return 'border-l-red-500';
      default:
        return 'border-l-gray-500';
    }
  };

  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp);
    const now = new Date();
    const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60));

    if (diffInMinutes < 1) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes}m ago`;
    } else if (diffInMinutes < 1440) {
      return `${Math.floor(diffInMinutes / 60)}h ago`;
    } else {
      return date.toLocaleDateString();
    }
  };

  const handleNotificationClick = async (notification: Notification) => {
    if (!notification.isRead) {
      await markAsRead(notification.id);
    }
  };

  const handleMarkAllAsRead = async () => {
    await markAllAsRead();
  };

  return (
    <div className="relative">
      {/* Notification Bell Button */}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="relative p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
      >
        <Bell className="h-6 w-6" />
        {unreadCount > 0 && (
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
            {unreadCount > 99 ? '99+' : unreadCount}
          </span>
        )}
      </button>

      {/* Notification Panel */}
      {isOpen && (
        <div className="absolute right-0 mt-2 w-96 bg-white rounded-lg shadow-lg border border-gray-200 z-50">
          {/* Header */}
          <div className="flex items-center justify-between p-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Notifications</h3>
            <div className="flex items-center space-x-2">
              {unreadCount > 0 && (
                <button
                  onClick={handleMarkAllAsRead}
                  className="text-sm text-blue-600 hover:text-blue-800 transition-colors"
                >
                  Mark all read
                </button>
              )}
              <button
                onClick={() => setIsOpen(false)}
                className="p-1 text-gray-400 hover:text-gray-600 transition-colors"
              >
                <X className="h-4 w-4" />
              </button>
            </div>
          </div>

          {/* Notifications List */}
          <div className="max-h-96 overflow-y-auto">
            {loading ? (
              <div className="flex items-center justify-center p-6">
                <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
              </div>
            ) : notifications.length === 0 ? (
              <div className="p-6 text-center text-gray-500">
                <Bell className="h-8 w-8 mx-auto mb-2 text-gray-300" />
                <p>No notifications yet</p>
              </div>
            ) : (
              <div className="divide-y divide-gray-100">
                {notifications.map((notification) => (
                  <div
                    key={notification.id}
                    onClick={() => handleNotificationClick(notification)}
                    className={`p-4 hover:bg-gray-50 cursor-pointer transition-colors border-l-4 ${getNotificationColor(notification.type)} ${
                      !notification.isRead ? 'bg-blue-50' : ''
                    }`}
                  >
                    <div className="flex items-start space-x-3">
                      <div className="flex-shrink-0 mt-1">
                        {getNotificationIcon(notification.type)}
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between">
                          <p className="text-sm font-medium text-gray-900 truncate">
                            {notification.trackingNumber}
                          </p>
                          <span className="text-xs text-gray-500">
                            {formatTimestamp(notification.timestamp)}
                          </span>
                        </div>
                        <p className="text-sm text-gray-600 mt-1">
                          {notification.message}
                        </p>
                        <div className="flex items-center mt-2 text-xs text-gray-500">
                          <span className="mr-2">{notification.customerName}</span>
                          {notification.serviceType && (
                            <span className="px-2 py-1 bg-gray-100 rounded-full">
                              {notification.serviceType}
                            </span>
                          )}
                        </div>
                        {!notification.isRead && (
                          <div className="mt-2">
                            <span className="inline-block w-2 h-2 bg-blue-500 rounded-full"></span>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Footer */}
          {notifications.length > 0 && (
            <div className="p-3 border-t border-gray-200 bg-gray-50">
              <button className="w-full text-sm text-blue-600 hover:text-blue-800 transition-colors">
                View all notifications
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
