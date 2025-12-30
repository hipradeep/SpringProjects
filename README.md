# Spring Boot Project

This is a Spring Boot application demonstrating user authentication, management, and product handling.

## specific Project Structure

- **`com.hipradeep.code`**: Root package containing the main application class, repositories, services, and security configurations.
- **`com.hipradeep.code.controller`**: REST controllers and Web controllers (e.g., `AppController`, `ProductController`).
- **`com.hipradeep.code.model`**: Entity classes (e.g., `User`, `Product`, `Role`).
- **`com.hipradeep.code.exception`**: Global exception handling.

## Technologies Used

- Java 17 (or compatible)
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security (Currently disabled for development convenience)
- Thymeleaf (for server-side rendering)
- MySQL / H2 Database (depending on configuration)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed.
- Maven installed (or use the provided `mvnw` wrapper).

### Installation

1.  Clone the repository.
2.  Navigate to the project directory.

### Running the Application

To run the application using Maven:

```bash
./mvnw spring-boot:run
```

The application will start on port `8080` (default).

### Accessing the Application

- **Home Page**: [http://localhost:8080](http://localhost:8080)
- **User List**: [http://localhost:8080/users](http://localhost:8080/users)
- **Registration**: [http://localhost:8080/register](http://localhost:8080/register)
 
> [!TIP]
> First create a user on the registration page before attempting to log in.

> [!NOTE]
> Spring Security login is currently disabled (`permitAll` on all requests) for easier testing.
 
## Internal Authentication Flow
 
When a user attempts to log in (if security were enabled), the following process occurs:
 
1.  **User Submission**: The user enters their email (username) and password on the login form.
2.  **DaoAuthenticationProvider**: Spring Security uses this provider to handle the authentication request. It retrieves the user details using a `UserDetailsService`.
3.  **CustomUserDetailsService**: This service (located in `com.hipradeep.code`) implements `UserDetailsService`.
    - Its `loadUserByUsername(email)` method is called.
    - It uses `UserRepository` to query the database for a `User` with the given email.
    - If found, it wraps the `User` entity in a `CustomUserDetails` object and returns it.
4.  **Password Verification**: The `DaoAuthenticationProvider` then uses the `BCryptPasswordEncoder` to checks if the entered password matches the hashed password stored in the `CustomUserDetails` object.
5.  **Result**:
    - **Success**: An authentication token is created and stored in the security context. The user is redirected to the success URL (`/users`).
    - **Failure**: An exception is thrown, and the user is shown the "Bad credentials" error.
 
## Security Configuration
 
The default Spring Security login screen is currently **DISABLED** for development convenience.
 
### How to Re-enable Login
To restore the login functionality, update `src/main/java/com/hipradeep/code/WebSecurityConfig.java` with the following configuration:
 
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/users").authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
            .usernameParameter("email")
            .defaultSuccessUrl("/users")
            .permitAll()
        .and()
        .logout().logoutSuccessUrl("/").permitAll();
}
```

## API Endpoints

- `GET /users`: List all users.
- `GET /register`: Show registration form.
- `POST /process_register`: Handle user registration.

## Testing

Run unit and integration tests with:

```bash
./mvnw test
```
