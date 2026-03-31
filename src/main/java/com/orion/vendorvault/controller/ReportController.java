package com.orion.vendorvault.controller;

import com.orion.vendorvault.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class ReportController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("exposureData", analyticsService.getContractExposureReport());
        model.addAttribute("performanceData", analyticsService.getVendorPerformanceMetrics());
        return "analytics/reports";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export() {
        byte[] pdf = analyticsService.generateContractReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract-kpi-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csv = analyticsService.generateCsvContractReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract-exposure.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
