cd "D:\Middleware\SwiftLogistics\notification-service"; dir target\*.jar

java -jar "D:\Middleware\SwiftLogistics\notification-service\target\notification-service-0.0.1-SNAPSHOT.jar"

Start-Process -FilePath "java" -ArgumentList "-jar","D:\Middleware\SwiftLogistics\notification-service\target\notification-service-0.0.1-SNAPSHOT.jar" -WindowStyle Minimized
Start-Sleep -Seconds 8

cd "D:\Middleware\SwiftLogistics\notification-service"; .\complete-api-test.ps1

Write-Host "=== Final Verification ===" -ForegroundColor Yellow; $notifications = Invoke-RestMethod -Uri "http://localhost:8083/api/notifications" -Method GET; Write-Host "Total notifications: $($notifications.Count)" -ForegroundColor Green; $notifications | ForEach-Object { Write-Host "ID: $($_.id) | $($_.orderId) | $($_.customerName) | $($_.type) | Read: $($_.isRead)" -ForegroundColor Cyan }

# Run the quick test script
.\quick-test.ps1

# For full API testing
.\complete-api-test.ps1