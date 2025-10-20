# Correct POST test using OrderUpdateDto format
Write-Host "=== Testing POST with OrderUpdateDto format ===" -ForegroundColor Yellow

$orderUpdate = @{
    id = "ORD-TEST-123"
    trackingNumber = "TRK-TEST-123"
    customerName = "Test Customer"
    status = "CONFIRMED"
    deliveryAddress = "123 Test Street, Test City, TS 12345"
    serviceType = "EXPRESS"
    updatedBy = "API_TEST"
    timestamp = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
} | ConvertTo-Json

Write-Host "Sending OrderUpdateDto JSON:" -ForegroundColor Cyan
Write-Host $orderUpdate -ForegroundColor White

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $orderUpdate -ContentType "application/json"
    Write-Host "Success! Created notification:" -ForegroundColor Green
    Write-Host "- ID: $($response.id)" -ForegroundColor Cyan
    Write-Host "- Order ID: $($response.orderId)" -ForegroundColor Cyan
    Write-Host "- Customer: $($response.customerName)" -ForegroundColor Cyan
    Write-Host "- Message: $($response.message)" -ForegroundColor Cyan
    Write-Host "- Type: $($response.type)" -ForegroundColor Cyan
    Write-Host "- Status: $($response.orderStatus)" -ForegroundColor Cyan
    Write-Host "- Timestamp: $($response.timestamp)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        try {
            $errorResponse = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorResponse)
            $errorContent = $reader.ReadToEnd()
            Write-Host "Error details: $errorContent" -ForegroundColor Red
        } catch {
            Write-Host "Could not read error details" -ForegroundColor Red
        }
    }
}
