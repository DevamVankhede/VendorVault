package com.orion.vendorvault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDocumentDto {
    private Long id;
    private String documentType;
    private String filePath;
    private String originalFileName;
    private boolean verified;
    private LocalDateTime uploadedAt;
    private LocalDateTime verifiedAt;
}

