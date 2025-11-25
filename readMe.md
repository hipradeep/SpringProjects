```java
Client → Security Filter Chain → DispatcherServlet → Controller

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


----------------------------------------------
-------------------------------------------------

# Complete JWT Authentication Flow in Spring Boot

## 1. Request Flow Overview

```
Client → Security Filter Chain → JwtRequestFilter → Authorization Check → Controller → AuthenticationManager → UserDetailsService → PasswordEncoder → JWT Creation → Response
```

## 2. Login Request (/authenticate)

### Controller Snippet

```java
@PostMapping("/authenticate")
public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
    Authentication authentication = null;
    try {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                );

        authentication = authenticationManager.authenticate(authToken);

    } catch (BadCredentialsException e) {
        throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthResponse(jwt));
}
```

## 3. Security Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    String[] permitAllUrls = {"/authenticate", "/public/**"};
    String[] authenticatedUrls = {"/welcome", "/profile/**"};
    String[] userRoleUrls = {"/home/premium", "/user/**"};
    String[] adminRoleUrls = {"/home/trunk", "/admin/**"};

    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllUrls).permitAll()
                .requestMatchers(authenticatedUrls).authenticated()
                .requestMatchers(userRoleUrls).hasRole("USER")
                .requestMatchers(adminRoleUrls).hasRole("ADMIN")
                .anyRequest().denyAll()
        )
        .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### AuthenticationManager Bean

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}
```

## 4. CustomUserDetailsService

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(userEntity);
    }
}
```

## 5. CustomUserDetails

```java
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .toList();
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getUsername(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
```

## 6. JWT Filter

```java
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
```

## 7. User Repository (In-Memory Example)

```java
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public UserRepositoryImpl() {
        users.put("admin", new User(
                "admin",
                "$2a$10$qZG22TWZ3YuztYW.FtSCP.x0EucBOGycpuHAtiMa2rEfnE4/uO8uS",
                "admin@example.com",
                Set.of("ADMIN", "USER")
        ));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
```

## 8. Full Authentication Flow Diagram

```
Client → /authenticate
    ↓
Spring Security Filter Chain
    ↓
JwtRequestFilter (skips because no token)
    ↓
AuthorizationFilter (permitAll → allowed)
    ↓
Controller
    ↓
AuthenticationManager.authenticate()
    ↓
DaoAuthenticationProvider
    ↓
CustomUserDetailsService
    ↓
UserRepository
    ↓
PasswordEncoder.matches()
    ↓
Success → JWT generated
```


```