package com.orion.vendorvault.model.entity;

import com.orion.vendorvault.model.enums.MsmeCategory;
import com.orion.vendorvault.model.enums.VendorStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_code", nullable = false, unique = true, length = 32)
    private String vendorCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "category", length = 120)
    private String category;

    @Column(name = "pan", nullable = false, unique = true, length = 16)
    private String pan;

    @Column(name = "gstin", unique = true, length = 20)
    private String gstin;

    @Enumerated(EnumType.STRING)
    @Column(name = "msme_category", nullable = false, length = 32)
    private MsmeCategory msmeCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private VendorStatus status;

    @Column(name = "blacklist_reason", length = 500)
    private String blacklistReason;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "state", length = 80)
    private String state;

    @Column(name = "pincode", length = 12)
    private String pincode;

    @Column(name = "contact_person", length = 120)
    private String contactPerson;

    @Column(name = "contact_email", length = 190)
    private String contactEmail;

    @Column(name = "contact_phone", length = 32)
    private String contactPhone;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<VendorDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<VendorBankDetail> bankDetails = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

