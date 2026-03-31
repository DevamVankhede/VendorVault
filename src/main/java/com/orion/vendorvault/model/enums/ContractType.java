package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractType {
    PURCHASE("Purchase"),
    SERVICE("Service"),
    AMC("AMC"),
    LEASE("Lease"),
    FRAMEWORK("Framework");

    private final String displayName;
}

