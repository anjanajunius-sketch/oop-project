# Online Grocery Management System

A Spring Boot web application built for the SE1020 OOP project. The system models a small online grocery store with customers, admins, products, carts, orders, and reviews.

## Stack

- Java 17, Maven, Spring Boot 3.5.x
- Spring Web + Thymeleaf (modern responsive UI: Bootstrap 5.3 + Bootstrap Icons + Inter font, all via CDN — no npm/build step)
- Spring Data JPA + Hibernate
- MySQL / MariaDB (default datasource — wired for a cPanel-hosted database). H2 is used only for `mvn test`.
- Spring Security (BCrypt password hashing only — all routes are open for now)

## Running

The default `application.properties` is wired against the cPanel MySQL DB (host `us101.serverclubservers.com`, db `paperzon_oop`). You only need to provide the password — credentials are read from environment variables so the password never lands in git.

In IntelliJ:

1. Run > **Edit Configurations…** > select `OopProjectApplication`.
2. **Modify options** > tick **Environment variables**.
3. Click the small icon at the right of the field to open the table editor and add **one row per variable**:

   | Name | Value |
   |---|---|
   | `DB_URL` | `jdbc:mysql://us101.serverclubservers.com:3306/paperzon_oop?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
   | `DB_USER` | `paperzon_oop` |
   | `DB_PASSWORD` | `<your real cPanel password>` |

   (Use the table editor — *not* the inline `;`-separated field — because the password may contain `;` or `[` `]`.)
4. Apply, then click ▶ Run. Open <http://localhost:8080>.

Hibernate creates the 8 tables on first run; `SeedDataRunner` inserts the 5 demo products + 1 admin + 2 customers + 1 review idempotently. Subsequent restarts preserve user-created data.

## Setting up a fresh cPanel MySQL / MariaDB database

The steps below show how to create a brand-new cPanel database from scratch. If you've already received `DB_URL` / `DB_USER` / `DB_PASSWORD` from a teammate, skip ahead to the [Running](#running) section.

### 1. Create the database in cPanel

1. cPanel → **MySQL® Databases**.
2. **Create New Database**: name it `grocery`. cPanel prepends your account name, so the actual name will be something like `cpaneluser_grocery`. Note the full name.
3. **Add New User**: pick a username (e.g. `groceryapp`) and a strong password. The full username will look like `cpaneluser_groceryapp`. Note both.
4. Scroll to **Add User to Database**: pick the user + the database, click *Add*. On the next screen tick **ALL PRIVILEGES**, then *Make Changes*.

### 2. Allow remote connections

1. cPanel → **Remote MySQL®** (sometimes named "Remote Database Access").
2. Find your public IP at <https://api.ipify.org> and paste it into the **Host** field. Click *Add Host*.
3. Each team member must add their own public IP here. Don't use `%` (any IP) — it exposes the database to the internet.

> If your cPanel doesn't show "Remote MySQL", or it's grayed out, your hosting provider has disabled remote access. Contact them or use the SSH-tunnel fallback (`ssh -L 3306:localhost:3306 user@yourdomain.com`, then connect to `localhost:3306`).

### 3. Find the host and port

The MySQL host is usually shown at the top of the **MySQL® Databases** page (look for "MySQL Hostname" or similar). It's typically:

- `yourdomain.com`, or
- `srvXXX.<provider>.com` (e.g. `srv123.namecheaphosting.com`), or
- A dedicated `mysql.yourdomain.com`.

The port is **3306** unless your provider remapped it (some use 3307).

### 4. Set the four credentials in IntelliJ (no file edits)

Open the run configuration:

1. In the top-right of IntelliJ, click the dropdown next to the green ▶ → **Edit Configurations…**
2. Select `OopProjectApplication` (or create a new "Spring Boot" run config pointing at it).
3. Click **Modify options** → tick **Environment variables** and **Active profiles**.
4. **Active profiles**: `mysql`
5. **Environment variables**: paste this all on one line (each key separated by `;`), filling in your cPanel values:

   ```
   DB_URL=jdbc:mysql://YOUR_HOST:3306/cpaneluser_grocery?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true;DB_USER=cpaneluser_groceryapp;DB_PASSWORD=YOUR_PASSWORD
   ```

6. Apply, then click ▶ Run. Watch the bottom Run console for `Started OopProjectApplication`.

Hibernate will create the 8 tables on first run (no need to run `schema.sql` manually against the remote DB), and `SeedDataRunner` will insert the 5 products + admin + 2 customers + 1 review on first start. Subsequent restarts preserve user-created data.

### Or via the command line

```bash
SPRING_PROFILES_ACTIVE=mysql \
DB_URL='jdbc:mysql://YOUR_HOST:3306/cpaneluser_grocery?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true' \
DB_USER='cpaneluser_groceryapp' \
DB_PASSWORD='YOUR_PASSWORD' \
./mvnw spring-boot:run
```

PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="mysql"
$env:DB_URL="jdbc:mysql://YOUR_HOST:3306/cpaneluser_grocery?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USER="cpaneluser_groceryapp"
$env:DB_PASSWORD="YOUR_PASSWORD"
.\mvnw.cmd spring-boot:run
```

### Troubleshooting

- **`Communications link failure` / connection hangs** → the provider is blocking port 3306 from outside, or your IP isn't whitelisted yet (check Remote MySQL).
- **`Access denied for user`** → wrong username/password, or the user wasn't added to the database with privileges (step 1.4).
- **`Unknown database`** → the `DB_URL` doesn't match the `cpaneluser_` prefix exactly.
- **`Public Key Retrieval is not allowed`** → make sure `allowPublicKeyRetrieval=true` is in the JDBC URL (it is in the examples above).
- **MariaDB-only syntax errors** → none expected; the schema is plain MySQL DDL that MariaDB accepts. If anything fails, check the Hibernate log for the failing CREATE statement and report it.

To go back to the H2 default just remove `mysql` from "Active profiles" (or unset `SPRING_PROFILES_ACTIVE`).

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
├── model/       User, Customer, Admin, Cart, CartItem, Product, Order, Review
├── repository/  one Spring Data JPA repository per entity
├── service/     CustomerService, AdminService, ProductService, CartService,
│                OrderService, ReviewService
└── controller/  HomeController + one Thymeleaf controller per member component
src/main/resources
├── application.properties        H2 default profile
├── application-mysql.properties  MySQL/MariaDB profile (env-var creds)
├── schema.sql                    H2-flavoured DDL (Hibernate auto-generates for MySQL)
├── data.sql                      H2 product seeds
└── templates/                    plain HTML Thymeleaf templates, no CSS/JS
```
