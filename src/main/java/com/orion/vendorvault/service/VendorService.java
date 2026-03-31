package com.orion.vendorvault.service;

import com.orion.vendorvault.dto.request.BlacklistReasonDto;
import com.orion.vendorvault.dto.request.VendorCreateRequestDto;
import com.orion.vendorvault.dto.request.VendorUpdateRequestDto;
import com.orion.vendorvault.model.entity.Vendor;
import com.orion.vendorvault.model.entity.VendorDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface VendorService {
    Vendor createVendor(VendorCreateRequestDto request);
    Vendor updateVendor(Long id, VendorUpdateRequestDto request);
    void activateVendor(Long id);
    void blacklistVendor(Long id, BlacklistReasonDto reasonDto);
    VendorDocument uploadDocument(Long vendorId, String documentType, MultipartFile file);
    void verifyDocument(Long documentId);
    void deleteDocument(Long documentId);
    Vendor getVendorById(Long id);
    Page<Vendor> getAllVendors(String status, Pageable pageable);
    Page<Vendor> searchVendors(String query, String status, Pageable pageable);
}
