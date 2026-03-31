package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.dto.request.AmendmentCreateDto;
import com.orion.vendorvault.dto.request.ContractCreateRequestDto;
import com.orion.vendorvault.dto.request.ContractUpdateRequestDto;
import com.orion.vendorvault.dto.request.RenewalRequestDto;
import com.orion.vendorvault.dto.request.TerminationReasonDto;
import com.orion.vendorvault.model.entity.Contract;
import com.orion.vendorvault.model.entity.ContractAmendment;
import com.orion.vendorvault.model.entity.ContractDocument;
import com.orion.vendorvault.model.entity.Vendor;
import com.orion.vendorvault.model.enums.AuditEventType;
import com.orion.vendorvault.model.enums.ContractStatus;
import com.orion.vendorvault.model.enums.ContractType;
import com.orion.vendorvault.repository.ContractAmendmentRepository;
import com.orion.vendorvault.repository.ContractDocumentRepository;
import com.orion.vendorvault.repository.ContractRepository;
import com.orion.vendorvault.repository.VendorRepository;
import com.orion.vendorvault.service.AuditLogService;
import com.orion.vendorvault.service.ContractService;
import com.orion.vendorvault.service.NotificationService;
import com.orion.vendorvault.util.ContractNumberGenerator;
import com.orion.vendorvault.util.DateUtil;
import com.orion.vendorvault.util.FileStorageUtil;
import com.orion.vendorvault.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final VendorRepository vendorRepository;
    private final ContractDocumentRepository contractDocumentRepository;
    private final ContractAmendmentRepository contractAmendmentRepository;
    private final ContractNumberGenerator contractNumberGenerator;
    private final FileStorageUtil fileStorageUtil;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;
    private final com.orion.vendorvault.repository.UserRepository userRepository;
    // Assume ApprovalService handles submitting for approval steps internally
    // private final ApprovalService approvalService; 

    @Override
    @Transactional
    public Contract createContract(ContractCreateRequestDto request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        // Get current user
        com.orion.vendorvault.model.entity.User currentUser = null;
        try {
            String username = SecurityUtil.getCurrentUsername();
            currentUser = userRepository.findByEmail(username).orElse(null);
        } catch (Exception e) {
            log.warn("Could not get current user: {}", e.getMessage());
        }

        Contract contract = Contract.builder()
                .contractNumber(contractNumberGenerator.generate(LocalDate.now().getYear()))
                .title(request.getTitle())
                .vendor(vendor)
                .contractType(ContractType.valueOf(request.getContractType()))
                .status(ContractStatus.DRAFT)
                .department(request.getDepartment())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .contractValue(request.getContractValue())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .paymentTerms(request.getPaymentTerms() != null ? request.getPaymentTerms() : "Standard")
                .createdBy(currentUser)
                .build();

        Contract saved = contractRepository.save(contract);
        auditLogService.logEvent(AuditEventType.CONTRACT_CREATED, "Contract", saved.getId(), "Contract created in Draft status");
        return saved;
    }

    @Override
    @Transactional
    public Contract updateContract(Long id, ContractUpdateRequestDto request) {
        Contract contract = getContractById(id);
        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT contracts can be updated");
        }
        
        contract.setTitle(request.getTitle());
        contract.setContractType(ContractType.valueOf(request.getContractType()));
        contract.setDepartment(request.getDepartment());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setContractValue(request.getContractValue());
        
        Contract saved = contractRepository.save(contract);
        auditLogService.logEvent("CONTRACT_UPDATED", "Contract", saved.getId(), "Contract updated");
        return saved;
    }

    @Override
    @Transactional
    public void submitForApproval(Long id) {
        Contract contract = getContractById(id);
        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new IllegalStateException("Contract must be in DRAFT status to be submitted");
        }
        contract.setStatus(ContractStatus.PENDING_APPROVAL);
        contractRepository.save(contract);
        // Note: Approval workflow initiation logic would hook here (handled in ApprovalService)
        auditLogService.logEvent(AuditEventType.CONTRACT_SUBMITTED, "Contract", id, "Contract submitted for approval");
    }

    @Override
    @Transactional
    public void terminateContract(Long id, TerminationReasonDto reasonDto) {
        Contract contract = getContractById(id);
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setTerminationReason(reasonDto.getReason());
        contract.setTerminatedAt(LocalDateTime.now());
        contractRepository.save(contract);
        auditLogService.logEvent(AuditEventType.CONTRACT_TERMINATED, "Contract", id, "Contract terminated. Reason: " + reasonDto.getReason());
    }

    @Override
    @Transactional
    public Contract renewContract(Long id, RenewalRequestDto request) {
        Contract oldContract = getContractById(id);
        oldContract.setStatus(ContractStatus.RENEWED);
        contractRepository.save(oldContract);

        Contract newContract = Contract.builder()
                .contractNumber(contractNumberGenerator.generate(LocalDate.now().getYear()))
                .title(request.getNewTitle() != null ? request.getNewTitle() : oldContract.getTitle() + " (Renewal)")
                .vendor(oldContract.getVendor())
                .contractType(oldContract.getContractType())
                .status(ContractStatus.DRAFT)
                .department(oldContract.getDepartment())
                .startDate(request.getNewStartDate())
                .endDate(request.getNewEndDate())
                .contractValue(request.getNewContractValue())
                .currency(oldContract.getCurrency())
                .paymentTerms(oldContract.getPaymentTerms())
                .renewalOfContract(oldContract)
                .build();
                
        Contract saved = contractRepository.save(newContract);
        auditLogService.logEvent(AuditEventType.CONTRACT_RENEWED, "Contract", oldContract.getId(), "Contract renewed into " + saved.getContractNumber());
        auditLogService.logEvent(AuditEventType.CONTRACT_CREATED, "Contract", saved.getId(), "Renewal contract created from " + oldContract.getContractNumber());
        return saved;
    }

    @Override
    @Transactional
    public void createAmendment(Long id, AmendmentCreateDto dto) {
        Contract contract = getContractById(id);
        ContractAmendment amendment = ContractAmendment.builder()
                .contract(contract)
                .amendmentNumber(dto.getTitle()) // Mapped to title here
                .description(dto.getDescription())
                .effectiveDate(dto.getEffectiveDate())
                .build();
        contractAmendmentRepository.save(amendment);
        
        if (dto.getAddedValue() != null && dto.getAddedValue().compareTo(BigDecimal.ZERO) > 0) {
            contract.setContractValue(contract.getContractValue().add(dto.getAddedValue()));
            contractRepository.save(contract);
        }
        
        auditLogService.logEvent("WORKFLOW_CREATED", "Contract", id, "Amendment created: " + dto.getTitle());
    }

    @Override
    @Transactional
    public ContractDocument uploadDocument(Long contractId, String documentType, MultipartFile file) {
        Contract contract = getContractById(contractId);
        String path = fileStorageUtil.store(file, "contract-docs");
        
        ContractDocument doc = ContractDocument.builder()
                .contract(contract)
                .documentType(com.orion.vendorvault.model.enums.DocumentType.valueOf(documentType))
                .originalFileName(file.getOriginalFilename())
                .filePath(path)
                .build();
                
        ContractDocument saved = contractDocumentRepository.save(doc);
        auditLogService.logEvent(AuditEventType.DOCUMENT_UPLOADED, "Contract", contractId, "Document uploaded: " + file.getOriginalFilename());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> getAllContracts(String statusStr, Pageable pageable) {
        if (statusStr != null && !statusStr.isBlank() && !statusStr.equalsIgnoreCase("ALL")) {
            // Note: In real scenarios, add status filtering implementation
        }
        return contractRepository.findAll(pageable);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?") // Every day at 1 AM
    public void processExpiryAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate threshold30 = today.plusDays(30);
        
        List<Contract> expiring = contractRepository.findContractsExpiringBetween(today, threshold30);
        for (Contract c : expiring) {
            c.setStatus(ContractStatus.EXPIRING_SOON);
            contractRepository.save(c);
            
            // Send alert to the creator
            if (c.getCreatedBy() != null) {
                notificationService.sendNotification(
                        c.getCreatedBy().getId(), 
                        com.orion.vendorvault.model.enums.NotificationType.EXPIRY_ALERT,
                        "Contract Expiring Soon",
                        "Contract " + c.getContractNumber() + " is expiring in less than 30 days.",
                        "/contracts/" + c.getId()
                );
            }
            auditLogService.logEvent(AuditEventType.CONTRACT_EXPIRY_ALERT_SENT, "Contract", c.getId(), "Expiry alert dispatched");
        }
        log.info("Processed expiry alerts for {} contracts", expiring.size());
    }
}
