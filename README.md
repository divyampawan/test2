# Ecommerce RESTful API

A comprehensive RESTful API for an ecommerce application built with Spring Boot, Spring Security, and JWT authentication.

## Features

- **User Management**: Registration, authentication, profile management
- **Product Management**: CRUD operations for products
- **Shopping Cart**: Add, remove, and update cart items
- **Order Management**: Create, view, and manage orders
- **JWT Authentication**: Secure API with JWT tokens
- **H2 Database**: In-memory database for development

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Security** with JWT
- **Spring Data JPA**
- **H2 Database**
- **MapStruct** for object mapping
- **Maven** for dependency management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## API Documentation

### Authentication

#### Register User
```
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "user@example.com",
  "email": "user@example.com",
  "phoneNumber": "1234567890",
  "password": "password123"
}
```

#### Login
```
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

### Products

#### Get All Products
```
GET /api/v1/products
Authorization: Bearer <token>
```

#### Get Product by ID
```
GET /api/v1/products/{id}
Authorization: Bearer <token>
```

#### Create Product
```
POST /api/v1/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "stockQuantity": 100,
  "imageUrl": "https://example.com/image.jpg"
}
```

#### Update Product
```
PUT /api/v1/products/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Updated Product Name",
  "description": "Updated Description",
  "price": 89.99,
  "stockQuantity": 50
}
```

#### Delete Product
```
DELETE /api/v1/products/{id}
Authorization: Bearer <token>
```

### Cart

#### Get Cart Items
```
GET /api/v1/cart
Authorization: Bearer <token>
```

#### Add Item to Cart
```
POST /api/v1/cart/add?productId={id}&quantity={quantity}
Authorization: Bearer <token>
```

#### Update Cart Item Quantity
```
PUT /api/v1/cart/{cartItemId}?quantity={quantity}
Authorization: Bearer <token>
```

#### Remove Item from Cart
```
DELETE /api/v1/cart/{cartItemId}
Authorization: Bearer <token>
```

### Orders

#### Get User Orders
```
GET /api/v1/orders
Authorization: Bearer <token>
```

#### Get Order by ID
```
GET /api/v1/orders/{id}
Authorization: Bearer <token>
```

#### Create Order
```
POST /api/v1/orders?discountCode={code}
Authorization: Bearer <token>
```

#### Process Order
```
PUT /api/v1/orders/{id}/process
Authorization: Bearer <token>
```

#### Complete Order
```
PUT /api/v1/orders/{id}/complete
Authorization: Bearer <token>
```

### User Profile

#### Get User Profile
```
GET /api/v1/users/profile
Authorization: Bearer <token>
```

#### Update User Profile
```
PUT /api/v1/users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "newemail@example.com",
  "phoneNumber": "9876543210"
}
```

#### Change Password
```
POST /api/v1/users/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "newPassword": "newpassword123"
}
```

### Health Check

#### API Health
```
GET /api/v1/health
```

## Data Models

### User
- `id`: Long
- `username`: String
- `email`: String
- `phoneNumber`: String
- `password`: String (encrypted)

### Product
- `id`: Long
- `name`: String
- `description`: String
- `price`: BigDecimal
- `stockQuantity`: Integer
- `imageUrl`: String

### CartItem
- `id`: Long
- `user`: User
- `product`: Product
- `quantity`: Integer

### Order
- `id`: Long
- `user`: User
- `orderDate`: LocalDateTime
- `status`: OrderStatus
- `totalAmount`: BigDecimal
- `orderItems`: List<OrderItem>

### OrderItem
- `id`: Long
- `order`: Order
- `product`: Product
- `quantity`: Integer
- `price`: BigDecimal

## Security

The API uses JWT (JSON Web Tokens) for authentication. All protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK`: Successful operation
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Access denied
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Development

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

### Package the Application

```bash
mvn package
```

## Configuration

Key configuration properties in `application.properties`:

- `jwt.secret`: JWT signing secret
- `jwt.expiration`: JWT token expiration time (milliseconds)
- `server.port`: Application port
- `spring.jpa.hibernate.ddl-auto`: Database schema generation strategy

## License

This project is licensed under the MIT License. 