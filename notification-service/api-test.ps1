# SwiftLogistics Notification Service API Test Script
Write-Host "=== Testing SwiftLogistics Notification Service ===" -ForegroundColor Yellow
Write-Host "Base URL: http://localhost:8083" -ForegroundColor Cyan
Write-Host ""

# Test 1: GET all notifications (should be empty initially)
Write-Host "1. Testing GET /api/notifications" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -ContentType "application/json"
    if ($response.Count -eq 0) {
        Write-Host "   Response: (empty array - as expected)" -ForegroundColor Cyan
    } else {
        Write-Host "   Response: $($response.Count) notifications found" -ForegroundColor Cyan
        $response | ConvertTo-Json -Depth 3 | Write-Host
    }
    Write-Host "   Success: GET notifications works" -ForegroundColor Green
} catch {
    Write-Host "   Error: GET notifications failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: POST a new notification
Write-Host "2. Testing POST /api/notifications" -ForegroundColor Green
$newNotification = @{
    orderId = "ORD-12345"
    trackingNumber = "TRK-98765"
    customerName = "John Doe"
    message = "Your order has been confirmed and is being processed"
    type = "ORDER_CONFIRMED"
    orderStatus = "CONFIRMED"
    deliveryAddress = "123 Main St, Cityville, ST 12345"
    serviceType = "EXPRESS_DELIVERY"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $newNotification -ContentType "application/json"
    Write-Host "   Created notification with ID: $($response.id)" -ForegroundColor Cyan
    Write-Host "   Message: $($response.message)" -ForegroundColor Cyan
    Write-Host "   Type: $($response.type)" -ForegroundColor Cyan
    Write-Host "   Status: $($response.orderStatus)" -ForegroundColor Cyan
    Write-Host "   Timestamp: $($response.timestamp)" -ForegroundColor Cyan
    Write-Host "   Success: POST notification works" -ForegroundColor Green
    
    # Store the ID for further testing
    $notificationId = $response.id
} catch {
    Write-Host "   Error: POST notification failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: GET all notifications again (should now have 1)
Write-Host "3. Testing GET /api/notifications (after POST)" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -ContentType "application/json"
    Write-Host "   Response: $($response.Count) notification(s) found" -ForegroundColor Cyan
    if ($response.Count -gt 0) {
        $response | ForEach-Object {
            Write-Host "   - ID: $($_.id), Order: $($_.orderId), Type: $($_.type)" -ForegroundColor Cyan
        }
    }
    Write-Host "   Success: GET notifications after POST works" -ForegroundColor Green
} catch {
    Write-Host "   Error: GET notifications failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Test completed ===" -ForegroundColor Green
