package com.orion.vendorvault.util;

import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.security.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            // Extract user ID from username (email)
            return 1L; // Default to admin for now
        }
        return 1L; // Default to admin
    }
    
    public static User getCurrentUser() {
        // Return null for now - will be populated by service layer
        return null;
    }
    
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
            return auth.getName();
        }
        return "admin@orion.com"; // Default to admin
    }
    
    public static boolean hasRole(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(roleName));
    }
    
    public static boolean isOwner(Long resourceOwnerId) {
        return getCurrentUserId().equals(resourceOwnerId);
    }
}
