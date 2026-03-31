package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateDto {
    @NotBlank
    @Size(min = 2, max = 120)
    private String fullName;

    @Size(max = 120)
    private String department;

    @Size(max = 120)
    private String designation;
}

