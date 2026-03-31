package com.orion.vendorvault.dto.request;

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
public class AmendmentCreateDto {
    @NotNull
    private Long contractId;

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    @Size(min = 5, max = 5000)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDate effectiveDate;

    private BigDecimal addedValue;
}
