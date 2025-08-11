-- Sample Products
INSERT INTO products (name, description, price, stock_quantity, image_url) VALUES 
('Laptop', 'High-performance laptop with latest specifications', 999.99, 10, 'https://cdn.thewirecutter.com/wp-content/media/2024/11/cheapgaminglaptops-2048px-7981.jpg?auto=webp&quality=75&width=1024'),
('Smartphone', 'Latest smartphone with advanced features', 699.99, 15, 'https://www.apple.com/v/iphone/home/cd/images/meta/iphone__kqge21l9n26q_og.png'),
('Headphones', 'Wireless noise-cancelling headphones', 199.99, 20, 'https://cdn.mos.cms.futurecdn.net/PbBRJvxoAm4BM7vfhh8ZfG.jpg'),
('Tablet', '10-inch tablet perfect for work and entertainment', 399.99, 8, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRauO_3jTuSfQcZmQvQrrrW5ZT8GYDHcZRzug&s'),
('Smartwatch', 'Fitness tracking smartwatch with health monitoring', 299.99, 12, 'https://cdn.thewirecutter.com/wp-content/media/2023/11/fitness-tracker-2048px-5346.jpg?auto=webp&quality=75&crop=1.91:1&width=1200'),
('Camera', 'Professional DSLR camera for photography enthusiasts', 1299.99, 5, 'https://www.orionphotogroup.com/wp-content/uploads/2023/09/OPG-Blog-Best-Professional-Canon-Cameras-That-Should-Be-On-Every-Photographers-List-10-12-2023-scaled.jpg'),
('Gaming Console', 'Next-gen gaming console for immersive gaming', 499.99, 7, 'https://image.benq.com/is/image/benqco/s22?$ResponsivePreset$'),
('Wireless Speaker', 'Portable Bluetooth speaker with premium sound', 149.99, 25, 'https://www.jbl.com/dw/image/v2/BFND_PRD/on/demandware.static/-/Sites-siteCatalog_JB_US_Imported/default/dw0c3ec204/categoryimage/Charge5.jpg?sw=800&sh=400');

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