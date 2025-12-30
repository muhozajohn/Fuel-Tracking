#!/bin/bash
# Script to start the backend server

echo "Starting Car Management Backend Server..."
echo ""

# Check if port 8080 is in use
if command -v lsof > /dev/null; then
    if lsof -ti:8080 > /dev/null 2>&1; then
        echo "[WARNING] Port 8080 is already in use!"
        echo "Please stop the existing server first using ./STOP_SERVER.sh"
        exit 1
    fi
fi

./mvnw exec:java -pl backend

