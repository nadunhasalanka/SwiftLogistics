import { useState, useEffect, useCallback } from 'react';
import type { Notification } from '../types/notification';
import { notificationService } from '../service/NotificationService';

export function useNotifications() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchNotifications = useCallback(async () => {
    try {
      setLoading(true);
      const [allNotifications, count] = await Promise.all([
        notificationService.getAllNotifications(),
        notificationService.getUnreadCount()
      ]);
      
      setNotifications(allNotifications);
      setUnreadCount(count);
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch notifications');
      console.error('Error fetching notifications:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchUnreadNotifications = useCallback(async () => {
    try {
      const unreadNotifications = await notificationService.getUnreadNotifications();
      return unreadNotifications;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch unread notifications');
      console.error('Error fetching unread notifications:', err);
      return [];
    }
  }, []);

  const markAsRead = useCallback(async (notificationId: number) => {
    try {
      await notificationService.markAsRead(notificationId);
      
      // Update local state
      setNotifications(prev => 
        prev.map(notification => 
          notification.id === notificationId 
            ? { ...notification, isRead: true }
            : notification
        )
      );
      
      // Update unread count
      setUnreadCount(prev => Math.max(0, prev - 1));
      
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to mark notification as read');
      console.error('Error marking notification as read:', err);
    }
  }, []);

  const markAllAsRead = useCallback(async () => {
    try {
      await notificationService.markAllAsRead();
      
      // Update local state
      setNotifications(prev => 
        prev.map(notification => ({ ...notification, isRead: true }))
      );
      
      setUnreadCount(0);
      
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to mark all notifications as read');
      console.error('Error marking all notifications as read:', err);
    }
  }, []);

  const addNotification = useCallback((notification: Notification) => {
    setNotifications(prev => [notification, ...prev]);
    if (!notification.isRead) {
      setUnreadCount(prev => prev + 1);
    }
  }, []);

  useEffect(() => {
    fetchNotifications();
  }, [fetchNotifications]);

  return {
    notifications,
    unreadCount,
    loading,
    error,
    fetchNotifications,
    fetchUnreadNotifications,
    markAsRead,
    markAllAsRead,
    addNotification
  };
}
