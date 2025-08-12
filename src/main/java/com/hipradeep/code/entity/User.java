package com.hipradeep.code.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
@Data
public class User {
    private String username;
    private String password;
    private String email;
    private Set<String> roles;

    public User(String username, String password, String email, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

}