package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.dto.request.BlacklistReasonDto;
import com.orion.vendorvault.dto.request.VendorCreateRequestDto;
import com.orion.vendorvault.dto.request.VendorUpdateRequestDto;
import com.orion.vendorvault.model.entity.Vendor;
import com.orion.vendorvault.model.entity.VendorDocument;
import com.orion.vendorvault.model.enums.AuditEventType;
import com.orion.vendorvault.model.enums.MsmeCategory;
import com.orion.vendorvault.model.enums.VendorStatus;
import com.orion.vendorvault.repository.VendorDocumentRepository;
import com.orion.vendorvault.repository.VendorRepository;
import com.orion.vendorvault.service.AuditLogService;
import com.orion.vendorvault.service.VendorService;
import com.orion.vendorvault.util.FileStorageUtil;
import com.orion.vendorvault.util.VendorCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorDocumentRepository vendorDocumentRepository;
    private final VendorCodeGenerator vendorCodeGenerator;
    private final FileStorageUtil fileStorageUtil;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public Vendor createVendor(VendorCreateRequestDto request) {
        if (vendorRepository.findByPan(request.getPan()).isPresent()) {
            throw new IllegalArgumentException("Vendor with PAN already exists");
        }

        Vendor vendor = Vendor.builder()
                .vendorCode(vendorCodeGenerator.generate())
                .name(request.getName())
                .category(request.getCategory())
                .pan(request.getPan())
                .gstin(request.getGstin())
                .msmeCategory(request.getMsmeCategory() != null ? MsmeCategory.valueOf(request.getMsmeCategory()) : MsmeCategory.NONE)
                .status(VendorStatus.UNDER_REVIEW)
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .build();

        Vendor saved = vendorRepository.save(vendor);
        auditLogService.logEvent(AuditEventType.VENDOR_CREATED, "Vendor", saved.getId(), "Vendor created: " + saved.getVendorCode());
        return saved;
    }

    @Override
    @Transactional
    public Vendor updateVendor(Long id, VendorUpdateRequestDto request) {
        Vendor vendor = getVendorById(id);
        
        vendor.setName(request.getName());
        vendor.setCategory(request.getCategory());
        vendor.setGstin(request.getGstin());
        vendor.setMsmeCategory(request.getMsmeCategory() != null ? MsmeCategory.valueOf(request.getMsmeCategory()) : MsmeCategory.NONE);
        vendor.setAddressLine1(request.getAddressLine1());
        vendor.setAddressLine2(request.getAddressLine2());
        vendor.setCity(request.getCity());
        vendor.setState(request.getState());
        vendor.setPincode(request.getPincode());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setContactEmail(request.getContactEmail());
        vendor.setContactPhone(request.getContactPhone());

        Vendor saved = vendorRepository.save(vendor);
        auditLogService.logEvent(AuditEventType.VENDOR_CREATED, "Vendor", saved.getId(), "Vendor updated");
        return saved;
    }

    @Override
    @Transactional
    public void activateVendor(Long id) {
        Vendor vendor = getVendorById(id);
        vendor.setStatus(VendorStatus.ACTIVE);
        vendorRepository.save(vendor);
        auditLogService.logEvent(AuditEventType.VENDOR_ACTIVATED, "Vendor", vendor.getId(), "Vendor activated");
    }

    @Override
    @Transactional
    public void blacklistVendor(Long id, BlacklistReasonDto reasonDto) {
        Vendor vendor = getVendorById(id);
        vendor.setStatus(VendorStatus.BLACKLISTED);
        vendor.setBlacklistReason(reasonDto.getReason());
        vendorRepository.save(vendor);
        auditLogService.logEvent(AuditEventType.VENDOR_BLACKLISTED, "Vendor", vendor.getId(), "Vendor blacklisted: " + reasonDto.getReason());
    }

    @Override
    @Transactional
    public VendorDocument uploadDocument(Long vendorId, String documentType, MultipartFile file) {
        Vendor vendor = getVendorById(vendorId);
        String path = fileStorageUtil.store(file, "vendor-docs");
        
        VendorDocument doc = VendorDocument.builder()
                .vendor(vendor)
                .documentType(com.orion.vendorvault.model.enums.DocumentType.valueOf(documentType))
                .originalFileName(file.getOriginalFilename())
                .filePath(path)
                .build();
                
        VendorDocument saved = vendorDocumentRepository.save(doc);
        auditLogService.logEvent(AuditEventType.DOCUMENT_UPLOADED, "Vendor", vendorId, "Document uploaded: " + file.getOriginalFilename());
        return saved;
    }

    @Override
    @Transactional
    public void verifyDocument(Long documentId) {
        VendorDocument doc = vendorDocumentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        doc.setVerified(true);
        vendorDocumentRepository.save(doc);
        auditLogService.logEvent(AuditEventType.DOCUMENT_VERIFIED, "Vendor", doc.getVendor().getId(), "Document verified: " + doc.getOriginalFileName());
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) {
        VendorDocument doc = vendorDocumentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        fileStorageUtil.delete(doc.getFilePath());
        vendorDocumentRepository.delete(doc);
        auditLogService.logEvent("DOCUMENT_DELETED", "Vendor", doc.getVendor().getId(), "Document deleted: " + doc.getOriginalFileName());
    }

    @Override
    @Transactional(readOnly = true)
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vendor> getAllVendors(String statusStr, Pageable pageable) {
        if (statusStr != null && !statusStr.isBlank() && !statusStr.equalsIgnoreCase("ALL")) {
            VendorStatus status = VendorStatus.valueOf(statusStr.toUpperCase());
            return vendorRepository.findByStatus(status, pageable);
        }
        return vendorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vendor> searchVendors(String query, String statusStr, Pageable pageable) {
        if (statusStr != null && !statusStr.isBlank() && !statusStr.equalsIgnoreCase("ALL")) {
            VendorStatus status = VendorStatus.valueOf(statusStr.toUpperCase());
            return vendorRepository.searchVendorsWithStatus(query, status, pageable);
        }
        return vendorRepository.searchVendors(query, pageable);
    }
}
