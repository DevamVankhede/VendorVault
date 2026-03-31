package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlaStatus {
    ON_TIME("On Time"),
    AT_RISK("At Risk"),
    BREACHED("Breached");

    private final String displayName;
}

