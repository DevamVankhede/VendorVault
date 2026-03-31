package com.orion.vendorvault.controller;

import com.orion.vendorvault.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AnalyticsService analyticsService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("kpis", analyticsService.getDashboardKpi());
        model.addAttribute("statusCounts", analyticsService.getContractsByStatus());
        return "dashboard/index"; // Thymeleaf
    }
}
