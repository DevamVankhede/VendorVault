package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardKpiDto {
    private long activeContracts;
    private BigDecimal totalActiveValue;
    private long expiringIn30Days;
    private long pendingApprovals;
}
