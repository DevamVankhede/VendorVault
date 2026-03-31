package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.model.entity.AuditLog;
import com.orion.vendorvault.repository.AuditLogRepository;
import com.orion.vendorvault.service.AuditLogService;
import com.orion.vendorvault.util.ReportGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ReportGeneratorUtil reportGeneratorUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(String eventType, String entityType, Long entityId, String details) {
        String performedBy = "SYSTEM";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            performedBy = auth.getName();
        }

        AuditLog logEntry = AuditLog.builder()
                .eventType(eventType)
                .entityType(entityType)
                .entityId(entityId)
                .message(details)
                // .performedBy(user) // TO BE FETCHED FROM DB
                .build();
        
        auditLogRepository.save(logEntry);
        log.debug("Audit event logged: {} for entity: {}[{}] by {}", eventType, entityType, entityId, performedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsForEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportAuditLog() {
        List<AuditLog> logs = auditLogRepository.findAll();
        // Uses the report utility to generate real Excel byte array
        return reportGeneratorUtil.generateAuditExcelReport(logs);
    }
}
