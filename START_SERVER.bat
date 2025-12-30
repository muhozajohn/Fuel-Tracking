@echo off
REM Script to start the backend server on Windows

echo Starting Car Management Backend Server...
echo.

REM Check if port 8080 is in use
netstat -ano | findstr :8080 >nul 2>&1
if %errorlevel% == 0 (
    echo [WARNING] Port 8080 is already in use!
    echo Please stop the existing server first using STOP_SERVER.bat
    echo Or press Ctrl+C to cancel and stop the existing server manually.
    pause
    exit /b 1
)

call mvnw.cmd exec:java -pl backend

