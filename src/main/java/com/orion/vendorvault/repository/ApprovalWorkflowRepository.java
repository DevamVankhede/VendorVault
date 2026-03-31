package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.ApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflow, Long> {
    List<ApprovalWorkflow> findByContractIdOrderByCreatedAtDesc(Long contractId);
    
    @Query("SELECT DISTINCT w FROM ApprovalWorkflow w JOIN w.steps s WHERE s.roleRequired = :roleName AND s.action = com.orion.vendorvault.model.enums.ApprovalAction.PENDING")
    List<ApprovalWorkflow> findPendingWorkflowsForRole(@Param("roleName") String roleName);
}
