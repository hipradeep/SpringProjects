package com.hipradeep.code.repository;

import com.hipradeep.code.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> users;

    public UserRepositoryImpl() {
        this.users = new HashMap<>();
        initializeUsers();
    }

    private void initializeUsers() {
        // Admin user
        users.put("admin", new User(
                "admin",
                "$2a$10$qZG22TWZ3YuztYW.FtSCP.x0EucBOGycpuHAtiMa2rEfnE4/uO8uS", // "password"
                "admin@example.com",
                Set.of("ADMIN", "USER")
        ));

        // Regular user
        users.put("user", new User(
                "user",
                "$2a$10$qZG22TWZ3YuztYW.FtSCP.x0EucBOGycpuHAtiMa2rEfnE4/uO8uS", // "password"
                //"password",
                "user@example.com",
                Set.of("USER")
        ));

        // Manager user
        users.put("manager", new User(
                "manager",
                "$2a$10$qZG22TWZ3YuztYW.FtSCP.x0EucBOGycpuHAtiMa2rEfnE4/uO8uS", // "password"
                "manager@example.com",
                Set.of("MANAGER", "USER")
        ));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
