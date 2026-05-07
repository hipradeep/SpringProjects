package com.lib.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LibAutoConfiguration {
    @Bean
    public MyLibService myLibService() {
        return new MyLibServiceImpl();
    }
}
