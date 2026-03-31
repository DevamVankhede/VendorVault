package com.orion.vendorvault.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvaluationStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    FINALIZED("Finalized");

    private final String displayName;
}

