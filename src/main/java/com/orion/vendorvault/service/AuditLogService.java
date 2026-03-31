package com.orion.vendorvault.service;

import com.orion.vendorvault.model.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditLogService {
    void logEvent(String eventType, String entityType, Long entityId, String details);
    List<AuditLog> getAuditLogsForEntity(String entityType, Long entityId);
    Page<AuditLog> getAllAuditLogs(Pageable pageable);
    byte[] exportAuditLog();
}
