package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrendIndicator {
    IMPROVING("Improving"),
    STABLE("Stable"),
    DECLINING("Declining");

    private final String displayName;
}

