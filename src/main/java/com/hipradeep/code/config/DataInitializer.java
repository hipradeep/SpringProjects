package com.hipradeep.code.config;

import com.hipradeep.code.entity.RoleEntity;
import com.hipradeep.code.entity.UserEntity;
import com.hipradeep.code.repository.RoleRepository;
import com.hipradeep.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new RoleEntity(null, "ROLE_USER")));

        RoleEntity adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new RoleEntity(null, "ROLE_ADMIN")));

        // 2. Seed Users
        if (userRepository.count() == 0) {
            String defaultHashedPassword = passwordEncoder.encode("password");

            // Seed Regular User
            UserEntity user = new UserEntity();
            user.setId(1L);
            user.setUsername("user");
            user.setPassword(defaultHashedPassword);
            user.setRoles(new HashSet<>(List.of(userRole)));
            userRepository.save(user);

            // Seed Admin User
            UserEntity admin = new UserEntity();
            admin.setId(2L);
            admin.setUsername("admin");
            admin.setPassword(defaultHashedPassword);
            admin.setRoles(new HashSet<>(List.of(userRole, adminRole)));
            userRepository.save(admin);

            System.out.println(">>> Seeded default UserEntities (user, admin) and Roles into database!");
        }
    }
}
