package com.hipradeep.code.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseChecker implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("[OK] Database connection successful!");
            System.out.println("URL: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("[NOT] Database connection failed: " + e.getMessage());
        }
    }
}