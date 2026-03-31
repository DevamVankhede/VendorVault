package com.orion.vendorvault.model.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditEventType {
    public static final String CONTRACT_CREATED = "CONTRACT_CREATED";
    public static final String CONTRACT_SUBMITTED = "CONTRACT_SUBMITTED";
    public static final String CONTRACT_APPROVED_STEP = "CONTRACT_APPROVED_STEP";
    public static final String CONTRACT_APPROVED_FINAL = "CONTRACT_APPROVED_FINAL";
    public static final String CONTRACT_REJECTED = "CONTRACT_REJECTED";
    public static final String CONTRACT_ACTIVATED = "CONTRACT_ACTIVATED";
    public static final String CONTRACT_EXPIRY_ALERT_SENT = "CONTRACT_EXPIRY_ALERT_SENT";
    public static final String CONTRACT_EXPIRED = "CONTRACT_EXPIRED";
    public static final String CONTRACT_TERMINATED = "CONTRACT_TERMINATED";
    public static final String CONTRACT_RENEWED = "CONTRACT_RENEWED";

    public static final String VENDOR_CREATED = "VENDOR_CREATED";
    public static final String VENDOR_ACTIVATED = "VENDOR_ACTIVATED";
    public static final String VENDOR_BLACKLISTED = "VENDOR_BLACKLISTED";

    public static final String DOCUMENT_UPLOADED = "DOCUMENT_UPLOADED";
    public static final String DOCUMENT_VERIFIED = "DOCUMENT_VERIFIED";

    public static final String USER_CREATED = "USER_CREATED";
    public static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    public static final String USER_LOGGED_OUT = "USER_LOGGED_OUT";
    public static final String PASSWORD_CHANGED = "PASSWORD_CHANGED";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String UNAUTHORIZED_ACCESS_ATTEMPT = "UNAUTHORIZED_ACCESS_ATTEMPT";

    public static final String WORKFLOW_CREATED = "WORKFLOW_CREATED";
    public static final String STEP_APPROVED = "STEP_APPROVED";
    public static final String STEP_REJECTED = "STEP_REJECTED";

    public static final String SLA_BREACH_DETECTED = "SLA_BREACH_DETECTED";
    public static final String EMAIL_DISPATCH_FAILED = "EMAIL_DISPATCH_FAILED";
}

