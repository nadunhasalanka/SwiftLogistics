Write-Host "Testing Notification Service API endpoints..."
Write-Host "Service should be running on http://localhost:8083"
Write-Host ""

# Test GET notifications
try {
    Write-Host "Testing GET /api/notifications..."
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -ContentType "application/json"
    Write-Host "✓ GET /api/notifications - SUCCESS"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 2)"
} catch {
    Write-Host "✗ GET /api/notifications - FAILED: $($_.Exception.Message)"
}

Write-Host ""

# Test POST new notification
try {
    Write-Host "Testing POST /api/notifications..."
    $notification = @{
        orderId = "TEST-001"
        trackingNumber = "TRK-123456"
        customerName = "John Doe"
        message = "Test notification created via API"
        type = "ORDER_CREATED"
        orderStatus = "CONFIRMED"
        serviceType = "EXPRESS"
        deliveryAddress = "123 Test Street, Test City"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $notification -ContentType "application/json"
    Write-Host "✓ POST /api/notifications - SUCCESS"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 2)"
} catch {
    Write-Host "✗ POST /api/notifications - FAILED: $($_.Exception.Message)"
}

Write-Host ""

# Test GET notifications again to see the new one
try {
    Write-Host "Testing GET /api/notifications (after POST)..."
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET -ContentType "application/json"
    Write-Host "✓ GET /api/notifications - SUCCESS"
    Write-Host "Total notifications: $($response.Count)"
    if ($response.Count -gt 0) {
        Write-Host "Latest notification: $($response[0] | ConvertTo-Json -Depth 2)"
    }
} catch {
    Write-Host "✗ GET /api/notifications - FAILED: $($_.Exception.Message)"
}

Write-Host ""
Write-Host "API testing completed!"
