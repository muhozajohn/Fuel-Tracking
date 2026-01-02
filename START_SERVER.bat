@echo off
REM Script to start the backend server on Windows

echo Starting Car Management Backend Server...
echo.

REM Check if port 8080 is in use (try PowerShell first, skip if not available)
where powershell >nul 2>&1
if %errorlevel% == 0 (
    powershell -Command "$connection = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue; if ($connection) { Write-Host '[WARNING] Port 8080 is already in use!'; Write-Host 'Please stop the existing server first using STOP_SERVER.bat'; exit 1 }" >nul 2>&1
    if %errorlevel% == 1 (
        echo [WARNING] Port 8080 is already in use!
        echo Please stop the existing server first using STOP_SERVER.bat
        echo Or press Ctrl+C to cancel and stop the existing server manually.
        pause
        exit /b 1
    )
) else (
    echo Note: Cannot check if port 8080 is in use (netstat/PowerShell not available)
    echo If you get "Address already in use" error, run STOP_SERVER.bat first
    echo.
)

call mvnw.cmd clean compile exec:java -pl backend

