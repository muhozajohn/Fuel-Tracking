@echo off
REM Script to stop any server running on port 8080

echo Stopping server on port 8080...
echo.

REM Method 1: Kill Java processes (works even without netstat)
echo Killing Java processes...
taskkill /F /IM java.exe /T >nul 2>&1
if %errorlevel% == 0 (
    echo Successfully stopped Java processes
) else (
    echo No Java processes found
)

REM Method 2: Kill javaw processes too
taskkill /F /IM javaw.exe /T >nul 2>&1

REM Method 3: Try to use PowerShell if available (more precise)
where powershell >nul 2>&1
if %errorlevel% == 0 (
    echo Trying PowerShell method...
    powershell -Command "Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }" >nul 2>&1
) else (
    echo PowerShell not available, using Java process kill method
)

echo.
echo Done. Port 8080 should now be free.
echo Wait a moment, then try starting the server again.
REM Use ping as delay alternative (works on all Windows systems)
ping 127.0.0.1 -n 3 >nul 2>&1

