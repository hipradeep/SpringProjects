package com.test.demo.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

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
