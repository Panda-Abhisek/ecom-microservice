# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Build, run, and test commands

### Build
- This project uses the Maven Wrapper at the repository root (`mvnw` / `mvnw.cmd`).
- Clean build and run all tests:

```bash
./mvnw clean package
# or on Windows PowerShell
./mvnw.cmd clean package
```

This produces an executable JAR under `target/ecom-application-0.0.1-SNAPSHOT.jar`.

### Run the application locally
- Run via Spring Boot Maven plugin (preferred during development):

```bash
./mvnw spring-boot:run
# or on Windows PowerShell
./mvnw.cmd spring-boot:run
```

- Run the built JAR (after `mvnw package`):

```bash
java -jar target/ecom-application-0.0.1-SNAPSHOT.jar
```

The application starts a Spring Boot service using the configuration in `src/main/resources/application.yml`.

### Tests
- Run all tests:

```bash
./mvnw test
# or on Windows PowerShell
./mvnw.cmd test
```

- Run a single test class (example with the existing `EcomApplicationTests`):

```bash
./mvnw -Dtest=EcomApplicationTests test
# or on Windows PowerShell
./mvnw.cmd -Dtest=EcomApplicationTests test
```

- Run a single test method:

```bash
./mvnw -Dtest=EcomApplicationTests#contextLoads test
```

Tests live under `src/test/java/com/app/ecom`.

> Note: There is no dedicated linting/formatting plugin configured in `pom.xml`. Use your IDE's inspections or add Maven plugins (e.g., Checkstyle, Spotless) if needed.

## High-level architecture and structure

### Overview
- This is a Spring Boot microservice (`@SpringBootApplication` in `com.app.ecom.EcomApplication`) built on:
  - `spring-boot-starter-web` for REST APIs
  - `spring-boot-starter-data-jpa` for persistence
  - `spring-boot-starter-actuator` for operational endpoints
  - PostgreSQL as the primary database
- The main code lives under `src/main/java/com/app/ecom`, organized into typical layered packages:
  - `controller` – HTTP API layer
  - `service` – business logic layer
  - `repository` – data access layer (Spring Data JPA)
  - `model` – JPA entities and domain enums
  - `dto` – request/response payloads for the REST APIs

### Layered flow (controller → service → repository → database)
- **Controllers** expose REST endpoints under `/api/**`:
  - `ProductController` (`/api/products`) handles CRUD and search for products.
  - `CartController` (`/api/cart`) manages a user's shopping cart via `X-User-Id`/`X-User-ID` headers.
  - `OrderController` (`/api/orders`) creates orders from the current cart using `X-User-ID`.
- **Services** encapsulate domain logic and orchestrate entities/repositories:
  - `ProductService` handles product creation, updates, soft deletion (via an `active` flag), active-product listing, and keyword search.
  - `CartService` manages cart contents for a user: adding items (with stock and user validation), retrieving the cart, deleting individual items, and clearing the cart.
  - `OrderService` creates `Order` aggregates from the current cart, computes totals, persists `Order` and `OrderItem` entities, and then clears the cart.
- **Repositories** are Spring Data JPA interfaces over `model` entities:
  - `ProductRepository` adds a custom `searchProducts` query and an `findByActiveTrue` finder.
  - `CartItemRepository`, `OrderRepository`, and `UserRepository` provide standard CRUD and a few custom methods (e.g., finding/deleting cart items by user/product).

Data typically flows:
1. HTTP request hits a `controller` endpoint.
2. Controller calls the appropriate `service` method.
3. Service uses one or more `repository` interfaces to load and persist `model` entities.
4. Service maps entities to `dto` objects for responses.
5. Controller returns a typed `ResponseEntity` wrapping these DTOs.

### Domain model and DTOs
- **Model layer (`model` package):**
  - Core entities include `Product`, `CartItem`, `Order`, `OrderItem`, `User`, `Address`, and enums such as `OrderStatus` and `UserRole`.
  - These are JPA-mapped classes backing the database schema.
- **DTO layer (`dto` package):**
  - `*Request` classes (`ProductRequest`, `CartItemRequest`, `UserRequest`, etc.) model incoming payloads.
  - `*Response` / read-side DTOs (`ProductResponse`, `OrderResponse`, `OrderItemDTO`, `UserResponse`, `AddressDTO`) represent what is sent back to clients.
  - Services are responsible for converting between `model` entities and DTOs (e.g., `ProductService#mapToProductResponse`, `OrderService#mapToOrderResponse`).

### Configuration, persistence, and observability
- **Application configuration:**
  - `src/main/resources/application.yml` configures:
    - The Spring Boot application name (`ecom-application`).
    - PostgreSQL datasource at `jdbc:postgresql://localhost:5432/microservice-ecom` with username `postgres` and password `123`.
    - JPA/Hibernate settings, including `ddl-auto: update` and the PostgreSQL dialect.
    - Hibernate JDBC time zone (`Asia/Kolkata`).
- **Actuator and management endpoints:**
  - Actuator is enabled via `spring-boot-starter-actuator` and configured in `application.yml`:
    - Exposes endpoints such as `beans` and `health` over HTTP.
    - Configures health details to always be shown.
    - Enables additional `info.app.*` metadata (name, description, version).

### Testing
- Current tests consist of a Spring Boot context load test in `EcomApplicationTests` under `src/test/java/com/app/ecom`.
- New tests should follow the same package structure as the main code (mirroring `com.app.ecom.*`) so that Spring Boot test configuration and component scanning behave as expected.
