# Quick Test Script for SwiftLogistics Notification Service
# Run this anytime to verify the service is working

Write-Host "🚀 SwiftLogistics Notification Service - Quick Test" -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Yellow

# Test service health
Write-Host "1. Testing Service Health..." -ForegroundColor Cyan
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications/health" -Method GET
    Write-Host "   ✅ Service Status: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Service not responding. Please start the service first." -ForegroundColor Red
    exit
}

# Get notification count
Write-Host "2. Checking Notifications..." -ForegroundColor Cyan
try {
    $notifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET
    Write-Host "   ✅ Total Notifications: $($notifications.Count)" -ForegroundColor Green
    
    # Show summary
    $unread = $notifications | Where-Object { -not $_.isRead }
    Write-Host "   📧 Unread: $($unread.Count)" -ForegroundColor Yellow
} catch {
    Write-Host "   ❌ Failed to retrieve notifications" -ForegroundColor Red
}

# Test creating a new notification
Write-Host "3. Testing POST (Create Notification)..." -ForegroundColor Cyan
$testOrder = @{
    id = "QUICK-TEST-$(Get-Date -Format 'MMddHHmmss')"
    trackingNumber = "TRK-QUICK-$(Get-Date -Format 'MMddHHmmss')"
    customerName = "Quick Test User"
    status = "CONFIRMED"
    deliveryAddress = "123 Test St, Test City"
    serviceType = "EXPRESS"
    updatedBy = "QUICK_TEST"
    timestamp = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
} | ConvertTo-Json

try {
    $newNotification = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method POST -Body $testOrder -ContentType "application/json"
    Write-Host "   ✅ Created notification ID: $($newNotification.id)" -ForegroundColor Green
    Write-Host "   📄 Message: $($newNotification.message)" -ForegroundColor White
} catch {
    Write-Host "   ❌ Failed to create notification: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "🎉 Quick test completed!" -ForegroundColor Green
Write-Host "Service is running at: http://localhost:8083" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Yellow
