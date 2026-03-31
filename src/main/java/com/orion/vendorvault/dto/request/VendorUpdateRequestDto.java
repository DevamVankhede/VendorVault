package com.orion.vendorvault.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorUpdateRequestDto {

    @NotBlank
    @Size(min = 2, max = 200)
    private String name;

    @Size(max = 120)
    private String category;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GSTIN format")
    private String gstin;

    @Size(max = 32)
    private String msmeCategory;

    @Size(max = 32)
    private String status;

    @Size(max = 500)
    private String blacklistReason;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 80)
    private String city;

    @Size(max = 80)
    private String state;

    @Size(max = 12)
    private String pincode;

    @Size(max = 120)
    private String contactPerson;

    @Email
    @Size(max = 190)
    private String contactEmail;

    @Size(max = 32)
    private String contactPhone;
}

