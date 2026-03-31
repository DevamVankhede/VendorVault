package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ContractUpdateRequestDto {

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @NotNull
    private Long vendorId;

    @NotBlank
    private String contractType;

    @NotBlank
    private String status;

    @Size(max = 120)
    private String department;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal contractValue;

    @Size(max = 8)
    private String currency;

    @Size(max = 5000)
    private String paymentTerms;
}

