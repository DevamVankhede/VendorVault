package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalAction {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DELEGATED("Delegated"),
    ESCALATED("Escalated");

    private final String displayName;
}

