package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDto {
    private Long id;
    private String contractNumber;
    private String title;
    private Long vendorId;
    private String vendorName;
    private String contractType;
    private String status;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractValue;
    private String currency;
    private String paymentTerms;
    private Long renewalOfContractId;
    private String terminationReason;
    private LocalDateTime terminatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContractDocumentDto> documents;
}

