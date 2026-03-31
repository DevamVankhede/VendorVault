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
public class ContractCreateRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotNull(message = "Vendor is required")
    private Long vendorId;

    @NotBlank(message = "Contract type is required")
    private String contractType;

    @Size(max = 120, message = "Department name too long")
    private String department;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Contract value is required")
    @DecimalMin(value = "0.01", message = "Contract value must be greater than 0")
    private BigDecimal contractValue;

    @Size(max = 8)
    private String currency;

    @Size(max = 5000)
    private String paymentTerms;
}

