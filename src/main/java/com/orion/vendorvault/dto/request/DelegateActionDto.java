package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DelegateActionDto {
    @NotNull
    private Long delegateToUserId;
    @NotBlank
    private String comments;
}
