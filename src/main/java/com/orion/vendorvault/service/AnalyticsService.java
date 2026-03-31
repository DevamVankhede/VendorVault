package com.orion.vendorvault.service;

import com.orion.vendorvault.dto.response.DashboardKpiDto;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    DashboardKpiDto getDashboardKpi();
    Map<String, Long> getContractsByStatus();
    List<Map<String, Object>> getContractExposureReport();
    List<Map<String, Object>> getVendorPerformanceMetrics();
    byte[] generateContractReport();
    byte[] generateCsvContractReport();
    byte[] generateVendorPerformanceReport();
}
