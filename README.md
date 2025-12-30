# Car Management & Fuel Tracking System

A Java-based system for managing cars and tracking fuel consumption with a REST API backend and CLI client.

---

## 🚀 Quick Start

### Start the Server

**On Linux/Mac/Git Bash:**
```bash
./mvnw exec:java -pl backend
```

**On Windows (CMD/PowerShell):**
```cmd
mvnw.cmd exec:java -pl backend
```

**Or use the helper script:**
```bash
./START_SERVER.sh    # Linux/Mac/Git Bash
START_SERVER.bat     # Windows
```

The server will start on **http://localhost:8080**

**Note:** First time will download Maven automatically (~10MB, one-time only)

### Test the Server

```bash
curl http://localhost:8080/api/cars
```

You should see `[]` (empty array) if the server is running.

### ⚠️ Troubleshooting: Port 8080 Already in Use

If you see `Address already in use: bind`, stop the existing server:

**Windows (CMD):**
```cmd
STOP_SERVER.bat
```

**Git Bash/Linux/Mac:**
```bash
./STOP_SERVER.sh
```

---

## 📁 Project Structure

```
challenge/
├── backend/                    # Backend REST API & Servlet
│   ├── src/main/java/
│   │   └── com/aem/academy/backend/
│   │       ├── api/           # REST API handlers & DTOs
│   │       ├── http/          # HTTP request routing
│   │       ├── model/          # Domain models (Car, FuelEntry, FuelStats)
│   │       ├── service/       # Business logic (CarService)
│   │       ├── servlet/        # Manual servlet implementation
│   │       └── Server.java     # Main server class
│   └── pom.xml
├── cli/                        # CLI client application
│   ├── src/main/java/
│   │   └── com/aem/academy/cli/
│   │       ├── model/          # Response models
│   │       └── CliApplication.java
│   └── pom.xml
├── mvnw / mvnw.cmd            # Maven wrapper (no Maven install needed)
├── START_SERVER.sh / .bat     # Helper scripts to start server
├── STOP_SERVER.sh / .bat      # Helper scripts to stop server
├── pom.xml                    # Parent POM
└── README.md                  # This file
```

---

## 📋 Prerequisites

- **Java 17 or higher** (Java 21 recommended)
- **No Maven installation needed** - Maven wrapper included!

---

## 🔨 Building the Project

**No Maven installation required!** Use the Maven wrapper:

From the root directory:

```bash
./mvnw clean install
```

Or on Windows (CMD/PowerShell):
```cmd
mvnw.cmd clean install
```

---

## 🌐 Backend API Endpoints

### REST API

- **POST** `/api/cars` - Create a new car
  - Request body: `{"brand": "Toyota", "model": "Corolla", "year": 2018}`
  
- **GET** `/api/cars` - List all cars

- **POST** `/api/cars/{id}/fuel` - Add fuel entry for a car
  - Request body: `{"liters": 40.0, "price": 52.5, "odometer": 45000}`

- **GET** `/api/cars/{id}/fuel/stats` - Get fuel statistics for a car
  - Response: `{"totalFuel": 120.0, "totalCost": 155.0, "averageConsumption": 6.4}`

### Servlet Endpoint

- **GET** `/servlet/fuel-stats?carId={id}` - Get fuel statistics (demonstrates manual servlet implementation)

---

## 💻 CLI Commands

Make sure the backend server is running first, then:

### 1. Create Car

```bash
./mvnw exec:java -pl cli -Dexec.args="create-car --brand Toyota --model Corolla --year 2018"
```

Or on Windows:
```cmd
mvnw.cmd exec:java -pl cli -Dexec.args="create-car --brand Toyota --model Corolla --year 2018"
```

### 2. Add Fuel Entry

```bash
./mvnw exec:java -pl cli -Dexec.args="add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000"
```

### 3. Get Fuel Statistics

```bash
./mvnw exec:java -pl cli -Dexec.args="fuel-stats --carId 1"
```

**Expected output:**
```
Total fuel: 120 L
Total cost: 155.00
Average consumption: 6.4 L/100km
```

---

## 🧪 Testing with Postman

### Import Collection

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `Car_Management_API.postman_collection.json`
5. Click **Import**

### Collection Structure

The collection includes:

#### 1. Cars Endpoints
- **Create Car** - POST `/api/cars`
- **List All Cars** - GET `/api/cars`
- **Add Fuel Entry** - POST `/api/cars/{id}/fuel`
- **Get Fuel Statistics** - GET `/api/cars/{id}/fuel/stats`

#### 2. Servlet Endpoint
- **Get Fuel Stats (Servlet)** - GET `/servlet/fuel-stats?carId={id}`

#### 3. Error Handling Tests
- Invalid Car ID (404)
- Invalid Data (400)
- Validation errors

### Testing Workflow

#### Step 1: Start the Server
```bash
./mvnw exec:java -pl backend
```

#### Step 2: Create a Car
1. Open **Create Car** request in Postman
2. Body is pre-filled with example data:
   ```json
   {
     "brand": "Toyota",
     "model": "Corolla",
     "year": 2018
   }
   ```
3. Click **Send**
4. **Expected:** Status 201, returns car object with ID
5. **Note the car ID** from the response (e.g., `"id": 1`)

#### Step 3: List All Cars
1. Open **List All Cars** request
2. Click **Send**
3. **Expected:** Status 200, returns array with your car

#### Step 4: Add Fuel Entries
1. Open **Add Fuel Entry** request
2. **Update the URL** - replace `1` with your actual car ID
3. Body is pre-filled:
   ```json
   {
     "liters": 40,
     "price": 52.5,
     "odometer": 45000
   }
   ```
