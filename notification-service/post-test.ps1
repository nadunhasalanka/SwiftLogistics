# Simple POST test for debugging
Write-Host "=== Testing POST endpoint ===" -ForegroundColor Yellow

$simpleNotification = @{
    orderId = "TEST-001"
    trackingNumber = "TRK-TEST-001"
    customerName = "Test User"
    message = "Test notification message"
    type = "ORDER_CREATED"
    orderStatus = "PENDING"
} | ConvertTo-Json

Write-Host "Sending JSON:" -ForegroundColor Cyan
Write-Host $simpleNotification -ForegroundColor White

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8083/api/notifications" -Method POST -Body $simpleNotification -ContentType "application/json"
    Write-Host "Success! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorResponse = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorResponse)
        $errorContent = $reader.ReadToEnd()
        Write-Host "Error details: $errorContent" -ForegroundColor Red
    }
}
