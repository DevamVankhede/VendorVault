package com.orion.vendorvault.dto.request;

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
public class UserUpdateRequestDto {
    @Size(max = 120)
    private String fullName;

    @Size(max = 120)
    private String department;

    @Size(max = 120)
    private String designation;

    private Set<Long> roleIds;
}
