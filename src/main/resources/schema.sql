-- Online Grocery Management System -- schema
-- Idempotent: drop in dependency-safe order then recreate.

DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;

-- Joined-inheritance base table.
CREATE TABLE users (
    user_id  VARCHAR(64) PRIMARY KEY,
    name     VARCHAR(120) NOT NULL,
    email    VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    user_type VARCHAR(20) NOT NULL
);

CREATE TABLE customers (
    user_id   VARCHAR(64) PRIMARY KEY,
    address   VARCHAR(255),
    phone     VARCHAR(40),
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE admins (
    user_id   VARCHAR(64) PRIMARY KEY,
    role_name VARCHAR(60),
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE products (
    product_id VARCHAR(64) PRIMARY KEY,
    name       VARCHAR(160) NOT NULL,
    price      DOUBLE NOT NULL,
    quantity   INT NOT NULL DEFAULT 0
);

CREATE TABLE carts (
    cart_id     VARCHAR(64) PRIMARY KEY,
    customer_id VARCHAR(64) NOT NULL UNIQUE,
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customers(user_id) ON DELETE CASCADE
);

CREATE TABLE cart_items (
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id      VARCHAR(64) NOT NULL,
    product_id   VARCHAR(64) NOT NULL,
    quantity     INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_cartitem_cart    FOREIGN KEY (cart_id)    REFERENCES carts(cart_id)    ON DELETE CASCADE,
    CONSTRAINT fk_cartitem_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE orders (
    order_id     VARCHAR(64) PRIMARY KEY,
    cart_id      VARCHAR(64),
    customer_id  VARCHAR(64) NOT NULL,
    order_date   VARCHAR(40) NOT NULL,
    total_amount DOUBLE NOT NULL,
    summary      VARCHAR(2000),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(user_id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    review_id   VARCHAR(64) PRIMARY KEY,
    product_id  VARCHAR(64) NOT NULL,
    customer_id VARCHAR(64) NOT NULL,
    rating      INT NOT NULL,
    comment     VARCHAR(1000),
    created_at  VARCHAR(40) NOT NULL,
    moderated   BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_review_product  FOREIGN KEY (product_id)  REFERENCES products(product_id)  ON DELETE CASCADE,
    CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES customers(user_id) ON DELETE CASCADE
);
