package com.orion.vendorvault.service;

import com.orion.vendorvault.dto.request.ApprovalActionDto;
import com.orion.vendorvault.dto.request.DelegateActionDto;
import com.orion.vendorvault.model.entity.ApprovalStep;
import com.orion.vendorvault.model.entity.ApprovalWorkflow;

import java.util.List;

public interface ApprovalService {
    void approve(Long stepId, ApprovalActionDto actionDto);
    void reject(Long stepId, ApprovalActionDto actionDto);
    void delegate(Long stepId, DelegateActionDto actionDto);
    List<ApprovalStep> getPendingQueueForUser(Long userId);
    ApprovalWorkflow getWorkflowById(Long id);
    List<ApprovalWorkflow> getWorkflowHistory(Long contractId);
    void checkSlaBreaches();
}
