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

The `java.net.http.HttpClient` (introduced in Java 9, standardized in Java 11) is built into the JDK. It supports both synchronous and asynchronous non-blocking requests.

### Implementation 

**Synchronous Example (UserService.java):**
```java
    public User findUserById(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + id))
                .header("X-Source", "Java-HttpClient")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), User.class);
    }
```

**Asynchronous Example (AlbumService.java):**
```java
    public CompletableFuture<Album> findByIdAsync(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/albums/" + id))
                .GET()
                .build();

        // sendAsync returns a CompletableFuture
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), Album.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
```

**Pros:**
- No external library required (built into JDK).
- Native support for asynchronous programming (`CompletableFuture`).
- Supports HTTP/2 natively.
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

## 4. HttpURLConnection (Java 8 Legacy)

Before Java 11's `HttpClient`, `HttpURLConnection` was the standard way to perform HTTP requests in the JDK. It is much more verbose as it requires manual handling of streams and status codes.

### Implementation (PhotoService.java)
```java
@Service
public class PhotoService {
    public Photo findById(Integer id) {
        URL url = new URL("https://jsonplaceholder.typicode.com/photos/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // Read stream and deserialize manually...
        }
    }
}
```

**Pros:**
- Available in all Java versions (very old legacy support).
- No external dependencies.

**Cons:**
- Extremely verbose and boilerplate-heavy.
- No built-in JSON support.
- Difficult to handle timeouts and complex headers.

---

## 5. RestTemplate (Legacy / Maintenance Mode)

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

| Feature | RestClient | Java 11+ HttpClient | WebClient | HttpURLConnection | RestTemplate |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Framework** | Spring Boot 3.2+ | Standard JDK 11+ | Spring WebFlux | Standard JDK (Legacy) | Spring (Legacy) |
| **JSON Mapping** | Automatic | Manual | Automatic | Manual | Automatic |
| **Asynchronous** | No | Yes | Yes (Native) | No | No |
| **Non-blocking** | No | No | Yes | No | No |
| **Status** | Active | Active | Active | Legacy | Maintenance |

## When to use what?

### 1. Use **RestClient** if:
- You are using **Spring Boot 3.2+**.
- Your application is **synchronous/blocking** (Standard Spring MVC).
- *Recommended default for most Spring Boot 3.x web applications.*

### 2. Use **WebClient** if:
- You are building a **Reactive application** (Spring WebFlux).
- You need **non-blocking** performance to handle massive concurrency.

### 3. Use **Java 11+ HttpClient** if:
- You are working in a **plain Java project** (JDK 11+) without Spring dependencies.
- You want to keep your application **lightweight** with zero external libraries.

### 4. Use **HttpURLConnection** (Java 8) if:
- You are forced to work with **legacy Java 8** environments and cannot add external libraries.
- *Generally avoided in modern development due to verbosity.*

### 5. Use **RestTemplate** (Legacy) if:
- You are maintaining an **older Spring project** (Boot < 3.2).
- *Do not use for new projects; it is in maintenance mode.*

