-- Seed data for the Online Grocery Management System.
-- Users with passwords are seeded via SeedDataRunner so that the password
-- column always contains a valid BCrypt hash for the documented secrets
-- (admin: "admin123", customers: "pass123"). Everything else lives here.

-- Products.
INSERT INTO products (product_id, name, price, quantity) VALUES
  ('P-1', 'Basmati Rice 5kg', 1850.00, 50),
  ('P-2', 'Coconut Oil 1L',    980.00, 80),
  ('P-3', 'Red Lentils 1kg',   420.00, 120),
  ('P-4', 'Brown Sugar 1kg',   360.00, 100),
  ('P-5', 'Tea Leaves 500g',   620.00, 60);

-- The sample review is seeded in SeedDataRunner because it depends on a
-- customer record that the runner creates after data.sql executes.