4. Click **Send**
5. **Expected:** Status 201

6. **Add another fuel entry:**
   - Change odometer to a higher value (e.g., 50000)
   - Change liters and price
   - Click **Send** again

#### Step 5: Get Fuel Statistics
1. Open **Get Fuel Statistics** request
2. **Update the URL** - replace `1` with your actual car ID
3. Click **Send**
4. **Expected:** Status 200, returns:
   ```json
   {
     "totalFuel": 85.0,
     "totalCost": 112.5,
     "averageConsumption": 9.0
   }
   ```

#### Step 6: Test Servlet Endpoint
1. Open **Get Fuel Stats (Servlet)** request
2. **Update query parameter** - change `carId=1` to your actual car ID
3. Click **Send**
4. **Expected:** Same result as REST API endpoint

#### Step 7: Test Error Handling
1. Try **Get Stats - Invalid Car ID** (should return 404)
2. Try **Create Car - Invalid Data** (should return 400)
3. Try **Add Fuel - Invalid Car ID** (should return 404)

### Example Test Sequence

1. **Create Car:**
   ```json
   POST /api/cars
   {
     "brand": "Honda",
     "model": "Civic",
     "year": 2020
   }
   ```
   Response: `{"id": 1, ...}`

2. **Add First Fuel Entry:**
   ```json
   POST /api/cars/1/fuel
   {
     "liters": 40,
     "price": 52.5,
     "odometer": 45000
   }
   ```

3. **Add Second Fuel Entry:**
   ```json
   POST /api/cars/1/fuel
   {
     "liters": 45,
     "price": 60.0,
     "odometer": 50000
   }
   ```

4. **Get Statistics:**
   ```
   GET /api/cars/1/fuel/stats
   ```
   Response:
   ```json
   {
     "totalFuel": 85.0,
     "totalCost": 112.5,
     "averageConsumption": 9.0
   }
   ```

### Postman Tips

- **Update Car IDs:** Remember to replace `1` in URLs with your actual car ID
- **Check Status Codes:** Verify correct HTTP status codes (201, 200, 404, 400)
- **View Response:** Check the response body to see the data structure
- **Test Edge Cases:** Use the Error Handling Tests folder to verify error responses
- **Collection Variables:** The collection includes a variable `base_url` set to `http://localhost:8080`

---

## ✅ Implementation Status

### Backend (✅ Complete)
- [x] REST API endpoints (POST/GET /api/cars, POST /api/cars/{id}/fuel, GET /api/cars/{id}/fuel/stats)
- [x] Servlet endpoint (GET /servlet/fuel-stats?carId={id})
- [x] In-memory storage (ConcurrentHashMap)
- [x] Service layer shared between REST API and Servlet
- [x] Error handling (404, 400, 500)
- [x] Input validation
- [x] JSON serialization/deserialization

### CLI Client (✅ Complete)
- [x] create-car command
- [x] add-fuel command
- [x] fuel-stats command
- [x] HTTP client using java.net.http.HttpClient
- [x] JSON parsing and formatted output

### Build System (✅ Complete)
- [x] Maven wrapper included (no installation needed)
- [x] Multi-module project structure
- [x] Dependencies configured (Jetty, Jackson)

---

## 🏗️ Architecture

- **Model Layer**: Domain objects (Car, FuelEntry, FuelStats)
- **Service Layer**: Business logic (CarService) - shared between REST API and Servlet
- **API Layer**: REST API handlers and request/response DTOs
- **Servlet Layer**: Manual servlet implementation for fuel stats
- **HTTP Layer**: Request routing and handling
- **CLI Layer**: Command-line interface using HTTP client

---

## 🛡️ Error Handling

The system handles:
- Invalid car IDs (404 Not Found)
- Missing or invalid request parameters (400 Bad Request)
- Validation errors (400 Bad Request)
- Internal server errors (500 Internal Server Error)

---

## 📊 Features

- ✅ In-memory storage (no database required)
- ✅ REST API with proper error handling (404 for invalid IDs, validation)
- ✅ Manual Servlet implementation demonstrating request lifecycle
- ✅ CLI client using java.net.http.HttpClient
- ✅ Clean separation of concerns (Service layer shared between REST API and Servlet)
- ✅ Proper JSON serialization/deserialization
- ✅ Input validation and error messages
- ✅ Thread-safe in-memory storage (ConcurrentHashMap)
- ✅ Comprehensive error responses with proper HTTP status codes

---

## 📝 Code Quality

- **Clean Architecture**: Separation of concerns (Model, Service, API, Servlet layers)
- **Thread Safety**: ConcurrentHashMap for in-memory storage
- **Error Handling**: Comprehensive error responses with proper HTTP status codes
- **Validation**: Input validation for all endpoints
- **Documentation**: Clear code structure and comprehensive README

---

## 🎯 Requirements Met

| Requirement | Status |
|-------------|--------|
| Backend REST API | ✅ Complete |
| Servlet implementation | ✅ Complete |
| CLI client | ✅ Complete |
| In-memory storage | ✅ Complete |
| Error handling | ✅ Complete |
| Service layer reuse | ✅ Complete |

---

## 📚 Additional Resources

- **Postman Collection**: `Car_Management_API.postman_collection.json`
- **Assignment Compliance**: See `ASSIGNMENT_COMPLIANCE.md` for detailed compliance report

---

**Ready to use! Start the server and begin testing! 🚀**
