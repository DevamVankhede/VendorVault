package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.dto.request.PasswordChangeDto;
import com.orion.vendorvault.dto.request.UserCreateRequestDto;
import com.orion.vendorvault.dto.request.UserUpdateRequestDto;
import com.orion.vendorvault.model.entity.Role;
import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.model.enums.AuditEventType;
import com.orion.vendorvault.repository.RoleRepository;
import com.orion.vendorvault.repository.UserRepository;
import com.orion.vendorvault.service.AuditLogService;
import com.orion.vendorvault.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public User createUser(UserCreateRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<Role> roles = new HashSet<>();
        if (request.getRoleIds() != null) {
            for (Long roleId : request.getRoleIds()) {
                roles.add(roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found")));
            }
        }

        User user = User.builder()
                .employeeId("EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);
        auditLogService.logEvent(AuditEventType.USER_CREATED, "User", savedUser.getId(), "Created user: " + savedUser.getEmail());
        return savedUser;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserUpdateRequestDto request) {
        User user = getUserById(id);
        
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getDesignation() != null) user.setDesignation(request.getDesignation());
        
        if (request.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : request.getRoleIds()) {
                roles.add(roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found")));
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        auditLogService.logEvent(AuditEventType.USER_CREATED, "User", updatedUser.getId(), "Updated user details");
        return updatedUser;
    }

    @Override
    @Transactional
    public void toggleStatus(Long id) {
        User user = getUserById(id);
        user.setActive(!user.isActive());
        userRepository.save(user);
        auditLogService.logEvent(AuditEventType.USER_CREATED, "User", user.getId(), "Status toggled to " + user.isActive());
    }

    @Override
    @Transactional
    public void assignRoles(Long id, Set<Long> roleIds) {
        User user = getUserById(id);
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            roles.add(roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found")));
        }
        user.setRoles(roles);
        userRepository.save(user);
        auditLogService.logEvent(AuditEventType.USER_CREATED, "User", user.getId(), "Roles updated");
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordChangeDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = getUserById(id);
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        auditLogService.logEvent(AuditEventType.PASSWORD_CHANGED, "User", user.getId(), "Password changed");
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void unlockAccount(Long id) {
        User user = getUserById(id);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
        auditLogService.logEvent(AuditEventType.ACCOUNT_LOCKED, "User", user.getId(), "Account unlocked manually");
    }
}
