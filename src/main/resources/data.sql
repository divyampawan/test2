-- Sample Products
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES 
('Laptop', 'High-performance laptop with latest specifications', 999.99, 10, 'https://via.placeholder.com/300x200?text=Laptop'),
('Smartphone', 'Latest smartphone with advanced features', 699.99, 15, 'https://via.placeholder.com/300x200?text=Smartphone'),
('Headphones', 'Wireless noise-cancelling headphones', 199.99, 20, 'https://via.placeholder.com/300x200?text=Headphones'),
('Tablet', '10-inch tablet perfect for work and entertainment', 399.99, 8, 'https://via.placeholder.com/300x200?text=Tablet'),
('Smartwatch', 'Fitness tracking smartwatch with health monitoring', 299.99, 12, 'https://via.placeholder.com/300x200?text=Smartwatch'),
('Camera', 'Professional DSLR camera for photography enthusiasts', 1299.99, 5, 'https://via.placeholder.com/300x200?text=Camera'),
('Gaming Console', 'Next-gen gaming console for immersive gaming', 499.99, 7, 'https://via.placeholder.com/300x200?text=Gaming+Console'),
('Wireless Speaker', 'Portable Bluetooth speaker with premium sound', 149.99, 25, 'https://via.placeholder.com/300x200?text=Wireless+Speaker');

-- Sample Users (password is 'password123' encrypted with BCrypt)
INSERT INTO users (username, email, password) VALUES 
('johndoe', 'john@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
('janesmith', 'jane@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
('admin', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');

-- Sample Discounts
INSERT INTO discounts (code, description, percentage, minimum_order_amount, max_discount_amount, start_date, end_date, active, usage_limit, usage_count) VALUES 
('WELCOME10', 'Welcome discount - 10% off', 10.00, 50.00, 100.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', true, 0, 0),
('SAVE20', 'Save 20% on orders over $100', 20.00, 100.00, 200.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', true, 0, 0),
('FLAT50', 'Flat $50 off on orders over $200', 0.00, 200.00, 50.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', true, 0, 0); 