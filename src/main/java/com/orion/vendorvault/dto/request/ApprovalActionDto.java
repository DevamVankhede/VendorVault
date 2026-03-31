package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApprovalActionDto {
    @NotBlank
    private String comments;
}
