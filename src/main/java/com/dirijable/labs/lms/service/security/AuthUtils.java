package com.dirijable.labs.lms.service.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    public UserDetails userDetailsFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("You are not authorized to do this");
        }
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails;
        }
        throw new AccessDeniedException("You are not authorized to do this");
    }
}
