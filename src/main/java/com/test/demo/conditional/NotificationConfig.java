package com.test.demo.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    /**
     * This bean will only be created if NO other bean of type NotificationService
     * exists in the Application Context.
     */
    @Bean
    @ConditionalOnMissingBean(NotificationService.class)
    public NotificationService defaultNotificationService() {
        return new DefaultNotificationService();
    }
}
