package com.orion.vendorvault.util;

import org.springframework.stereotype.Component;

@Component
public class VendorCodeGenerator {
    private static long sequence = 1000L;
    
    public synchronized String generate() {
        sequence++;
        return "VND-" + String.format("%06d", sequence);
    }
}
