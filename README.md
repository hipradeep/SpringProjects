# Reactive Spring Boot R2DBC Project

This is a clean starter project for building Reactive applications using Spring WebFlux and Spring Data R2DBC with PostgreSQL.

## Stack
- Java 21
- Spring Boot 2.5.7 (Reactive)
- PostgreSQL
- Maven

## Setup
The project is configured to connect to a local PostgreSQL instance.
- **URL**: `r2dbc:postgresql://localhost:5432/products_database`
- **Username**: `postgres`
- **Password**: `1234`

## Running
```cmd
mvn spring-boot:run
```
