# E-commerce Application API Documentation

This document provides detailed documentation for all API endpoints in the e-commerce application, explaining the functionality of each endpoint line by line.

## Table of Contents
1. [Authentication Endpoints](#authentication-endpoints)
2. [User Endpoints](#user-endpoints)
3. [Product Endpoints](#product-endpoints)
4. [Cart Endpoints](#cart-endpoints)
5. [Order Endpoints](#order-endpoints)
6. [Payment Endpoints](#payment-endpoints)
7. [Discount Endpoints](#discount-endpoints)

---

## Authentication Endpoints

### 1. User Login
- **Endpoint**: `POST /api/v1/auth/login`
- **Request Body**:
  ```json
  {
    "username": "user123",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt.token.here",
    "expiresIn": 3600
  }
  ```
- **Code Flow**:
  1. Validates the request body using `@Valid`
  2. Authenticates user credentials using Spring Security's `AuthenticationManager`
  3. If authentication succeeds, generates a JWT token
  4. Returns the token and its expiration time
- **Error Cases**:
  - 401 Unauthorized: If authentication fails
  - 400 Bad Request: If request body is invalid

### 2. User Registration
- **Endpoint**: `POST /api/v1/auth/register`
- **Request Body**:
  ```json
  {
    "username": "newuser",
    "password": "secure123",
    "email": "user@example.com",
    "phoneNumber": "1234567890"
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "username": "newuser",
    "email": "user@example.com",
    "phoneNumber": "1234567890"
  }
  ```
- **Code Flow**:
  1. Validates the request body
  2. Maps DTO to User entity
  3. Saves the new user
  4. Returns the created user DTO (without password)
- **Error Cases**:
  - 400 Bad Request: If username/email already exists or validation fails

## User Endpoints

### 1. Get User Profile
- **Endpoint**: `GET /api/v1/users/profile`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Response**:
  ```json
  {
    "id": 1,
    "username": "user123",
    "email": "user@example.com",
    "phoneNumber": "1234567890"
  }
  ```
- **Code Flow**:
  1. Gets authentication from security context
  2. Verifies user is authenticated
  3. Fetches user details by username
  4. Returns user DTO
- **Error Cases**:
  - 401 Unauthorized: If no valid token provided
  - 404 Not Found: If user not found

### 2. Update User Profile
- **Endpoint**: `PUT /api/v1/users/profile`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "email": "newemail@example.com",
    "phoneNumber": "0987654321"
  }
  ```
- **Response**: Updated user DTO
- **Code Flow**:
  1. Verifies authentication
  2. Fetches current user
  3. Updates allowed fields (email, phone)
  4. Saves changes
  5. Returns updated user DTO
- **Error Cases**:
  - 401 Unauthorized: If not authenticated
  - 400 Bad Request: If validation fails

## Product Endpoints

### 1. Get All Products
- **Endpoint**: `GET /api/v1/products`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "name": "Smartphone",
      "description": "Latest model smartphone",
      "price": 699.99,
      "stockQuantity": 50
    },
    ...
  ]
  ```
- **Code Flow**:
  1. Fetches all products from database
  2. Maps entities to DTOs
  3. Returns product list

### 2. Get Product by ID
- **Endpoint**: `GET /api/v1/products/{id}`
- **Response**: Single product DTO
- **Code Flow**:
  1. Fetches product by ID
  2. Returns 404 if not found
  3. Maps to DTO and returns

## Cart Endpoints

### 1. Get Cart Items
- **Endpoint**: `GET /api/v1/cart`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "Smartphone",
        "price": 699.99
      },
      "quantity": 2
    }
  ]
  ```
- **Code Flow**:
  1. Verifies authentication
  2. Fetches current user
  3. Retrieves cart items for user
  4. Returns cart item DTOs

### 2. Add to Cart
- **Endpoint**: `POST /api/v1/cart/add`
- **Headers**:
  - `Authorization: Bearer <token>`
  - `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "productId": 1,
    "quantity": 1
  }
  ```
- **Response**: Success message
- **Code Flow**:
  1. Verifies authentication
  2. Validates request body
  3. Fetches product
  4. Adds item to user's cart
  5. Returns success response

## Order Endpoints

### 1. Create Order
- **Endpoint**: `POST /api/v1/orders`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "shippingAddress": "123 Main St, City, Country",
    "paymentMethod": "CREDIT_CARD"
  }
  ```
- **Response**: Created order DTO
- **Code Flow**:
  1. Verifies authentication
  2. Fetches current user
  3. Creates order from cart
  4. Processes payment
  5. Returns order details

## Payment Endpoints

### 1. Process Payment
- **Endpoint**: `POST /api/v1/payments/process`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "orderId": 1,
    "paymentMethod": "CREDIT_CARD",
    "cardNumber": "4111111111111111",
    "expiryDate": "12/25",
    "cvv": "123"
  }
  ```
- **Response**: Payment confirmation DTO
- **Code Flow**:
  1. Validates payment details
  2. Processes payment through payment gateway
  3. Updates order status
  4. Returns payment confirmation

## Discount Endpoints

### 1. Apply Discount Code
- **Endpoint**: `POST /api/v1/discounts/apply`
- **Headers**:
  - `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "code": "SUMMER20"
  }
  ```
- **Response**:
  ```json
  {
    "code": "SUMMER20",
    "discountType": "PERCENTAGE",
    "value": 20,
    "description": "20% off on all items"
  }
  ```
- **Code Flow**:
  1. Validates discount code
  2. Checks if code is active and applicable
  3. Returns discount details
  
---

## Error Responses

All error responses follow this format:
```json
{
  "timestamp": "2025-08-11T00:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found",
  "path": "/api/v1/users/999"
}
```

Common error statuses:
- 400 Bad Request: Invalid input data
- 401 Unauthorized: Authentication required or invalid token
- 403 Forbidden: Insufficient permissions
- 404 Not Found: Resource not found
- 500 Internal Server Error: Server-side error
