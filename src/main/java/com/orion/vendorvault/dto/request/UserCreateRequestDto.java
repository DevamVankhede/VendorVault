package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDto {
    @NotBlank
    @Size(max = 120)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 190)
    private String email;

    @Size(max = 120)
    private String department;

    @Size(max = 120)
    private String designation;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    @NotNull
    private Set<Long> roleIds;
}
