package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
public class RenewalRequestDto {
    @NotNull
    private Long contractId;
    private String newTitle;
    private BigDecimal newContractValue;
    @NotNull
    @FutureOrPresent
    private LocalDate newStartDate;
    @NotNull
    @FutureOrPresent
    private LocalDate newEndDate;
}
