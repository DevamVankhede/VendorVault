package com.orion.vendorvault.controller;

import com.orion.vendorvault.dto.request.ContractCreateRequestDto;
import com.orion.vendorvault.service.ContractService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final VendorService vendorService;

    @GetMapping
    public String listContracts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String dir,
            @RequestParam(required = false) String status,
            Model model) {
            
        PageRequest pageRequest = PaginationUtil.buildPageRequest(page, size, sort, dir);
        Page<com.orion.vendorvault.model.entity.Contract> contractPage = contractService.getAllContracts(status, pageRequest);

        model.addAttribute("contractPage", contractPage);
        model.addAttribute("pageNumbers", PaginationUtil.getPageNumbers(contractPage));
        model.addAttribute("status", status);
        return "contract/list";
    }

    @GetMapping("/{id}")
    public String viewContract(@PathVariable Long id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id));
        return "contract/view";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) Long vendorId, Model model) {
        ContractCreateRequestDto dto = new ContractCreateRequestDto();
        if (vendorId != null) {
            dto.setVendorId(vendorId);
        }
        model.addAttribute("contractDTO", dto);
        // Would fetch list of vendors using paginated request or REST endpoint for dropdown
        return "contract/form";
    }

    @PostMapping("/create")
    public String processCreate(@Valid @ModelAttribute("contractDTO") ContractCreateRequestDto dto,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("contractDTO", dto);
            return "contract/form";
        }
        try {
            com.orion.vendorvault.model.entity.Contract c = contractService.createContract(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Contract created: " + c.getContractNumber());
            return "redirect:/contracts/" + c.getId();
        } catch (Exception e) {
            model.addAttribute("contractDTO", dto);
            model.addAttribute("errorMessage", e.getMessage());
            return "contract/form";
        }
    }
    
    @PostMapping("/{id}/submit")
    public String submitForApproval(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.submitForApproval(id);
            redirectAttributes.addFlashAttribute("successMessage", "Contract submitted for approval.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/contracts/" + id;
    }
}
