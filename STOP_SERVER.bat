@echo off
REM Script to stop any server running on port 8080

echo Stopping server on port 8080...
echo.

REM Method 1: Kill processes using port 8080
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Found process %%a using port 8080
    taskkill /F /PID %%a >nul 2>&1
    if errorlevel 1 (
        echo Failed to kill process %%a - may need administrator rights
        echo Try running this script as Administrator
    ) else (
        echo Successfully stopped process %%a
    )
)

REM Method 2: Kill Java processes that might be the server
echo.
echo Checking for Java processes...
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST 2^>nul ^| findstr "PID:"') do (
    for /f "tokens=2 delims=:" %%b in ("%%a") do (
        set PID=%%b
        setlocal enabledelayedexpansion
        echo Checking Java process !PID!...
        netstat -ano 2>nul | findstr "!PID!" | findstr ":8080" >nul
        if not errorlevel 1 (
            echo Killing Java process !PID! using port 8080
            taskkill /F /PID !PID! >nul 2>&1
        )
        endlocal
    )
)

REM Method 3: Kill Maven exec processes
taskkill /F /FI "WINDOWTITLE eq *exec:java*" /T >nul 2>&1

echo.
echo Done. Port 8080 should now be free.
echo Wait 2 seconds, then try starting the server again.
timeout /t 2 >nul

