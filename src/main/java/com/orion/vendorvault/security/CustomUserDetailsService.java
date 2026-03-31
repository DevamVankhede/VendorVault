package com.orion.vendorvault.security;

import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username (email): {}", username);
        User user = userRepository.findByEmail(username)
                .orElseGet(() -> {
                    log.warn("User not found with email: {}", username);
                    throw new UsernameNotFoundException("User not found with email: " + username);
                });
        log.info("User found: {}, password hash exists: {}", user.getEmail(), user.getPasswordHash() != null);

        if (!user.isActive()) {
            throw new org.springframework.security.authentication.DisabledException("User account is disabled");
        }
        if (user.isAccountLocked()) {
            throw new org.springframework.security.authentication.LockedException("User account is locked");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.getRoles().stream()
                        .map(role -> {
                            String roleName = role.getRoleName();
                            if (!roleName.startsWith("ROLE_")) {
                                roleName = "ROLE_" + roleName;
                            }
                            return new SimpleGrantedAuthority(roleName);
                        })
                        .collect(Collectors.toList())
        );
    }
}
