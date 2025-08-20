# API Endpoints Checklist

## Authentication
- [ ] `POST /api/v1/auth/login` - User login
- [ ] `POST /api/v1/auth/register` - User registration
- [ ] `GET /api/v1/auth/me?username={username}` - Get current user details

## User Management
- [ ] `GET /api/v1/users/profile` - Get user profile
- [ ] `PUT /api/v1/users/profile` - Update user profile
- [ ] `POST /api/v1/users/change-password` - Change password
- [ ] `GET /api/v1/users/{id}` - Get user by ID

## Product Management
- [ ] `GET /api/v1/products` - Get all products
- [ ] `GET /api/v1/products/{id}` - Get product by ID
- [ ] `POST /api/v1/products` - Create new product
- [ ] `PUT /api/v1/products/{id}` - Update product
- [ ] `DELETE /api/v1/products/{id}` - Delete product

## Shopping Cart
- [ ] `GET /api/v1/cart` - Get user's cart
- [ ] `POST /api/v1/cart/add` - Add item to cart
- [ ] `PUT /api/v1/cart/{id}` - Update cart item quantity
- [ ] `DELETE /api/v1/cart/{id}` - Remove item from cart

## Order Management
- [ ] `GET /api/v1/orders` - Get user's orders
- [ ] `POST /api/v1/orders` - Create new order
- [ ] `PUT /api/v1/orders/{id}/process` - Process order
- [ ] `PUT /api/v1/orders/{id}/complete` - Complete order

## Payment Processing
- [ ] `POST /api/v1/payments` - Create payment
- [ ] `POST /api/v1/payments/simulate-success` - Simulate successful payment

## Discount Management
- [ ] `GET /api/v1/discounts/validate?code={code}&amount={amount}` - Validate discount code
- [ ] `POST /api/v1/discounts` - Create new discount

## How to Use
1. To mark an endpoint as complete, say: "Check off [endpoint name]"
   Example: "Check off User login"
2. To check all endpoints of a specific type: "Show me completed auth endpoints"
