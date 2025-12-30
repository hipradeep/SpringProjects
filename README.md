# Reactive Spring Boot PostgreSQL CRUD (R2DBC)

This project demonstrates a full Reactive CRUD application using Spring WebFlux and **Spring Data R2DBC** with PostgreSQL.

## Architecture Overview
- **Framework**: Spring Boot 2.5.7 (Reactive)
- **Database**: PostgreSQL (via R2DBC)
- **Language**: Java 21
- **Layers**:
  - `ProductController`: Handles HTTP requests and returns `Product` entities.
  - `ProductService`: Business logic for product operations.
  - `ProductRepository`: Reactive database access via `R2dbcRepository<Product, Integer>`.
  - `Product`: Database entity where `id` is automatically generated. Now includes a list of `tags`.
  - `Tag`: Related entity stored in a separate `tags` table (One-to-Many).

---

## How to Run the Application

To run the application, use the following command in your terminal:

```cmd
mvn spring-boot:run
```

> [!TIP]
> If you encounter a `ClassNotFoundException` in your IDE, try running `mvn clean compile` or "Reload All Maven Projects" in IntelliJ.

---

## FAQ & Technical Concepts

### 1. Is `schema.sql` really necessary?
**Yes.** Unlike Spring Data JPA (Hibernate), which can auto-generate tables from entities, **Spring Data R2DBC** is a lightweight specification that does not support DDL (Data Definition Language) generation. 

- **Without `schema.sql`**: R2DBC will throw a `relation "products" does not exist` error on startup if the table is missing.
- **Data Preservation**: Use `CREATE TABLE IF NOT EXISTS` to keep your data. We only used `DROP TABLE` once to force the `SERIAL` sequence fix. If you use `DROP TABLE`, all your data will be deleted on every restart!

### 2. How is the ID generated automatically?
Automatic ID generation in this setup involves three layers working together:

1.  **PostgreSQL (`SERIAL`)**: In [schema.sql](file:///c:/Users/hipradeep/Documents/SpringProjects/src/main/resources/schema.sql), the `id` column is defined as `SERIAL`. This creates a sequence in the database that automatically increments every time a new row is inserted without an ID.
2.  **Spring Data R2DBC (`@Id`)**: In our [Product](file:///c:/Users/hipradeep/Documents/SpringProjects/src/main/java/com/hipradeep/code/entity/Product.java) entity, the `id` field is marked with `@Id`. 
3.  **The "Null ID" Rule**: When calling `repository.save(product)`, Spring Data R2DBC checks if the `id` is `null`. 
    - If `id` is **null**, it executes an `INSERT` statement *omitting* the ID column, allowing PostgreSQL to assign the next value from the sequence.
    - If `id` is **not null**, it executes an `UPDATE` statement.

### 3. How is the 1:N (Tags) relationship handled?
Spring Data R2DBC does not yet support automatic collection mapping (like JPA's `@OneToMany`). 
- **Manual Orchestration**: In `ProductService`, we manually coordinate between `ProductRepository` and `TagRepository`.
- **Fetching**: When getting a product, we fetch the `tags` list in a separate reactive call and attach it to the `Product` entity (which is marked with `@Transient`).
- **Data Integrity**: We use `ON DELETE CASCADE` in the SQL schema so that when a product is deleted, its tags are automatically removed by the database.

---

## How to Run locally

This project is configured to use a **local PostgreSQL server**.
- **Prerequisites**: Ensure you have PostgreSQL installed and running on `localhost:5432`.
- **Database**: Create a database named `products_database`.
- **User/Password**: `postgres` / `1234`.
- **Auto-Schema**: The table `products` is automatically created on startup.

---

## API Documentation (CURL Commands)

### Save a Product (With Tags)
```cmd
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"name\":\"mobile\",\"qty\":1,\"price\":15000, \"tags\":[{\"name\":\"electronics\"}, {\"name\":\"gadget\"}]}"
```

### Get All Products
```cmd
curl http://localhost:8080/products
```

### Get Product by ID
```cmd
curl http://localhost:8080/products/1
```

### Get Products in Price Range
```cmd
curl "http://localhost:8080/products/product-range?min=10000&max=20000"
```

### Update a Product (With Tags)
```cmd
curl -X PUT http://localhost:8080/products/update/1 -H "Content-Type: application/json" -d "{\"name\":\"mobile-updated\",\"qty\":2,\"price\":16000, \"tags\":[{\"name\":\"premium\"}, {\"name\":\"new-arrival\"}]}"
```

### Delete a Product
```cmd
curl -X DELETE http://localhost:8080/products/delete/1
```
