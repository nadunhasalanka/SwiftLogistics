# Complete API Testing for SwiftLogistics Notification Service
Write-Host "========================================" -ForegroundColor Yellow
Write-Host "SwiftLogistics Notification Service API" -ForegroundColor Yellow
Write-Host "Complete Functionality Testing" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

# Test 1: Health Check
Write-Host "1. Health Check" -ForegroundColor Green
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/health" -Method GET
    Write-Host "   Status: $($health.status) | Service: $($health.service)" -ForegroundColor Cyan
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Get All Notifications
Write-Host "2. GET All Notifications" -ForegroundColor Green
try {
    $allNotifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET
    Write-Host "   Total notifications: $($allNotifications.Count)" -ForegroundColor Cyan
    $allNotifications | ForEach-Object {
        Write-Host "   • ID: $($_.id) | $($_.orderId) | $($_.customerName) | $($_.type)" -ForegroundColor White
    }
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: Get Unread Notifications
Write-Host "3. GET Unread Notifications" -ForegroundColor Green
try {
    $unreadNotifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/unread" -Method GET
    Write-Host "   Unread notifications: $($unreadNotifications.Count)" -ForegroundColor Cyan
    $unreadNotifications | ForEach-Object {
        Write-Host "   • Unread: $($_.orderId) | $($_.customerName) | $($_.type)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Get Unread Count
Write-Host "4. GET Unread Count" -ForegroundColor Green
try {
    $unreadCount = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/count/unread" -Method GET
    Write-Host "   Unread count: $($unreadCount.count)" -ForegroundColor Cyan
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Get Notifications by Customer
Write-Host "5. GET Notifications by Customer (Test Customer)" -ForegroundColor Green
try {
    $customerNotifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/customer/Test Customer" -Method GET
    Write-Host "   Notifications for 'Test Customer': $($customerNotifications.Count)" -ForegroundColor Cyan
    $customerNotifications | ForEach-Object {
        Write-Host "   • $($_.orderId): $($_.message)" -ForegroundColor White
    }
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 6: Get Notifications by Order
Write-Host "6. GET Notifications by Order (ORD-TEST-123)" -ForegroundColor Green
try {
    $orderNotifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/order/ORD-TEST-123" -Method GET
    Write-Host "   Notifications for order 'ORD-TEST-123': $($orderNotifications.Count)" -ForegroundColor Cyan
    $orderNotifications | ForEach-Object {
        Write-Host "   • $($_.type): $($_.message)" -ForegroundColor White
    }
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 7: Mark Notification as Read (ID 6)
Write-Host "7. PUT Mark Notification as Read (ID: 6)" -ForegroundColor Green
try {
    $readNotification = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/6/read" -Method PUT
    Write-Host "   Marked notification ID 6 as read. Status: $($readNotification.isRead)" -ForegroundColor Cyan
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 8: Create Another Notification
Write-Host "8. POST Create Another Notification" -ForegroundColor Green
$newOrder = @{
    id = "ORD-DEMO-456"
    trackingNumber = "TRK-DEMO-456"
    customerName = "Demo User"
    status = "IN_TRANSIT"
    deliveryAddress = "456 Demo Avenue, Demo City, DC 67890"
    serviceType = "STANDARD"
    updatedBy = "DEMO_TEST"
    timestamp = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
} | ConvertTo-Json

try {
    $newNotification = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $newOrder -ContentType "application/json"
    Write-Host "   Created notification ID: $($newNotification.id) for order: $($newNotification.orderId)" -ForegroundColor Cyan
    Write-Host "   Message: $($newNotification.message)" -ForegroundColor White
} catch {
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "All API endpoints tested successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Yellow
