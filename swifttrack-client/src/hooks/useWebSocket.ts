import { useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { Notification } from '../types/notification';

interface UseWebSocketOptions {
  onNotification: (notification: Notification) => void;
  onUnreadCountUpdate: (count: number) => void;
  onConnect?: () => void;
  onDisconnect?: () => void;
  onError?: (error: any) => void;
}

export function useWebSocket(options: UseWebSocketOptions) {
  const {
    onNotification,
    onUnreadCountUpdate,
    onConnect,
    onDisconnect,
    onError
  } = options;

  const stompClient = useRef<Client | null>(null);
  const reconnectTimeoutRef = useRef<number | null>(null);
  const isConnected = useRef(false);

  const connect = useCallback(() => {
    try {
      // Create SockJS connection
      const socket = new SockJS('http://localhost:8083/ws');
      
      // Create STOMP client
      stompClient.current = new Client({
        webSocketFactory: () => socket,
        connectHeaders: {},
        debug: (str) => {
          console.log('STOMP Debug:', str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      stompClient.current.onConnect = () => {
        console.log('STOMP connected to notification service');
        isConnected.current = true;
        onConnect?.();
        
        // Subscribe to notification topics
        if (stompClient.current) {
          // Subscribe to general notifications
          stompClient.current.subscribe('/topic/notifications', (message) => {
            try {
              const notification = JSON.parse(message.body) as Notification;
              console.log('Received notification:', notification);
              onNotification(notification);
            } catch (error) {
              console.error('Error parsing notification:', error);
            }
          });

          // Subscribe to unread count updates
          stompClient.current.subscribe('/topic/notifications/count', (message) => {
            try {
              const count = parseInt(message.body, 10);
              console.log('Received unread count update:', count);
              onUnreadCountUpdate(count);
            } catch (error) {
              console.error('Error parsing unread count:', error);
            }
          });
        }
      };

      stompClient.current.onStompError = (frame) => {
        console.error('STOMP error:', frame);
        isConnected.current = false;
        onError?.(frame);
      };

      stompClient.current.onWebSocketClose = () => {
        console.log('STOMP WebSocket closed');
        isConnected.current = false;
        onDisconnect?.();
      };

      // Activate the client
      stompClient.current.activate();

    } catch (error) {
      console.error('Error creating STOMP connection:', error);
      onError?.(error);
    }
  }, [onNotification, onUnreadCountUpdate, onConnect, onDisconnect, onError]);

  const disconnect = useCallback(() => {
    if (reconnectTimeoutRef.current) {
      clearTimeout(reconnectTimeoutRef.current);
      reconnectTimeoutRef.current = null;
    }
    
    if (stompClient.current) {
      stompClient.current.deactivate();
      stompClient.current = null;
      isConnected.current = false;
    }
  }, []);

  const sendMessage = useCallback((destination: string, message: any) => {
    if (stompClient.current && isConnected.current) {
      stompClient.current.publish({
        destination,
        body: JSON.stringify(message)
      });
    } else {
      console.warn('STOMP client is not connected');
    }
  }, []);

  useEffect(() => {
    connect();
    
    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  return {
    sendMessage,
    disconnect,
    reconnect: connect,
    isConnected: isConnected.current
  };
}
