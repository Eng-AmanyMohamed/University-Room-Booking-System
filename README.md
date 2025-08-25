# 🎓 University Room Booking System (Back-End)

 **This project is developed as part of the *Banque Misr Backend Internship* program, focusing on mastering Spring Boot, RESTful APIs, security, logging ,testing(unit & integration) and enterprise-grade application design.**
 
---

## 🔧 Project Overview
The University Room Booking System (Back-End) is a robust Spring Boot application designed to streamline the process of booking academic rooms (classrooms, labs, etc.) across university buildings. It supports role-based access control (RBAC), JWT authentication, room availability checks, booking workflows, admin approvals, cancellation policies, and comprehensive audit logging.

This system enables students, faculty, and administrators to efficiently manage room reservations while enforcing business rules such as no double bookings, holiday restrictions, approval workflows, and time window constraints.

---
## 🛠️ Technologies & Tools Used

| Category       | Technology |
|----------------|----------|
| Framework      | Spring Boot (Java 17+) |
| Build Tool     | Maven |
| Database       | H2 (Dev), MySQL (Production) |
| Security       | Spring Security + JWT |
| Validation     | Jakarta Bean Validation (Hibernate Validator) |
| ORM            | Spring Data JPA / Hibernate |
| API Testing    | Postman ,Swagger UI  |
| Logging        | SLF4J + Logback  |
| Dev Tools      | Lombok, Spring Initializr  |
| Testing        | JUnit 5, Mockito, MockMvc  |

---

## 📁 Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/sprints/UniversityRoomBookingSystem
│   │       ├── config/               # Security, JWT, WebConfig, DataSource
│   │       ├── controller/           # REST Controllers (Auth, Room, Booking, Admin)
│   │       ├── dto/                  # Request/Response DTOs
│   │       ├── exception/            # Custom exceptions & handler
│   │       ├── util/                 # JWT Authentication Filter
│   │       ├── model/                # JPA Entities (User, Room, Booking, etc.)
│   │       ├── repository/           # JPA Repositories
│   │       ├── service/              # Service Interfaces & Implementations
│   │       ├── validation/            # Custom validators (@NoOverlap)
│   │       ├── modelmapper/          # map from entity to Dto and from entity to response Dto
│   │       └── RoomBookingApplication.java
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
└── test/
    ├── java/
    │   └── com/sprints/UniversityRoomBookingSystem
    │       ├── integration/          # JPA integration tests
    │       ├── service/              # Service unit tests
    └── resources/
        └── application-test.yml      # H2 + test config
```

---

## 🚀 Project Setup

### 1. Prerequisites
- Java 17 or higher
- Maven 
- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- MySQL

### 2. Clone the Project
```bash
git clone https://github.com/Eng-AmanyMohamed/University-Room-Booking-System.git
cd University-Room-Booking-System
```
### 2. Configure Database
Edit src/main/resources/application-dev.yml (for MySQL):
```
DB_URL=jdbc:mysql://localhost:3306/room_booking
DB_USER=root
DB_PASS=password
JWT_SECRET=yourStrongSecretKeyHere
```
### 4.🧪 Running the Application
Using Maven (Command Line)
```
mvn spring-boot:run
```
---
## ⚙️ Configuration
- application.yml
```
spring:
  profiles:
    active: dev
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASS}
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    com.sprints.UniversityRoomBookingSystem: INFO
    org.springframework.security: DEBUG
