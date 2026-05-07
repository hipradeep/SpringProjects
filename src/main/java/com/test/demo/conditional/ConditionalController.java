package com.test.demo.conditional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConditionalController {

    @Autowired(required = false)
    private MessageService messageService;

    @Autowired(required = false)
    private String featureFlagBean;

    @Autowired
    private NotificationService notificationService;

    /**
     * Flow of @Conditional OS-based selection:
     * 
     * sequenceDiagram
     *     participant Boot as Spring Boot Startup
     *     participant Registry as Bean Definition Registry
     *     participant WinCond as WindowsCondition
     *     participant LinCond as LinuxCondition
     *     participant Config as ConditionalConfig
     *
     *     Boot->>Registry: Scan for @Configuration classes
     *     Registry->>Config: Identify Bean Methods (windowsMessageService, linuxMessageService)
     *     
     *     Note over Config, WinCond: Evaluating windowsMessageService
     *     Config->>WinCond: matches(context, metadata)
     *     WinCond->>WinCond: Check System.getProperty("os.name")
     *     WinCond-->>Config: returns true (Current OS is Windows)
     *     Config->>Registry: Register "windowsMessageService" Bean
     *     
     *     Note over Config, LinCond: Evaluating linuxMessageService
     *     Config->>LinCond: matches(context, metadata)
     *     LinCond->>LinCond: Check System.getProperty("os.name")
     *     LinCond-->>Config: returns false
     *     Note right of Config: Bean is IGNORED
     * 
     * How it works:
     * 1. The @Conditional annotation takes a Class (that implements the Condition interface).
     * 2. That class has a method called matches().
     * 3. The matches() method must return a boolean:
     *    - return true: Spring says "The condition is met! I will create this bean."
     *    - return false: Spring says "The condition failed! I will ignore this bean."
     */
    @GetMapping("/conditional-message")
    public String getMessage() {
        if (messageService == null) {
            return "No MessageService bean found for this OS environment.";
        }
        return messageService.getMessage();
    }

    @GetMapping("/feature-status")
    public String getFeatureStatus() {
        return featureFlagBean != null ? featureFlagBean : "Feature bean not found";
    }

    @GetMapping("/notify")
    public String notifyUser() {
        return notificationService.send("Hello from @ConditionalOnMissingBean!");
    }
}
