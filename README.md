# Online Grocery Management System

A Spring Boot web application built for the SE1020 OOP project. The system models a small online grocery store with customers, admins, products, carts, orders, and reviews.

## Stack

- Java 17, Maven, Spring Boot 3.5.x
- Spring Web + Thymeleaf (plain HTML test pages — no CSS/JS)
- Spring Data JPA + Hibernate
- H2 (file-based, default) — MySQL config also included (commented)
- Spring Security (BCrypt password hashing only — all routes are open for now)

## Running

```bash
./mvnw spring-boot:run
```

Then open <http://localhost:8080>. The H2 console is available at <http://localhost:8080/h2-console> with JDBC URL `jdbc:h2:file:./data/grocery`, user `sa`, no password.

The schema and seed data (`schema.sql`, `data.sql`) are re-applied on every startup, so the database always starts fresh and consistent.

## Switching to MySQL

In `src/main/resources/application.properties` comment out the H2 datasource block and uncomment the MySQL block. Then:

```sql
CREATE DATABASE grocery;
```

Adjust username/password to match your local MySQL.

## Class diagram

The entities mirror the project class diagram:

```
User (abstract)
├── Customer ── 1..1 Cart ── 0..* CartItem ── n..1 Product
│      │
│      ├── 0..* Order
│      └── 0..* Review ──────────────────────── n..1 Product
└── Admin
```

`User` is a JPA `@Inheritance(JOINED)` parent, and `Customer` / `Admin` are persisted in their own sub-tables. This gives the project real OOP at the persistence layer (encapsulation, inheritance, polymorphism).

## Workload distribution (6 members)

| # | Member component | Owns | Plain-HTML pages |
|---|---|---|---|
| 1 | **Customer Management** | `Customer`, `CustomerService`, `CustomerController`, `CustomerRepository` | `/customers`, `/customers/register`, `/customers/login`, `/customers/{id}/edit`, `/customers/{id}/profile` |
| 2 | **Admin Management** | `Admin`, `AdminService`, `AdminController`, `AdminRepository` | `/admins`, `/admins/register`, `/admins/login`, `/admins/{id}/edit`, `/admins/dashboard` |
| 3 | **Product Management** | `Product`, `ProductService`, `ProductController`, `ProductRepository` | `/products`, `/products/add`, `/products/{id}/edit` |
| 4 | **Cart Management** | `Cart`, `CartItem`, `CartService`, `CartController`, `Cart/CartItemRepository` | `/cart/{customerId}`, `/cart/{customerId}/add` |
| 5 | **Order Management** | `Order`, `OrderService`, `OrderController`, `OrderRepository` | `/orders`, `/orders/place/{customerId}`, `/orders/{orderId}` |
| 6 | **Review Management** | `Review`, `ReviewService`, `ReviewController`, `ReviewRepository` | `/reviews`, `/reviews/add`, `/reviews/{id}/edit`, `/reviews/moderate` |

`User` (the abstract base) is shared infrastructure used by both Customer and Admin Management.

Each component implements full CRUD (Create, Read, Update, Delete) and applies all three OOP concepts:

- **Encapsulation** — entities have private fields with explicit getters and setters.
- **Inheritance** — `Customer` and `Admin` extend `User`; persistence uses joined-table inheritance.
- **Polymorphism** — `User.getRoleLabel()` is overridden by both subclasses; controllers and templates render it without caring about the concrete type.

## Default seeded accounts

| Role | Email | Password | ID |
|------|-------|----------|----|
| Admin | `admin@grocery.local` | `admin123` | `U-A1` |
| Customer | `alice@example.com` | `pass123` | `U-C1` |
| Customer | `bimal@example.com` | `pass123` | `U-C2` |

> The seeded password hashes in `data.sql` are placeholder BCrypt strings. You can register fresh accounts via `/customers/register` or `/admins/register` to log in immediately.

## Project layout

```
src/main/java/com/grocery/oopproject
├── OopProjectApplication.java
├── config/SecurityConfig.java
├── domain/      User, Customer, Admin, Cart, CartItem, Product, Order, Review
├── repository/  one Spring Data JPA repository per entity
├── service/     CustomerService, AdminService, ProductService, CartService,
│                OrderService, ReviewService
└── web/         HomeController + one Thymeleaf controller per member component
src/main/resources
├── application.properties
├── schema.sql
├── data.sql
└── templates/   plain HTML Thymeleaf templates, no CSS/JS
```
