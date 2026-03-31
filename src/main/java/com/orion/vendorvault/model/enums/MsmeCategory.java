package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MsmeCategory {
    MICRO("Micro"),
    SMALL("Small"),
    MEDIUM("Medium"),
    NONE("None");

    private final String displayName;
}

