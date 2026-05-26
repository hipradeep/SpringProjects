package com.hipradeep.code.config;

import com.hipradeep.code.entity.UserEntity;
import com.hipradeep.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

            // Seed Regular User
            userRepository.save(new UserEntity(
                    1L,
                    "user",
                    defaultHashedPassword
            ));

            // Seed Admin User
            userRepository.save(new UserEntity(
                    2L,
                    "admin",
                    defaultHashedPassword
            ));

            System.out.println(">>> Seeded default UserEntities (user, admin) into database!");
        }
    }
}
