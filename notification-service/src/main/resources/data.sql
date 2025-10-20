-- Sample notification data for testing
INSERT INTO notifications (order_id, tracking_number, customer_name, message, type, order_status, timestamp, is_read, delivery_address, service_type) VALUES
('ORD-001', 'TRK-2024-001', 'John Doe', 'Order TRK-2024-001 has been submitted and is pending confirmation.', 'ORDER_CREATED', 'PENDING', '2024-09-06 10:00:00', false, '123 Main St, City, State 12345', 'STANDARD'),
('ORD-002', 'TRK-2024-002', 'Jane Smith', 'Order TRK-2024-002 has been confirmed and is being prepared for pickup.', 'ORDER_CONFIRMED', 'CONFIRMED', '2024-09-06 11:30:00', false, '456 Oak Ave, City, State 67890', 'EXPRESS'),
('ORD-003', 'TRK-2024-003', 'Bob Johnson', 'Order TRK-2024-003 has been picked up from the origin location.', 'ORDER_PICKED_UP', 'PICKED_UP', '2024-09-06 14:15:00', true, '789 Pine Rd, City, State 11111', 'STANDARD'),
('ORD-004', 'TRK-2024-004', 'Alice Brown', 'Order TRK-2024-004 is now in transit to the delivery location.', 'ORDER_IN_TRANSIT', 'IN_TRANSIT', '2024-09-06 16:45:00', false, '321 Elm St, City, State 22222', 'EXPRESS'),
('ORD-005', 'TRK-2024-005', 'Charlie Wilson', 'Order TRK-2024-005 has been successfully delivered!', 'ORDER_DELIVERED', 'DELIVERED', '2024-09-06 18:30:00', true, '654 Maple Dr, City, State 33333', 'STANDARD');
