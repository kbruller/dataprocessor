package com.example.dataprocessor.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityCheck")
public class SecurityCheckService {

    /**
     * Custom business logic for verifying record ownership.
     * @param categoryId The ID of the category to be checked (received from the Service via #id)
     */
    public boolean isOwner(Long categoryId) {
        // 1. Retrieve the name of the currently logged-in user from the Spring Security context
        String currentUsername = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("anonymous");

        // Here we can simulate the logic:
        // In a production environment, you would query the category from the categoryRepository here,
        // and check whether 'entity.getCreatedBy()' matches 'currentUsername'.

        // Let's assume that the user named "user" is the owner only of categories with even IDs
        if ("user".equals(currentUsername)) {
            return categoryId % 2 == 0;
        }

        // In all other cases (e.g., if there is no user with that name, or if no one is logged in yet)
        return false;
    }
}