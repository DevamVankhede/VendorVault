package com.orion.vendorvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VendorVaultApplication {
    public static void main(String[] args) {
        SpringApplication.run(VendorVaultApplication.class, args);
    }
}
