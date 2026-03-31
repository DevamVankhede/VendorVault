package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlaType {
    DELIVERY("Delivery"),
    QUALITY("Quality"),
    RESPONSE_TIME("Response Time");

    private final String displayName;
}

