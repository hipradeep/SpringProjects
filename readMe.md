```java
Client
  |
  | POST /authenticate (username, password)
  |
Controller
  |
  v
AuthenticationManager
  |
  v
DaoAuthenticationProvider (Spring auto-created)
  |
  v
CustomUserDetailsService --> UserRepository --> User
  |
  v
PasswordEncoder (BCrypt)
  |
  v
Authenticated UserDetails
  |
  v
JWT generated
  |
  v
Returned to client
----------------------------------------------
Next Request:
  |
  | Authorization: Bearer <jwt>
  v
JwtRequestFilter
  |
  v
Validate token & load user
  |
  v
SecurityContextHolder.setAuthentication()
  |
  v
Authorization rules (hasRole, authenticated)
  |
  v
Controller Method

```