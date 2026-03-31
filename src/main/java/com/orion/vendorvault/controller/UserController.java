package com.orion.vendorvault.controller;

import com.orion.vendorvault.dto.request.UserCreateRequestDto;
import com.orion.vendorvault.service.UserService;
import com.orion.vendorvault.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String dir,
            Model model) {
            
        PageRequest pageRequest = PaginationUtil.buildPageRequest(page, size, sort, dir);
        Page<com.orion.vendorvault.model.entity.User> userPage = userService.getAllUsers(pageRequest);

        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", PaginationUtil.getPageNumbers(userPage));
        return "user/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("userDTO", new UserCreateRequestDto());
        return "user/form"; // Thymeleaf
    }

    @PostMapping("/create")
    public String processCreate(@Valid @ModelAttribute("userDTO") UserCreateRequestDto dto,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/form";
        }
        try {
            userService.createUser(dto);
            redirectAttributes.addFlashAttribute("successMessage", "User successfully created.");
            return "redirect:/users";
        } catch (Exception e) {
            result.reject("globalError", e.getMessage());
            return "user/form";
        }
    }
    
    @PostMapping("/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "User status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
