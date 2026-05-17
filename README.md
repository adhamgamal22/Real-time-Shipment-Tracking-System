# 🚚 Real-Time Shipment Tracking System

A **Spring Boot** backend system for managing shipments and payments with real-time notifications, live location tracking, JWT security, and Stripe integration.
---

## ✨ Features

- 🔐 **JWT Authentication** — Secure login, signup, and password reset
- 📦 **Shipment Management** — Create, track, and update shipment status
- 💳 **Payment Integration** — Full payment lifecycle with Stripe
- 🔔 **Real-Time Notifications** — WebSocket-powered live updates
- 📍 **Live Location Tracking** — Real-time GPS tracking via WebSocket
- 👤 **User Profile Management** — View and update profile, change password
- 🛡️ **Role-Based Access Control** — Admin and User roles
- 📄 **Swagger UI** — Interactive API documentation

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 17 | Core language |
| Spring Boot | Backend framework |
| Spring Security + JWT | Authentication & Authorization |
| PostgreSQL | Database |
| Hibernate / JPA | ORM |
| WebSocket (STOMP) | Real-time notifications & live location |
| Stripe API | Payment processing |
| Swagger / OpenAPI | API documentation |
| Lombok | Boilerplate reduction |

---

## 📁 Project Structure

```
src/main/java/com/Adham/Shipment/
│
├── config/
│   ├── SecurityConfig.java        # JWT & Role-based security rules
│   ├── JwtAuthFilter.java         # JWT request filter
│   ├── JwtHelper.java             # JWT token utility
│   ├── SwaggerConfig.java         # Swagger/OpenAPI setup
│   ├── StripeConfig.java          # Stripe API initialization
│   ├── WebSocketConfig.java       # WebSocket/STOMP broker config
│   └── CorsConfig.java            # CORS settings
│
├── controller/
│   ├── AuthController.java        # Login, signup, password reset
│   ├── ShipmentController.java    # Shipment CRUD & tracking
│   ├── PaymentController.java     # Payment process & cancel
│   ├── StripeController.java      # Stripe payment intent & webhook
│   ├── UserController.java        # User profile management
│   ├── AdminController.java       # Admin user & shipment management
│   ├── NotificationController.java# Get & mark notifications
│   └── LocationController.java         # Live location tracking
│
├── Services/
│   ├── AuthService.java           # Authentication logic
│   ├── ShipmentService.java       # Shipment business logic
│   ├── PaymentService.java        # Payment business logic
│   ├── StripeService.java         # Stripe integration logic
│   ├── UserService.java           # User profile logic
│   ├── AdminService.java          # Admin operations
│   ├── NotificationService.java   # Real-time notification logic
│   ├── LocationService.java            # Live location tracking logic
│   ├── UserDetailsServiceImpl.java# Spring Security user loader
│   └── Utils/SecurityUtils.java   # Security helper methods
│
├── Repository/
│   ├── UserAccountRepo.java       # User data access
│   ├── ShipmentRepository.java    # Shipment data access
│   ├── PaymentRepo.java           # Payment data access
│   ├── NotificationRepo.java      # Notification data access
│   └── PasswordResetRepo.java     # Password reset token access
│
├── Shipment.entites/
│   ├── User.java                  # User entity
│   ├── Shipment.java              # Shipment entity
│   ├── Payment.java               # Payment entity
│   ├── Notification.java          # Notification entity
│   ├── PasswordReset.java         # Password reset token entity
│   ├── Role.java                  # User role enum (ADMIN/USER)
│   ├── ShipmentStatus.java        # Shipment status enum
│   └── PaymentStatus.java         # Payment status enum
│
├── Dto/
│   ├── user/
│   │   ├── AdminUserResponse.java
│   │   ├── UpdateRoleRequest.java
│   │   ├── UpdateProfileRequest.java
│   │   ├── ChangePasswordRequest.java
│   │   └── UserProfileResponse.java
│   ├── LocationUpdateRequest.java
│   ├── LocationResponse.java
│   ├── PaymentRequest.java
│   ├── PaymentResponse.java
│   ├── StripePaymentRequest.java
│   ├── StripePaymentResponse.java
│   ├── NotificationResponse.java
│   ├── CreateShipmentRequest.java
│   ├── ShipmentResponse.java
│   ├── LoginRequest.java
│   ├── SignupRequest.java
│   ├── PasswordResetdto.java
│   ├── StatusUpdateMessage.java
│   └── UpdateStatusRequest.java
│
└── Shared/
    └── CustomResponseException.java  # Global custom exception
```

---

## 🔗 API Endpoints

### 🔑 Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/signup` | Register new user |
| POST | `/auth/login` | Login and get JWT token |
| POST | `/auth/reset-password/initiate` | Request password reset |

### 📦 Shipment
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/shipment/create` | Create new shipment (Admin) |
| GET | `/api/shipment/{id}` | Get shipment by ID |
| GET | `/api/shipment/track/{trackingNumber}` | Track shipment |
| GET | `/api/shipment/getall` | Get all shipments |
| PUT | `/api/shipment/{id}/status` | Update shipment status |

### 💳 Payment
| Method | Endpoint | Description |
|---|---|---|
| POST | `/payment/process` | Create payment by tracking number |
| POST | `/payment/shipment/{shipmentId}` | Create payment by shipment ID |
| PUT | `/payment/{trackingNumber}/cancel` | Cancel payment |
| GET | `/payment` | Get all payments |

### 💰 Stripe
| Method | Endpoint | Description |
|---|---|---|
| POST | `/stripe/create-intent` | Create Stripe payment intent |
| POST | `/stripe/webhook` | Stripe webhook handler |

### 👤 User
| Method | Endpoint | Description |
|---|---|---|
| GET | `/user/profile` | Get current user profile |
| PUT | `/user/profile` | Update profile |
| PUT | `/user/change-password` | Change password |

### 🛡️ Admin
| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/users` | Get all users |
| PUT | `/admin/users/{id}/role` | Update user role |
| DELETE | `/admin/users/{id}` | Delete user |
| GET | `/admin/shipments` | Get all shipments |

### 🔔 Notifications
| Method | Endpoint | Description |
|---|---|---|
| GET | `/notifications` | Get all notifications |
| GET | `/notifications/unread` | Get unread notifications |
| PUT | `/notifications/{id}/read` | Mark as read |
| PUT | `/notifications/read-all` | Mark all as read |

### 📍 Live Location 
| Method | Endpoint | Description |
|---|---|---|
| POST | `/Location/update` | Update shipment location (REST) |
| GET | `/Location/{shipmentId}` | Get current shipment location |
| WS | `/app/Location.update` | Update location via WebSocket |

---

## ⚡ Real-Time Features (WebSocket)

Connect via WebSocket on `/ws` using STOMP protocol:

### 🔔 Notifications


Notifications are triggered automatically on:
- ✅ Payment created / updated / processed / cancelled
- 📦 Shipment status updated

### 📍 Live Location Tracking


---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Setup

1. **Clone the repository**
```bash
git clone https://github.com/adhamgamal22/realtime-shipment-tracking-system.git
cd realtime-shipment-tracking-system
```

2. **Configure `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shipment_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
stripe.secret.key=sk_test_your_stripe_key
stripe.webhook.secret=whsec_your_webhook_secret
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

---

## 👨‍💻 Author

**Adham Gamal**
- GitHub: [@adhamgamal22](https://github.com/adhamgamal22)
- LinkedIn: [linkedin.com/in/adhamgamal]([(https://www.linkedin.com/in/adhamgamal74)](https://www.linkedin.com/in/adhamgamal74))
