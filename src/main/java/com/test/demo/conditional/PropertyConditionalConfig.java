package com.test.demo.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConditionalConfig {

    @Bean
    @ConditionalOnProperty(name = "app.feature.enabled", havingValue = "true")
    public String featureFlagBean() {
        return "Feature is ENABLED";
    }

    @Bean
    @ConditionalOnProperty(name = "app.feature.enabled", havingValue = "false", matchIfMissing = true)
    public String defaultFeatureBean() {
        return "Feature is DISABLED (Default)";
    }
}
