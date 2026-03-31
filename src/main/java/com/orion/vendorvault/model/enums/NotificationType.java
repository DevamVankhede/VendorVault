package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    APPROVAL_REQUIRED("Approval Required"),
    CONTRACT_APPROVED("Contract Approved"),
    CONTRACT_REJECTED("Contract Rejected"),
    EXPIRY_ALERT("Expiry Alert"),
    CONTRACT_EXPIRED("Contract Expired"),
    SLA_BREACH_ALERT("SLA Breach Alert"),
    VENDOR_BLACKLISTED("Vendor Blacklisted"),
    ACCOUNT_CREATED("Account Created"),
    ACCOUNT_LOCKED("Account Locked"),
    EVALUATION_FINALIZED("Evaluation Finalized"),
    EMAIL_DISPATCH_FAILED("Email Dispatch Failed");

    private final String displayName;
}

