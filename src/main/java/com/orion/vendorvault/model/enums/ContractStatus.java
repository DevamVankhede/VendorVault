package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    DRAFT("Draft"),
    PENDING_APPROVAL("Pending Approval"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    ACTIVE("Active"),
    EXPIRING_SOON("Expiring Soon"),
    EXPIRED("Expired"),
    TERMINATED("Terminated"),
    RENEWED("Renewed");

    private final String displayName;
}

