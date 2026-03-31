package com.orion.vendorvault.model.entity;

import com.orion.vendorvault.model.enums.ContractStatus;
import com.orion.vendorvault.model.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_number", nullable = false, unique = true, length = 40)
    private String contractNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false, length = 32)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ContractStatus status;

    @Column(name = "department", length = 120)
    private String department;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "contract_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal contractValue;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "payment_terms", columnDefinition = "TEXT")
    private String paymentTerms;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "renewal_of_contract_id")
    private Contract renewalOfContract;

    @Column(name = "termination_reason", length = 500)
    private String terminationReason;

    @Column(name = "terminated_at")
    private LocalDateTime terminatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<ContractDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<ContractAmendment> amendments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = ContractStatus.DRAFT;
        }
        if (this.currency == null || this.currency.isBlank()) {
            this.currency = "INR";
        }
        if (this.contractValue == null) {
            this.contractValue = BigDecimal.ZERO;
        }
        if (this.contractNumber == null || this.contractNumber.isBlank()) {
            // Placeholder-safe uniqueness: replaced by ContractNumberGenerator in service layer.
            // Still ensures DB constraint isn't violated if service missed generation.
            this.contractNumber = "CTR-TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        }
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

