@echo off
REM Force kill all Java processes (use with caution)

echo Killing all Java processes...
echo WARNING: This will stop ALL Java applications!
echo.
pause

taskkill /F /IM java.exe /T 2>nul
taskkill /F /IM javaw.exe /T 2>nul

echo.
echo Done. All Java processes stopped.
pause

