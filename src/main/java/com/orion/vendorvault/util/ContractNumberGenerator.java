package com.orion.vendorvault.util;

import org.springframework.stereotype.Component;

@Component
public class ContractNumberGenerator {
    private static long sequence = 1000L;
    
    public synchronized String generate(int fiscalYear) {
        sequence++;
        return "CTR-FY" + String.valueOf(fiscalYear).substring(2) + "-" + String.format("%05d", sequence);
    }
}
