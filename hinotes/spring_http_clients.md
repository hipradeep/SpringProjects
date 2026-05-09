# Spring Boot HTTP Clients: RestClient vs HttpClient

This document compares and demonstrates two ways to consume external REST APIs in Spring Boot 3.2+: the modern `RestClient` and the standard Java `HttpClient`.

---

## 1. RestClient (Spring Boot 3.2+)

`RestClient` is a synchronous HTTP client that offers a modern, fluent API. It is the successor to `RestTemplate`.

### Implementation (PostService.java)
```java
@Service
public class PostService {
    private final RestClient restClient;

    public PostService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("X-App-Name", "Demo-Application") // Default header
                .build();
    }

    public List<Post> findAll() {
        return restClient.get()
                .uri("/posts")
                .header("X-Source", "Spring-RestClient") // Request-specific header
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }
}
```

**Pros:**
- Fluent, readable API.
- Integrated with Spring's message converters (Jackson).
- Easy error handling using `.onStatus()`.
- Modern replacement for `RestTemplate`.

---

## 2. Java HttpClient (Standard Java 11+)

The `java.net.http.HttpClient` is built into the JDK. It is a good choice when you want to avoid Spring-specific dependencies or need a more low-level control.

### Implementation (UserService.java)
```java
@Service
public class UserService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public User findUserById(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id))
                .header("X-Source", "Java-HttpClient") // Add header
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), User.class);
    }
}
```

**Pros:**
- No external library required (built into JDK).
- Supports both synchronous and asynchronous calls.
- Lightweight and efficient.

---

## 3. WebClient (Spring WebFlux)

`WebClient` is a non-blocking, reactive client for performing HTTP requests. It is the modern standard for reactive applications but can also be used in synchronous applications.

### Implementation (CommentService.java)
```java
@Service
public class CommentService {
    private final WebClient webClient;

    public CommentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    // Synchronous call (Blocking)
    public List<Comment> findAllSync() {
        return webClient.get()
                .uri("/comments")
                .retrieve()
                .bodyToFlux(Comment.class)
                .collectList()
                .block();
    }

    // Asynchronous call (Reactive)
    public Mono<Comment> findByIdAsync(Integer id) {
        return webClient.get()
                .uri("/comments/{id}", id)
                .retrieve()
                .bodyToMono(Comment.class);
    }
}
```

**Pros:**
- Supports non-blocking and reactive programming (Project Reactor).
- Highly performant under high concurrency.
- Built-in support for streaming.
- Can be used in both Servlet (blocking) and Reactive (non-blocking) stacks.

---

## 4. RestTemplate (Legacy / Maintenance Mode)

`RestTemplate` was the standard Spring HTTP client for many years. While it is still widely used, it is now in maintenance mode in favor of `RestClient`.

### Implementation (TodoService.java)
```java
@Service
public class TodoService {
    private final RestTemplate restTemplate;

    public TodoService() {
        this.restTemplate = new RestTemplate();
    }

    public Todo findById(Integer id) {
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Todo> response = restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);
        return response.getBody();
    }
}
```

**Pros:**
- Very well documented (years of community support).
- Simple to use for basic synchronous calls.

**Cons:**
- Large API surface (too many overloaded methods).
- Not as clean or fluent as `RestClient`.
- Maintenance mode (no new features).

---

## Comparison Summary

| Feature | RestClient | Java HttpClient | WebClient | RestTemplate |
| :--- | :--- | :--- | :--- | :--- |
| **Framework** | Spring Boot 3.2+ | Standard JDK 11+ | Spring WebFlux | Spring (Legacy) |
| **JSON Mapping** | Automatic | Manual | Automatic | Automatic |
| **Asynchronous** | No | Yes | Yes (Native) | No |
| **Non-blocking** | No | No | Yes | No |
| **Status** | Active | Active | Active | Maintenance |


## When to use what?

### 1. Use **RestClient** if:
- You are using **Spring Boot 3.2+**.
- Your application is **synchronous/blocking** (Standard Spring MVC).
- You want a clean, modern API with automatic JSON handling via Jackson.
- *Recommended default for most Spring Boot 3.x web applications.*

### 2. Use **WebClient** if:
- You are building a **Reactive application** (Spring WebFlux).
- You need **non-blocking** performance to handle massive concurrency.
- You need to perform complex reactive operations (e.g., merging multiple API streams).
- *Avoid using `.block()` in production unless strictly necessary for legacy integration.*

### 3. Use **Java HttpClient** if:
- You are working in a **plain Java project** without Spring dependencies.
- You want to keep your application **lightweight** with zero external libraries.
- You need a mix of sync and async calls in a non-Spring environment.

### 4. Use **RestTemplate** (Legacy) if:
- You are maintaining an **older Spring project** (Boot < 3.2).
- *Do not use for new projects; it is in maintenance mode.*

