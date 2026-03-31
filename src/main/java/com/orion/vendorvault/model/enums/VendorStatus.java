package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VendorStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLACKLISTED("Blacklisted"),
    UNDER_REVIEW("Under Review");

    private final String displayName;
}