```
- Profiles
  - application-dev.yml: MySQL, debug logs, JWT expiration 
  - application-prod.yml: Production DB, rate limiting, JWT 
  - application-test.yml: H2, no security, auto-ddl

---

## 🚀 Features
### ✅ Role-Based Access Control (RBAC):
- STUDENT: Search availability, request bookings, view/cancel own bookings.
- FACULTY: Request bookings for lectures/events, view/cancel own bookings.
- ADMIN: Full control over rooms, bookings, users, and policies.

---

### 🔐 JWT Authentication & Security
- Secure login/logout
- Token expiration handling
- Role-based endpoint protection

---
### 📅 Room Booking Management
- Create, view, cancel bookings
- Approval workflow (PENDING → APPROVED/REJECTED)
- Prevent double bookings with time overlap validation

---

### 🔍 Room Availability Search
- Query free time slots by room and date range
- Filter by features (e.g., projector, whiteboard, computers)

---

### 🏢 Room & Building Management 
- CRUD operations on rooms, buildings, departments
- Attach/detach room features
- Soft delete with constraints

---
### 📚 Audit Trail & History Logging
- Track all booking status changes in BookingHistory
- Log actor (user), action, timestamp, and reason

---
### 🛡️ Validation & Business Rules
- Custom @NoOverlap constraint
- Date validation (@FutureOrPresent, max 90-day horizon)
- Holiday exclusion
- Cancellation policy enforcement

---
### 🧪 Comprehensive Testing
- Unit tests with JUnit 5 + Mockito
- Integration tests with @SpringBootTest, MockMvc, H2 database
- Repository query testing with TestEntityManager

---
### 📦 Modular Architecture
- Clean separation: Controllers → Services → Repositories
- DTOs for safe data transfer
- Exception handling via @ControllerAdvice

---
### 📄 Configurable via Profiles
- application-dev.yml, application-prod.yml
- Logging levels, DB settings, JWT secrets
 
---
## ✅ Business Rules
| Rule       | Enforcement |
|----------------|----------|
| No past bookings                   | @FutureOrPresent on start time|
| Max booking horizon: 90 days       | Custom validator |
| No overlapping bookings            | @Query+@NoOverlap annotation |
| Holidays are non-bookable          |HolidayRepository check before create/approve|
| Only admin can approve/reject                    |RBAC + service logic |
| Cancellation allowed only before start time      | Service validation|
| Cannot delete room with future approved bookings | Repository check → 409 CONFLICT  |
| Every status change →BookingHistoryentry         | Service layer logging  |

---
## 🌐 Sample API Endpoints

All secured endpoints require a valid JWT in the header:  
`Authorization: Bearer <your-jwt-token>`
- POST `/api/users/register`Register a new user (ِAdmin,Student,FacultyMember).
- POST `/api/auth/login`Authenticate user, return JWT
- GET `/api/rooms/available` Get available time slots for a room/date
- POST `/api/bookings` Request a new booking (PENDING)
- PATCH `/api/admin/bookings/{id}/approve` Approve booking
- PATCH `/api/admin/bookings/{id}/reject` 	Reject booking with reason
- GET `/api/admin/bookings` List all bookings (filterable)
- DELETE `/api/bookings/{id}` Cancel own booking
- POST `/api/admin/rooms` Add new room
- POST `api/holidays` Add new holiday
- GET `api/holidays` Get all holidays
---
## 🧹 Global Exception Handling
- Returns structured JSON errors:
```
{
  "timestamp": "2025-04-05T10:00:00Z",
  "path": "/api/v1/bookings",
  "status": 400,
  "error": "Bad Request",
  "message": "Booking overlaps with existing reservation.",
}
```
- Handles:
   - ResourceNotFoundException
   - UnauthorizedActionException.
   - ValidationException.
   - BookingOverlapException.

---

## 🧪 Testing Strategy
### ✅ Unit Tests
- BookingServiceTest: Overlap detection, approval logic
- ValidationTest: @NoOverlap, @FutureOrPresent, holiday checks
### ✅ Integration Tests
- RoomControllerTest (@WebMvcTest): Auth headers, response codes
- BookingRepositoryTest (@DataJpaTest): Native/JPQL queries for availability
- FullFlowIT (@SpringBootTest): End-to-end booking → approval → cancel
- Uses H2 in-memory DB for fast, isolated testing. 

---

## Team:
This project was a collaborative effort with an outstanding team:
- Amany Mohamed
- Mohamed Mostafa
- Youssef Ahmed
- Touka Mohamed
- Leena Hesham
- Esraa Tarek
- Ayten Aaser
- Mariam Ayman

