package com.orion.vendorvault.controller;

import com.orion.vendorvault.dto.request.VendorCreateRequestDto;
import com.orion.vendorvault.dto.request.VendorUpdateRequestDto;
import com.orion.vendorvault.service.VendorService;
import com.orion.vendorvault.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public String listVendors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "vendorCode") String sort,
            @RequestParam(defaultValue = "ASC") String dir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Model model) {
            
        Page<com.orion.vendorvault.model.entity.Vendor> vendorPage;
        PageRequest pageRequest = PaginationUtil.buildPageRequest(page, size, sort, dir);

        if (search != null && !search.isBlank()) {
            vendorPage = vendorService.searchVendors(search, status, pageRequest);
        } else {
            vendorPage = vendorService.getAllVendors(status, pageRequest);
        }

        model.addAttribute("vendorPage", vendorPage);
        model.addAttribute("pageNumbers", PaginationUtil.getPageNumbers(vendorPage));
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        return "vendor/list";
    }

    @GetMapping("/{id}")
    public String viewVendor(@PathVariable Long id, Model model) {
        model.addAttribute("vendor", vendorService.getVendorById(id));
        return "vendor/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vendorDTO", new VendorCreateRequestDto());
        return "vendor/form";
    }

    @PostMapping("/create")
    public String processCreate(@Valid @ModelAttribute("vendorDTO") VendorCreateRequestDto dto,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "vendor/form";
        }
        try {
            com.orion.vendorvault.model.entity.Vendor v = vendorService.createVendor(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor created successfully. Code: " + v.getVendorCode());
            return "redirect:/vendors/" + v.getId();
        } catch (Exception e) {
            result.reject("globalError", e.getMessage());
            return "vendor/form";
        }
    }

    @PostMapping("/{id}/upload")
    public String uploadDocument(@PathVariable Long id, 
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("documentType") String type,
                                 RedirectAttributes redirectAttributes) {
        vendorService.uploadDocument(id, type, file);
        redirectAttributes.addFlashAttribute("successMessage", "Document uploaded successfully");
        return "redirect:/vendors/" + id;
    }
}
