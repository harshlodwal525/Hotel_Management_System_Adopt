# 🏨 Hotel Management System

## 📋 Prerequisites
- Java 17 or higher  
- Maven 3.6+  
- Docker 

## 🛠️ Installation & Setup
### 1. Clone the Repository
```bash
git clone <repository-url>
cd hotel-booking-system
2. Run with Maven
bash
Copy code
# Install dependencies and run tests
mvn clean install

# Run the application
mvn spring-boot:run
3. Run with Docker
bash
Copy code
# Build and run with Docker
build and run manually
docker build -t hotel-management-system .
docker run -p 8080:8080 hotel-management-system
📊 Database Access
H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:hotelbookingdb

Username: sa

Password: password

📖 API Documentation
Once the application is running, access the interactive API documentation:

Swagger UI: http://localhost:8080/swagger-ui.html

🔐 Authentication
Register a New User
bash
Copy code
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123",
    "role": "ADMIN"
  }'
Login
bash
Copy code
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
Response:

json
Copy code
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "role": "ROLE_ADMIN"
  }
}
🏨 Sample API Usage
1. Create a Hotel (Admin Only)
bash
Copy code
curl -X POST http://localhost:8080/hotels \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "JW Hotel",
    "location": "Pune",
    "rating": 5
  }'
2. Add Rooms to Hotel (Admin Only)
bash
Copy code
# Add a Single Room
curl -X POST http://localhost:8080/hotels/1/rooms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "101",
    "type": "SINGLE",
    "pricePerNight": 1500.00
  }'

# Add a Double Room
curl -X POST http://localhost:8080/hotels/1/rooms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "201",
    "type": "DOUBLE",
    "pricePerNight": 2500.00
  }'

# Add a Suite
curl -X POST http://localhost:8080/hotels/1/rooms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "301",
    "type": "SUITE",
    "pricePerNight": 5000.00
  }'
3. Search Available Rooms (User/Admin)
bash
Copy code
curl -X GET "http://localhost:8080/rooms/search?location=Pune&checkIn=14-10-2025&checkOut=23-10-2025" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
4. Create a Booking (User/Admin)
bash
Copy code
curl -X POST http://localhost:8080/bookings \
  -H "Authorization: Bearer USER_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomId": 1,
    "checkInDate": "2025-11-11",
    "checkOutDate": "2025-11-13"
  }'
5. View My Bookings (User/Admin)
bash
Copy code
curl -X GET http://localhost:8080/bookings/mybooking \
  -H "Authorization: Bearer USER_JWT_TOKEN"
6. Cancel a Booking (User/Admin)
bash
Copy code
curl -X DELETE http://localhost:8080/bookings/1 \
  -H "Authorization: Bearer USER_JWT_TOKEN"

📝 API Endpoints Summary
Method	Endpoint	Role	Description
POST	/auth/register	PUBLIC	Register new user
POST	/auth/login	PUBLIC	User authentication
POST	/hotels	ADMIN	Create hotel
PUT	/hotels/{id}	ADMIN	Update hotel
DELETE	/hotels/{id}	ADMIN	Delete hotel
POST	/hotels/{hotelId}/rooms	ADMIN	Add room
GET	/rooms/search	USER/ADMIN	Search rooms
POST	/bookings	USER/ADMIN	Create booking
GET	/bookings/mybooking	USER/ADMIN	Get user bookings
DELETE	/bookings/{id}	USER/ADMIN	Cancel booking

🤝 Contributed By Harsh

📄 License
This project is licensed under the MIT License – see the LICENSE file for details.

📧 harshlodwal525@gmail.com
