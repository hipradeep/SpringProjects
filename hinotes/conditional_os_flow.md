# @Conditional: OS-Based Bean Selection Flow

This note explains the flow and implementation of selecting different beans based on the Operating System using Spring's `@Conditional` annotation.

## 1. Implementation Components

### A. The Conditions
These classes check the system environment at startup.
- **`WindowsCondition`**: Returns `true` if `os.name` contains "Windows".
- **`LinuxCondition`**: Returns `true` if `os.name` contains "Linux".

### B. The Service
A simple interface `MessageService` with two implementations:
- `WindowsMessageService`
- `LinuxMessageService`

### C. The Configuration
The magic happens here using the `@Conditional` annotation:

```java
@Configuration
public class ConditionalConfig {

    @Bean
    @Conditional(WindowsCondition.class)
    public MessageService windowsMessageService() {
        return new WindowsMessageService();
    }

    @Bean
    @Conditional(LinuxCondition.class)
    public MessageService linuxMessageService() {
        return new LinuxMessageService();
    }
}
```

---

## 2. Visual Execution Flow

```mermaid
sequenceDiagram
    participant Boot as Spring Boot Startup
    participant Registry as Bean Definition Registry
    participant WinCond as WindowsCondition
    participant LinCond as LinuxCondition
    participant Config as ConditionalConfig

    Boot->>Registry: Scan for @Configuration classes
    Registry->>Config: Identify Bean Methods
    
    rect rgb(240, 248, 255)
    Note over Config, WinCond: Evaluating windowsMessageService
    Config->>WinCond: matches()
    WinCond->>WinCond: Check os.name (is Windows?)
    WinCond-->>Config: returns true
    Config->>Registry: Register "windowsMessageService" Bean
    end

    rect rgb(255, 240, 245)
    Note over Config, LinCond: Evaluating linuxMessageService
    Config->>LinCond: matches()
    LinCond->>LinCond: Check os.name (is Linux?)
    LinCond-->>Config: returns false
    Note right of Config: Bean is IGNORED
    end
```

---

## 3. Step-by-Step Flow Description

1.  **Context Initialization**: During startup, Spring Boot scans all classes marked with `@Configuration`.
2.  **Condition Checking**: For every method marked with `@Bean` and `@Conditional(Condition.class)`:
    *   Spring instantiates the specified `Condition` class.
    *   It calls the `matches(ConditionContext context, AnnotatedTypeMetadata metadata)` method.
3.  **The "Return" Decision**:
    *   The `matches()` method must return a **boolean**.
    *   If **`true`**: The method is executed, and the returned object is registered as a Spring Bean.
    *   If **`false`**: The method is skipped entirely. No bean is created.
4.  **Injection**: When other components (like a Controller) ask for a `MessageService` bean via `@Autowired`, Spring looks into the registry and finds the one that was successfully created (e.g., the Windows version).
5.  **Runtime**: At runtime, the application only "sees" the bean that matched the condition. The other implementation doesn't even exist in the application's memory.
