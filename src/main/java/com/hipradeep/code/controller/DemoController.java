package com.hipradeep.code.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    // 1. Path-Based Authorization Demonstrations

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public Endpoint: Access Granted (permitAll)";
    }

    @GetMapping("/private")
    public String privateEndpoint(Authentication authentication) {
        return "Private Endpoint: Access Granted (authenticated). Welcome " + authentication.getName() + "!";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication) {
        return "Admin Dashboard Endpoint: Access Granted (hasRole('ADMIN')). Welcome " + authentication.getName() + "!";
    }

    @GetMapping("/delete/resource")
    public String deleteResource(Authentication authentication) {
        return "Delete Resource Endpoint: Access Granted (hasAuthority('OP_DELETE')). Welcome " + authentication.getName() + "!";
    }

    @GetMapping("/restricted")
    public String restrictedEndpoint() {
        return "Restricted Endpoint: This should be blocked for everyone (denyAll)";
    }

    // 2. Method-Level Security Annotation Demonstrations

    @GetMapping("/method/preauthorize-role")
    @PreAuthorize("hasRole('ADMIN')")
    public String preAuthorizeRole(Authentication authentication) {
        return "PreAuthorize Role: Access Granted for ADMIN. User: " + authentication.getName();
    }

    @GetMapping("/method/preauthorize-permission")
    @PreAuthorize("hasAuthority('OP_DELETE')")
    public String preAuthorizePermission(Authentication authentication) {
        return "PreAuthorize Permission: Access Granted for OP_DELETE. User: " + authentication.getName();
    }

    @GetMapping("/method/secured")
    @Secured("ROLE_USER")
    public String securedRole(Authentication authentication) {
        return "Secured Role: Access Granted for ROLE_USER. User: " + authentication.getName();
    }

    @GetMapping("/method/rolesallowed")
    @RolesAllowed("ADMIN")
    public String rolesAllowed(Authentication authentication) {
        return "RolesAllowed: Access Granted for ADMIN. User: " + authentication.getName();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') OR @userService.isResourceOwner(#id, authentication.name)")
    public String getUserDetails(@PathVariable Long id, Authentication authentication) {
        return "Access Granted for user resource with ID " + id + ". Accessed by " + authentication.getName();
    }
}

