package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.dto.response.DashboardKpiDto;
import com.orion.vendorvault.model.enums.ContractStatus;
import com.orion.vendorvault.repository.ApprovalStepRepository;
import com.orion.vendorvault.repository.ContractRepository;
import com.orion.vendorvault.repository.PerformanceEvaluationRepository;
import com.orion.vendorvault.service.AnalyticsService;
import com.orion.vendorvault.util.ReportGeneratorUtil;
import com.orion.vendorvault.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ContractRepository contractRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final ReportGeneratorUtil reportGeneratorUtil;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboard-kpi", key = "#root.methodName")
    public DashboardKpiDto getDashboardKpi() {
        long activeContracts = contractRepository.countByStatus(ContractStatus.ACTIVE);
        BigDecimal totalValue = contractRepository.sumContractValueByStatus(ContractStatus.ACTIVE);
        
        LocalDate today = LocalDate.now();
        long expiring = contractRepository.findContractsExpiringBetween(today, today.plusDays(30)).size();
        
        // Use a 1 for pending approvals if anonymous or calculate properly
        Long userId = SecurityUtil.getCurrentUserId();
        long pending = approvalStepRepository.findPendingStepsForUser(userId).size();

        return DashboardKpiDto.builder()
                .activeContracts(activeContracts)
                .totalActiveValue(totalValue != null ? totalValue : BigDecimal.ZERO)
                .expiringIn30Days(expiring)
                .pendingApprovals(pending)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getContractsByStatus() {
        List<Object[]> counts = contractRepository.getContractStatusCounts();
        Map<String, Long> map = new HashMap<>();
        for (Object[] r : counts) {
            map.put(r[0].toString(), (Long) r[1]);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getContractExposureReport() {
        List<Object[]> results = contractRepository.getFinancialExposureByDepartment();
        return results.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("department", r[0]);
            map.put("exposure", r[1]);
            return map;
        }).toList();
    }

    @Override
    public byte[] generateContractReport() {
        return reportGeneratorUtil.generatePdfReport(null);
    }

    @Override
    public byte[] generateCsvContractReport() {
        return reportGeneratorUtil.generateCsvReport(getContractExposureReport());
    }

    @Override
    public byte[] generateVendorPerformanceReport() {
        return reportGeneratorUtil.generatePdfReport(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getVendorPerformanceMetrics() {
        return performanceEvaluationRepository.findAll().stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("vendor", p.getVendor().getName());
            map.put("quality", p.getQualityScore());
            map.put("delivery", p.getDeliveryScore());
            map.put("communication", p.getCommunicationScore());
            map.put("compliance", p.getComplianceScore());
            return map;
        }).limit(5).toList();
    }
}
