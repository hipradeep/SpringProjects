# Reactive Spring Boot Project

This project demonstrates various Reactive Programming concepts using Spring WebFlux, including `Mono`, `Flux`, and Functional Routing.

## Annotation-based Controller vs. Functional Routing

In Spring WebFlux, you have two ways to define your REST endpoints:

### 1. Annotation-based Controller (`@RestController`)
This is the traditional approach used in Spring MVC.
- **How it works**: Uses `@RestController`, `@RequestMapping`, `@GetMapping`, etc.
- **Pros**: Familiar, consistent with Spring MVC, easy to read for most Spring developers.
- **Cons**: Can feel "magic" due to heavy use of annotations. Routing and logic are coupled in the same class.

### 2. Functional Routing (`RouterFunction` & `HandlerFunction`)
Introduced in Spring WebFlux as a more programmatic and functional alternative.
- **How it works**: Uses `RouterFunction` to define routes and `HandlerFunction` (usually in a separate `Handler` class) to contain the logic.
- **Pros**:
    - **Explicit Routing**: All routes are defined in one place (`RouterConfig`), which is easier to track.
    - **Decoupled**: Handler logic is separated from the routing definition, making it cleaner for testing and reuse.
    - **No "Magic"**: Uses standard Java/Kotlin functional APIs instead of annotation scanning.
    - **Performance**: Slightly faster startup time as it avoids intensive annotation scanning for routes.
- **Cons**: Can have a steeper learning curve for those used to standard Spring MVC.

## Why do we need Routers?
- **Immutability and Composability**: Functional routers are immutable and can be easily composed or shared.
- **Reactive Nature**: The functional style aligns more closely with the reactive programming paradigm (Project Reactor).
- **Control**: You have full control over the request/response flow without relying on Spring's internal annotation processing.
