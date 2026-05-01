-- Seed data for the Online Grocery Management System.

-- Admin user (password "admin123" — bcrypt placeholder hash).
INSERT INTO users (user_id, name, email, password, user_type) VALUES
  ('U-A1', 'Site Admin', 'admin@grocery.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe7l1.YVNL8I9xiAsbF9wLrM3RGqHQ.K', 'ADMIN');
INSERT INTO admins (user_id, role_name) VALUES ('U-A1', 'SUPER_ADMIN');

-- Customers (password "pass123").
INSERT INTO users (user_id, name, email, password, user_type) VALUES
  ('U-C1', 'Alice Perera', 'alice@example.com', '$2a$10$Dow1QmZ2XJgZe5LTtT0u9.s9uX6X1NnBQy6xQfGzqK9ZqCQyQyZxK', 'CUSTOMER'),
  ('U-C2', 'Bimal Silva', 'bimal@example.com', '$2a$10$Dow1QmZ2XJgZe5LTtT0u9.s9uX6X1NnBQy6xQfGzqK9ZqCQyQyZxK', 'CUSTOMER');

INSERT INTO customers (user_id, address, phone) VALUES
  ('U-C1', '12, Galle Road, Colombo 03', '0771234567'),
  ('U-C2', '45, Peradeniya Road, Kandy', '0719876543');

-- Each customer auto-gets a cart.
INSERT INTO carts (cart_id, customer_id) VALUES
  ('CRT-1', 'U-C1'),
  ('CRT-2', 'U-C2');

-- Products.
INSERT INTO products (product_id, name, price, quantity) VALUES
  ('P-1', 'Basmati Rice 5kg', 1850.00, 50),
  ('P-2', 'Coconut Oil 1L',    980.00, 80),
  ('P-3', 'Red Lentils 1kg',   420.00, 120),
  ('P-4', 'Brown Sugar 1kg',   360.00, 100),
  ('P-5', 'Tea Leaves 500g',   620.00, 60);

-- One sample review.
INSERT INTO reviews (review_id, product_id, customer_id, rating, comment, created_at, moderated) VALUES
  ('R-1', 'P-1', 'U-C1', 5, 'Excellent rice, fluffy and aromatic.', '2026-04-30T10:00:00', FALSE);
