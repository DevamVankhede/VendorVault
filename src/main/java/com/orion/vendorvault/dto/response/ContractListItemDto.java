package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractListItemDto {
    private Long id;
    private String contractNumber;
    private String title;
    private String vendorName;
    private String status;
    private BigDecimal contractValue;
    private LocalDate endDate;
}

