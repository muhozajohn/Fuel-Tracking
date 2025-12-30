#!/bin/bash
# Script to stop any server running on port 8080

echo "Stopping server on port 8080..."
echo ""

# Method 1: Kill all Java processes (safest for this project)
echo "Killing Java processes..."
pkill -9 -f "java.*backend" 2>/dev/null
pkill -9 -f "maven.*backend" 2>/dev/null
pkill -9 -f "exec:java.*backend" 2>/dev/null

# Method 2: Try to find process using port 8080
if command -v lsof > /dev/null 2>&1; then
    PID=$(lsof -ti:8080 2>/dev/null)
    if [ -n "$PID" ]; then
        echo "Found process $PID using port 8080"
        kill -9 $PID 2>/dev/null && echo "Stopped process $PID"
    fi
fi

# Method 3: On Windows Git Bash, try using netstat and taskkill
if [ -n "$WINDIR" ]; then
    # Windows environment
    PIDS=$(cmd.exe /c "netstat -ano | findstr :8080" 2>/dev/null | awk '{print $NF}' | sort -u)
    for PID in $PIDS; do
        if [ -n "$PID" ] && [ "$PID" != "0" ]; then
            echo "Found Windows process $PID using port 8080"
            cmd.exe /c "taskkill /F /PID $PID" 2>/dev/null && echo "Stopped process $PID"
        fi
    done
fi

# Method 4: Kill any remaining Java processes that might be servers
JAVA_PIDS=$(ps aux | grep -i "java" | grep -v grep | awk '{print $2}' 2>/dev/null)
if [ -n "$JAVA_PIDS" ]; then
    echo "Checking Java processes..."
    for PID in $JAVA_PIDS; do
        # Check if it's listening on 8080 (rough check)
        if netstat -ano 2>/dev/null | grep -q "$PID.*8080" || \
           lsof -p $PID 2>/dev/null | grep -q "8080"; then
            echo "Killing Java process $PID that may be using port 8080"
            kill -9 $PID 2>/dev/null
        fi
    done
fi

echo ""
echo "Done. Port 8080 should now be free."
echo "Wait 2 seconds, then try starting the server again."

