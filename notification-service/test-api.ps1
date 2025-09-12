# Simple API Test Script for Notification Service
Write-Host "=== Notification Service API Test ===" -ForegroundColor Green
Write-Host ""

# Wait for service to be ready
Start-Sleep -Seconds 2

# Test 1: GET all notifications (should be empty initially)
Write-Host "1. Testing GET /notifications..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -Headers @{"Accept"="application/json"}
    Write-Host "✓ GET request successful" -ForegroundColor Green
    Write-Host "Response: " -NoNewline
    if ($response -eq $null -or $response.Count -eq 0) {
        Write-Host "[] (empty - as expected)" -ForegroundColor Cyan
    } else {
        Write-Host $response -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ GET request failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: POST a new notification
Write-Host "2. Testing POST /notifications..." -ForegroundColor Yellow
$newNotification = @{
    orderId = "ORD001"
    trackingNumber = "TRK123456"
    customerName = "John Doe"
    message = "Your order has been shipped"
    type = "ORDER_IN_TRANSIT"
    orderStatus = "IN_TRANSIT"
    deliveryAddress = "123 Main St, City"
    serviceType = "Express"
} | ConvertTo-Json

try {
    $postResponse = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $newNotification -ContentType "application/json"
    Write-Host "✓ POST request successful" -ForegroundColor Green
    Write-Host "Created notification with ID: $($postResponse.id)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ POST request failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: GET all notifications again (should now have 1 item)
Write-Host "3. Testing GET /notifications again..." -ForegroundColor Yellow
try {
    $response2 = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -Headers @{"Accept"="application/json"}
    Write-Host "✓ GET request successful" -ForegroundColor Green
    Write-Host "Number of notifications: $($response2.Count)" -ForegroundColor Cyan
    if ($response2.Count -gt 0) {
        Write-Host "First notification: " -NoNewline
        Write-Host "$($response2[0].message)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ GET request failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Test completed ===" -ForegroundColor Green
