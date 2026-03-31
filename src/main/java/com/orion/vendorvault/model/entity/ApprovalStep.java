package com.orion.vendorvault.model.entity;

import com.orion.vendorvault.model.enums.ApprovalAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "workflow_id", nullable = false)
    private ApprovalWorkflow workflow;

    @Column(name = "step_order", nullable = false)
    private int stepOrder;

    @Column(name = "role_required", nullable = false, length = 64)
    private String roleRequired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedToUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 32)
    private ApprovalAction action;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "action_by")
    private User actionBy;

    @Column(name = "action_at")
    private LocalDateTime actionAt;

    @Column(name = "comments", length = 1000)
    private String comments;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

