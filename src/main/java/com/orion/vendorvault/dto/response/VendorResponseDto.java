package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorResponseDto {
    private Long id;
    private String vendorCode;
    private String name;
    private String category;
    private String pan;
    private String gstin;
    private String msmeCategory;
    private String status;
    private String blacklistReason;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<VendorDocumentDto> documents;
}

