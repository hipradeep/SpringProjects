package com.hipradeep.code.repository;

import com.hipradeep.code.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}