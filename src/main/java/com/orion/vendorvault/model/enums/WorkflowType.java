package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkflowType {
    CONTRACT_APPROVAL("Contract Approval"),
    AMENDMENT_APPROVAL("Amendment Approval"),
    RENEWAL_APPROVAL("Renewal Approval");

    private final String displayName;
}

