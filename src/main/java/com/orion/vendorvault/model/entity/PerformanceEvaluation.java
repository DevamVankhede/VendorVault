package com.orion.vendorvault.model.entity;

import com.orion.vendorvault.model.enums.EvaluationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "evaluation_period", nullable = false, length = 32)
    private String evaluationPeriod;

    @Column(name = "delivery_score", nullable = false)
    private int deliveryScore;

    @Column(name = "quality_score", nullable = false)
    private int qualityScore;

    @Column(name = "communication_score", nullable = false)
    private int communicationScore;

    @Column(name = "compliance_score", nullable = false)
    private int complianceScore;

    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private EvaluationStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evaluator_id")
    private User evaluator;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

