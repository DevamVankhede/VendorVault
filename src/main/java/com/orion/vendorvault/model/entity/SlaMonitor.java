package com.orion.vendorvault.model.entity;

import com.orion.vendorvault.model.enums.BreachSeverity;
import com.orion.vendorvault.model.enums.SlaStatus;
import com.orion.vendorvault.model.enums.SlaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sla_monitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Enumerated(EnumType.STRING)
    @Column(name = "sla_type", nullable = false, length = 32)
    private SlaType slaType;

    @Column(name = "target_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal targetValue;

    @Column(name = "actual_value", precision = 10, scale = 2)
    private BigDecimal actualValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private SlaStatus status;

    @Column(name = "breach", nullable = false)
    private boolean breach;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 16)
    private BreachSeverity severity;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

