# Reactive Spring Boot PostgreSQL CRUD (R2DBC) - Functional Endpoints

This project demonstrates a full Reactive CRUD application using **Spring WebFlux Functional Endpoints (Router/Handler)** and **Spring Data R2DBC** with PostgreSQL.

## Architecture Overview
- **Framework**: Spring Boot 2.5.7 (Reactive)
- **Database**: PostgreSQL (via R2DBC)
- **Language**: Java 21
- **Pattern**: **Functional Endpoints** (Replaces `@RestController`)
- **Layers**:
  - `RouterConfig`: Defines the API routes (`/router/products...`).
  - `ProductHandler`: Handles HTTP requests and returns `ServerResponse`.
  - `ProductService`: Business logic for product operations.
  - `ProductRepository`: Reactive database access via `R2dbcRepository`.
  - `GlobalErrorWebExceptionHandler`: Global Exception Handling (rendering JSON errors).

---

## How to Run the Application

To run the application, use the following command in your terminal:

```cmd
mvn spring-boot:run
```

> [!TIP]
> If you encounter a `ClassNotFoundException` in your IDE, try running `mvn clean compile` or "Reload All Maven Projects" in IntelliJ.

---

## Global Exception Handling
The project implements a global exception handler for functional endpoints.
- **Handler**: `GlobalErrorWebExceptionHandler`
- **Customization**: `GlobalErrorAttributes`
- **Behavior**:
    - **ProductNotFoundException**: Returns `404 Not Found` with custom JSON.
    - **Other Errors**: Returns `500 Internal Server Error`.


---

## Traditional vs. Reactive Global Exception Handling

| Feature | Traditional Spring MVC (`@ControllerAdvice`) | Reactive Spring WebFlux (`WebExceptionHandler`) |
| :--- | :--- | :--- |
| **Execution Model** | Blocking / Synchronous | Non-Blocking / Asynchronous |
| **Base Class** | `ResponseEntityExceptionHandler` | `AbstractErrorWebExceptionHandler` |
| **Return Unit** | `ResponseEntity<T>` | `Mono<ServerResponse>` |
| **Context** | `WebRequest` | `ServerRequest` |
| **Error Attributes** | `ErrorAttributes` (Servlet-based) | `ErrorAttributes` (Reactive-based) |
| **Mechanism** | Uses reflection and AOP proxies to intercept exceptions. | Operating at the lower level of the HTTP handling chain (Netty/Reactor). |

In this project, we use `GlobalErrorWebExceptionHandler` which extends `AbstractErrorWebExceptionHandler`. This is necessary because in a functional reactive application, exceptions might occur during the reactive stream processing, and standard `@ControllerAdvice` may not catch all signals correctly, especially those happening before the controller level (e.g., routing errors).

---


## API Documentation (CURL Commands)

**Note**: All endpoints now use the `/router` prefix as defined in `RouterConfig`.

### Save a Product (With Tags)
```cmd
curl -X POST http://localhost:8080/router/products/save -H "Content-Type: application/json" -d "{\"name\":\"mobile\",\"qty\":1,\"price\":15000, \"tags\":[{\"name\":\"electronics\"}, {\"name\":\"gadget\"}]}"
```

### Get All Products
```cmd
curl http://localhost:8080/router/products
```

### Get Product by ID
```cmd
curl http://localhost:8080/router/products/1
```

### Get Products in Price Range
```cmd
curl "http://localhost:8080/router/products/product-range?min=10000&max=20000"
```

### Update a Product
```cmd
curl -X PUT http://localhost:8080/router/products/update/1 -H "Content-Type: application/json" -d "{\"name\":\"mobile-updated\",\"qty\":2,\"price\":16000, \"tags\":[{\"name\":\"premium\"}, {\"name\":\"new-arrival\"}]}"
```

### Delete a Product
```cmd
curl -X DELETE http://localhost:8080/router/products/delete/1
```

---

## How to Run locally

This project is configured to use a **local PostgreSQL server**.
- **Prerequisites**: Ensure you have PostgreSQL installed and running on `localhost:5432`.
- **Database**: Create a database named `products_database`.
- **User/Password**: `postgres` / `1234`.
- **Auto-Schema**: The table `products` is automatically created on startup.
