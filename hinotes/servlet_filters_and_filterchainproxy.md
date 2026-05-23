# Spring Security: Servlet Filters & FilterChainProxy

This note covers the architectural foundation of Spring Security in Servlet-based applications, detailing how `FilterChainProxy` manages internal filters to secure incoming requests in Spring Boot.

---

## 1. Core Concept: Servlet Filters in Spring Security

Spring Security's web protection is entirely built on **Servlet Filters** (the standard Java Enterprise Edition JEE mechanism). Filters intercept incoming HTTP requests before they reach the Controller (Servlet) and intercept outgoing responses before they return to the client.

```
Request  ──► [Filter 1] ──► [Filter 2] ──► [Servlet / Controller]
                                                  │
Response ◄── [Filter 1] ◄── [Filter 2] ◄──────────┘
```

---

## 2. The Role of `FilterChainProxy`

In a Spring Boot application:
* **`FilterChainProxy`** is a central filter provided by Spring Security. It is registered in the Servlet container (like Tomcat).
* It acts as the **entry point** into Spring Security's filter mechanism.
* Instead of performing the security checks itself, it delegates the request to one or more matching **`SecurityFilterChain`** instances.
* A single request passes through a sequence of **15 to 30 internal security filters** contained inside the matched chain.

---

## 3. Key Internal Filters (Order Matters)

Spring Security maintains a precise ordering of filters. Understanding this order is crucial when inserting custom filters (like custom JWT filters).

| Filter Class | Responsibility |
| :--- | :--- |
| **`SecurityContextPersistenceFilter`** *(Legacy)* / **`SecurityContextHolderFilter`** *(Modern)* | Loads the existing authentication information (from HTTP Session, etc.) into the `SecurityContext` at the start of the request, and clears/persists it at the end. |
| **`CsrfFilter`** | Protects against Cross-Site Request Forgery by generating and validating CSRF tokens for modifying requests (POST, PUT, DELETE). |
| **`UsernamePasswordAuthenticationFilter`** | Processes login form submissions. Intercepts `POST` requests to `/login` and authenticates the user using standard username/password credentials. |
| **`BasicAuthenticationFilter`** | Extracts and processes credentials from the HTTP standard `Authorization` header containing `Basic <credentials>` (Base64-encoded). |
| **`ExceptionTranslationFilter`** | Catch block for security exceptions (`AccessDeniedException` and `AuthenticationException`). It translates these exceptions into HTTP responses (e.g. redirecting to login page or returning 401/403 errors). |
| **`FilterSecurityInterceptor`** *(Legacy)* / **`AuthorizationFilter`** *(Modern)* | The final security filter in the chain. It determines whether the current authenticated user has the necessary privileges/roles to access the requested resource. |

---

## 4. Architectural Request Flow

The exact lifecycle of a request entering a Spring Boot application secured by Spring Security is structured as follows:

```mermaid
graph TD
    A[Client Request] --> B[Servlet Container (Tomcat)]
    B --> C["DelegatingFilterProxy (Bridge Bean)"]
    C --> D["FilterChainProxy (Spring Security Head)"]
    D --> E{Find Matching SecurityFilterChain}
    E -->|Matches Route| F[Filter 1: SecurityContextHolderFilter]
    F --> G[Filter 2: CsrfFilter]
    G --> H[...]
    H --> I[Filter N: AuthorizationFilter]
    I --> J[DispatcherServlet]
    J --> K[Controller / API Endpoint]
```

### Flow Breakdown:
1. **Tomcat/Servlet Container** receives the incoming HTTP request.
2. The request reaches **`DelegatingFilterProxy`**, a standard Servlet filter that links the Servlet container to the Spring Application Context.
3. `DelegatingFilterProxy` locates and delegates execution to the **`FilterChainProxy`** bean.
4. `FilterChainProxy` checks configured filter chains and picks the **matching `SecurityFilterChain`** based on the request URL.
5. The request travels through the ordered collection of security filters.
6. If all filters pass, the request is dispatched to the standard **`DispatcherServlet`** and finally reaches your `@RestController`.
