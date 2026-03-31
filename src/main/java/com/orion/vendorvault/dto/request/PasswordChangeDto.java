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
public class PasswordChangeDto {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 50)
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 50)
    private String confirmPassword;
}
