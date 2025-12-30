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

### 4. How are Validation & Errors handled?
We use `spring-boot-starter-validation` (JSR-303) and a unified global exception handler.
- **Validation**: Fields in `Product` and `Tag` are annotated with `@NotBlank`, `@Min`, and `@Positive`.
- **Validation Errors**: A `@RestControllerAdvice` (`GlobalExceptionHandler`) catches `WebExchangeBindException` and returns a structured `400 Bad Request` with field-specific error messages.
- **Custom Exceptions**: We implemented `ProductNotFoundException`. If you request a non-existent ID, the service throws this runtime exception, which the global handler catches to return a clean `404 Not Found` response.

### 5. Why Flyway? (Database Migration)

We migrated from `schema.sql` to **Flyway** for robust database version control.

#### **Why do we use it?**
- **Consistency**: Ensures every environment (dev, test, prod) has the exact same database schema.
- **History**: Tracks exactly which scripts have been applied and when.
- **Safety**: Prevents accidental data loss (unlike `schema.sql`, which can be fragile with `DROP TABLE` commands).

#### **What problem does it solve?**
It solves "Schema Drift". Without a migration tool, it's easy for manual changes or disparate scripts to cause the application code to become out of sync with the database structure, leading to runtime errors.

#### **How is it implemented?**
1.  **Dependencies**: Added `flyway-core` **AND** `spring-boot-starter-jdbc`.
    > **Note**: Even though this is a Reactive (R2DBC) app, Flyway requires a blocking JDBC driver to run migrations on startup.
2.  **Configuration**: In `application.properties`:
    - Point Flyway to the database (using JDBC URL).
    - Set `spring.flyway.baseline-on-migrate=true` (to handle existing databases).
    - Disable Spring's default `sql.init.mode`.
3.  **Scripts**: Migration scripts are placed in `src/main/resources/db/migration`.
4.  **Metadata**: Flyway creates a `flyway_schema_history` table in your database to track applied versions.

> [!NOTE]
> **Schema Evolution Example**: We added a `description` column later by creating `V2__add_description_to_products.sql`. Flyway automatically detected that `V1` was already applied and only ran the new `V2` script, preserving existing data while updating the structure.
>
> **Q: What if I remove a field from the `Product` entity?**
> - **Database**: The column and its existing data **remains** in the database.
> - **App**: Spring Data R2DBC simply ignores the column.
> - **To Delete**: You must explicitly create a migration script (e.g., `V3__drop_description.sql`) with `ALTER TABLE products DROP COLUMN description;`.
>   > **WARNING**: This will permanently delete the column and **ALL data** inside it immediately upon restart.
>
> **Q: How do I add Flyway to an *existing* database?**
> 1.  **Snapshot**: Create a `V1__init.sql` that contains the `CREATE TABLE` scripts for your *current* database state.
> 2.  **Baseline**: Set `spring.flyway.baseline-on-migrate=true`.
> 3.  **Run**: When you start the app, Flyway sees the existing tables, creates the history table, marks `V1` as "Baseline" (skipped), and will only run future scripts (`V2+`).
>
> **Q: Does Flyway auto-generate scripts (like Hibernate)?**
> - **No**. Flyway is a **Script Runner**, not a generator.
> - **R2DBC limitations**: Unlike JPA, R2DBC cannot scan your entities to generate SQL. You **must** write the SQL files manually in `db/migration`. If you delete them, nothing happens!
>
> **Q: Can I rollback a migration (undo)?**
> - **Not easily**. The "Undo" feature is part of the paid Flyway Teams edition.
> - **Strategy**: We "Roll Forward". If `V2` breaks something, we don't delete it. We create `V3__fix_V2.sql` to revert the changes or fix the issue.
>
> **Q: Is this setup production-ready?**
> - **Yes, but with a caveat**: We are currently packaging **two** drivers: `R2DBC` (for the app) and `JDBC` (just for Flyway).
> - **Optimization**: In large-scale cloud architectures (like Kubernetes), you might run Flyway as a separate "Init Container" or CI/CD step so your main application doesn't need the heavy JDBC driver.

#### **What are the alternatives?**
- **Liquibase**: Defines changes in XML/YAML/JSON (database-agnostic) but is more verbose.
- **Spring Data JPA (Hibernate)**: Can auto-generate DDL (`ddl-auto`), but risky for production.
- **Manual Scripts**: Implementing `schema.sql` manually (what we had before), which lacks version tracking and rollback safety.

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
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"name\":\"mobile\",\"qty\":1,\"price\":15000,\"description\":\"Latest smartphone\", \"tags\":[{\"name\":\"electronics\"}, {\"name\":\"gadget\"}]}"
```

### Get All Products (Paginated & Sorted)
```cmd
curl "http://localhost:8080/products?page=0&size=5&sort=price,desc"
```

### Get All Products (Default)
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
curl -X PUT http://localhost:8080/products/update/1 -H "Content-Type: application/json" -d "{\"name\":\"mobile-updated\",\"qty\":2,\"price\":16000,\"description\":\"Updated description\", \"tags\":[{\"name\":\"premium\"}, {\"name\":\"new-arrival\"}]}"
```

### Delete a Product
```cmd
curl -X DELETE http://localhost:8080/products/delete/1
```
