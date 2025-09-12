# Test script for notification service API
Write-Host "Testing Notification Service API..."

try {
    # Test GET notifications endpoint
    Write-Host "`nTesting GET /api/notifications..."
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET
    Write-Host "GET Response: $($response | ConvertTo-Json -Depth 3)"
    
    # Test POST notification endpoint
    Write-Host "`nTesting POST /api/notifications..."
    $notificationData = @{
        orderId = "ORD-123"
        trackingNumber = "TRK-456"
        customerName = "John Doe"
        message = "Your order has been created successfully"
        type = "ORDER_CREATED"
        orderStatus = "PENDING"
        deliveryAddress = "123 Main St, City"
        serviceType = "Standard Delivery"
    } | ConvertTo-Json
    
    $postResponse = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $notificationData -ContentType "application/json"
    Write-Host "POST Response: $($postResponse | ConvertTo-Json -Depth 3)"
    
    # Test GET notifications again to see the created notification
    Write-Host "`nTesting GET /api/notifications after creating one..."
    $responseAfter = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET
    Write-Host "GET Response After: $($responseAfter | ConvertTo-Json -Depth 3)"
    
    Write-Host "`n✅ All API tests passed successfully!"
} catch {
    Write-Host "❌ Error testing API: $($_.Exception.Message)"
    Write-Host "Stack trace: $($_.ScriptStackTrace)"
}
