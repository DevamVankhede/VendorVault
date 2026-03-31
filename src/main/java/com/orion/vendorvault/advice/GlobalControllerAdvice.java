package com.orion.vendorvault.advice;

import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.repository.ApprovalStepRepository;
import com.orion.vendorvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final ApprovalStepRepository approvalStepRepository;
    private final UserRepository userRepository;

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("pendingCount")
    public long pendingCount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return userRepository.findByEmail(authentication.getName())
                    .map(user -> (long) approvalStepRepository.findPendingStepsForUser(user.getId()).size())
                    .orElse(0L);
        }
        return 0L;
    }
}
