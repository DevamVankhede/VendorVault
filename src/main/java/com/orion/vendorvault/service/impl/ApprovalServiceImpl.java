package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.dto.request.ApprovalActionDto;
import com.orion.vendorvault.dto.request.DelegateActionDto;
import com.orion.vendorvault.model.entity.ApprovalStep;
import com.orion.vendorvault.model.entity.ApprovalWorkflow;
import com.orion.vendorvault.model.entity.Contract;
import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.model.enums.ApprovalAction;
import com.orion.vendorvault.model.enums.ContractStatus;
import com.orion.vendorvault.model.enums.WorkflowStatus;
import com.orion.vendorvault.repository.ApprovalStepRepository;
import com.orion.vendorvault.repository.ApprovalWorkflowRepository;
import com.orion.vendorvault.repository.ContractRepository;
import com.orion.vendorvault.repository.UserRepository;
import com.orion.vendorvault.service.ApprovalService;
import com.orion.vendorvault.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalStepRepository approvalStepRepository;
    private final ApprovalWorkflowRepository approvalWorkflowRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public void approve(Long stepId, ApprovalActionDto actionDto) {
        ApprovalStep step = approvalStepRepository.findById(stepId).orElseThrow();
        step.setAction(ApprovalAction.APPROVED);
        step.setComments(actionDto.getComments());
        step.setActionAt(LocalDateTime.now());
        approvalStepRepository.save(step);

        ApprovalWorkflow workflow = step.getWorkflow();
        // Check if this was the last step
        boolean allApproved = workflow.getSteps().stream()
                .allMatch(s -> s.getAction() == ApprovalAction.APPROVED);

        if (allApproved) {
            workflow.setStatus(WorkflowStatus.APPROVED);
            approvalWorkflowRepository.save(workflow);

            Contract contract = contractRepository.findById(workflow.getContract().getId()).orElseThrow();
            contract.setStatus(ContractStatus.APPROVED);
            contractRepository.save(contract);
            auditLogService.logEvent("CONTRACT_APPROVED_FINAL", "Contract", contract.getId(), "Contract fully approved");
        } else {
            auditLogService.logEvent("STEP_APPROVED", "ApprovalWorkflow", workflow.getId(), "Step approved: " + actionDto.getComments());
        }
    }

    @Override
    @Transactional
    public void reject(Long stepId, ApprovalActionDto actionDto) {
        ApprovalStep step = approvalStepRepository.findById(stepId).orElseThrow();
        step.setAction(ApprovalAction.REJECTED);
        step.setComments(actionDto.getComments());
        step.setActionAt(LocalDateTime.now());
        approvalStepRepository.save(step);

        ApprovalWorkflow workflow = step.getWorkflow();
        workflow.setStatus(WorkflowStatus.REJECTED);
        approvalWorkflowRepository.save(workflow);

        Contract contract = contractRepository.findById(workflow.getContract().getId()).orElseThrow();
        contract.setStatus(ContractStatus.DRAFT);
        contractRepository.save(contract);
        
        auditLogService.logEvent("CONTRACT_REJECTED", "Contract", contract.getId(), "Contract rejected: " + actionDto.getComments());
    }

    @Override
    @Transactional
    public void delegate(Long stepId, DelegateActionDto actionDto) {
        ApprovalStep step = approvalStepRepository.findById(stepId).orElseThrow();
        User delegateTo = userRepository.findById(actionDto.getDelegateToUserId()).orElseThrow();
        
        step.setAssignedToUser(delegateTo);
        step.setComments("Delegated: " + actionDto.getComments());
        approvalStepRepository.save(step);
        
        auditLogService.logEvent("STEP_DELEGATED", "ApprovalStep", step.getId(), "Delegated to user " + delegateTo.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalStep> getPendingQueueForUser(Long userId) {
        return approvalStepRepository.findPendingStepsForUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalWorkflow getWorkflowById(Long id) {
        return approvalWorkflowRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalWorkflow> getWorkflowHistory(Long contractId) {
        return approvalWorkflowRepository.findByContractIdOrderByCreatedAtDesc(contractId);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void checkSlaBreaches() {
        List<ApprovalStep> breaches = approvalStepRepository.findSlaBreaches(LocalDateTime.now());
        // Handle SLAs
        log.info("Checked SLA breaches: {} found", breaches.size());
    }
}
