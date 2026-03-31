package com.orion.vendorvault.service;

import com.orion.vendorvault.dto.request.PasswordChangeDto;
import com.orion.vendorvault.dto.request.UserCreateRequestDto;
import com.orion.vendorvault.dto.request.UserUpdateRequestDto;
import com.orion.vendorvault.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {
    User createUser(UserCreateRequestDto request);
    User updateUser(Long id, UserUpdateRequestDto request);
    void toggleStatus(Long id);
    void assignRoles(Long id, Set<Long> roleIds);
    void changePassword(Long id, PasswordChangeDto dto);
    User getUserById(Long id);
    Page<User> getAllUsers(Pageable pageable);
    User getUserByEmail(String email);
    void unlockAccount(Long id);
}
