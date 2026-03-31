package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
    // Vendor
    PAN("PAN"),
    GSTIN("GSTIN"),
    BANK_LETTER("Bank Letter"),
    MSME_CERT("MSME Certificate"),
    QUALITY_CERT("Quality Certificate"),

    // Contract
    SIGNED_COPY("Signed Copy"),
    AMENDMENT("Amendment"),
    SUPPORTING("Supporting Document"),
    NDA("NDA");

    private final String displayName;
}

