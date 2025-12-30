# Reactive Spring Boot R2DBC Project

This is a clean starter project for building Reactive applications using Spring WebFlux and Spring Data R2DBC with PostgreSQL.

## Stack
- Java 21
- Spring Boot 2.5.7 (Reactive)
- PostgreSQL
- Maven
- Twilio SDK 9.x

## Setup
The project is configured to connect to a local PostgreSQL instance.
- **URL**: `r2dbc:postgresql://localhost:5432/products_database`
- **Username**: `postgres`
- **Password**: `1234`

## Features

### Simulated Twilio OTP Service
This project includes a simulated OTP sending service using Twilio.
- **Functionality**: Generates a 6-digit OTP and logs it to the console instead of sending an actual SMS (to avoid costs/setup).
- **Endpoint**: `POST /router/sendOTP`
- **Request Body**:
    ```json
    {
      "phoneNumber": "+1234567890",
      "userName": "testUser"
    }
    ```
- **Response**: Returns a delivery status and the message content.

For a detailed workflow diagram, please refer to the [walkthrough](file:///c:/Users/hipradeep/.gemini/antigravity/brain/caa2e88e-74be-4152-accb-7275f41627cc/walkthrough.md).

## Running
```cmd
mvn spring-boot:run
```
