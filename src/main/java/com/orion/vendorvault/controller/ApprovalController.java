package com.orion.vendorvault.controller;

import com.orion.vendorvault.dto.request.ApprovalActionDto;
import com.orion.vendorvault.service.ApprovalService;
import com.orion.vendorvault.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/pending")
    public String pendingApprovals(Model model) {
        Long userId = SecurityUtil.getCurrentUserId();
        model.addAttribute("pendingSteps", approvalService.getPendingQueueForUser(userId));
        return "approval/queue";
    }

    @PostMapping("/{stepId}/approve")
    public String processApprove(@PathVariable Long stepId, 
                                 @Valid @ModelAttribute("actionDTO") ApprovalActionDto actionDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Provide comments.");
            return "redirect:/approvals/pending";
        }
        try {
            approvalService.approve(stepId, actionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Step approved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/approvals/pending";
    }

    @PostMapping("/{stepId}/reject")
    public String processReject(@PathVariable Long stepId, 
                                @Valid @ModelAttribute("actionDTO") ApprovalActionDto actionDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Provide comments.");
            return "redirect:/approvals/pending";
        }
        try {
            approvalService.reject(stepId, actionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Step rejected successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/approvals/pending";
    }
}
