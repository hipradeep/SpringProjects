package com.hipradeep.code.config;

import com.hipradeep.code.entity.User;
import com.hipradeep.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            String defaultHashedPassword = passwordEncoder.encode("password");

            // Seed Admin
            userRepository.save(new User(
                    "admin",
                    defaultHashedPassword,
                    "admin@example.com",
                    Set.of("ADMIN", "USER")
            ));

            // Seed Regular User
            userRepository.save(new User(
                    "user",
                    defaultHashedPassword,
                    "user@example.com",
                    Set.of("USER")
            ));

            // Seed Manager
            userRepository.save(new User(
                    "manager",
                    defaultHashedPassword,
                    "manager@example.com",
                    Set.of("MANAGER", "USER")
            ));

            System.out.println(">>> Seeded default users (admin, user, manager) into PostgreSQL database!");
        }
    }
}
