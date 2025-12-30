# Reactive Spring Boot MongoDB CRUD

This project demonstrates a full Reactive CRUD (Create, Read, Update, Delete) application using Spring WebFlux and Spring Data MongoDB Reactive, following professional patterns like DTO mapping.

## Implementation Steps

1.  **Dependency Setup**: Added `spring-boot-starter-data-mongodb-reactive` to `pom.xml`.
2.  **Entity & DTO Model**: 
    - `Product`: MongoDB entity for persistence.
    - `ProductDto`: Data Transfer Object for API requests/responses.
3.  **Mapping Utility**: Created `AppUtils` to map between Entity and DTO using `BeanUtils`.
4.  **Service Layer**: Implemented `ProductService` to encapsulate business logic and mapping.
5.  **Repository**: Created `ProductRepository` with custom query support (e.g., `findByPriceBetween`).
6.  **Controller**: REST endpoints using `Mono` and `Flux` with `ProductDto`.

---

## Technical Note: Why we removed `spring-boot-starter-web`

You might notice that `spring-boot-starter-web` has been removed from `pom.xml`. 
- **The Problem**: When both MVC (web) and WebFlux (webflux) are present, Spring Boot defaults to MVC. Spring MVC's Jackson configuration does not natively know how to deserialize `reactor.core.publisher.Mono` used in `@RequestBody`.
- **The Solution**: By removing the MVC dependency, the app runs as a **pure Reactive WebFlux** application. WebFlux provides built-in support for reactive types in controllers, resolving the `InvalidDefinitionException`.

---

## How to Run locally

As of the latest update, **this project includes an embedded MongoDB (Flapdoodle)** for convenience.
- **You do NOT need to install MongoDB** locally to run/test the code. 
- Spring Boot will automatically start a transient in-memory MongoDB instance when you start the application.

---

## API Documentation (CURL Commands)

### 1. Save Product
```cmd
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"name\":\"mobile\",\"qty\":1,\"price\":15000}"
```

### 2. Get All Products
```cmd
curl http://localhost:8080/products
```

### 3. Get Products in Price Range
```cmd
curl "http://localhost:8080/products/product-range?min=10000&max=50000"
```

### 4. Get Product by ID
```cmd
curl http://localhost:8080/products/{id}
```

### 5. Update Product
```cmd
curl -X PUT http://localhost:8080/products/update/{id} -H "Content-Type: application/json" -d "{\"name\":\"laptop\",\"qty\":2,\"price\":55000}"
```

### 6. Delete Product
```cmd
curl -X DELETE http://localhost:8080/products/delete/{id}
```
