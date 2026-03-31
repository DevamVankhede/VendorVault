package com.orion.vendorvault.service;

import com.orion.vendorvault.dto.request.AmendmentCreateDto;
import com.orion.vendorvault.dto.request.ContractCreateRequestDto;
import com.orion.vendorvault.dto.request.ContractUpdateRequestDto;
import com.orion.vendorvault.dto.request.RenewalRequestDto;
import com.orion.vendorvault.dto.request.TerminationReasonDto;
import com.orion.vendorvault.model.entity.Contract;
import com.orion.vendorvault.model.entity.ContractDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    Contract createContract(ContractCreateRequestDto request);
    Contract updateContract(Long id, ContractUpdateRequestDto request);
    void submitForApproval(Long id);
    void terminateContract(Long id, TerminationReasonDto reasonDto);
    Contract renewContract(Long id, RenewalRequestDto request);
    void createAmendment(Long id, AmendmentCreateDto dto);
    ContractDocument uploadDocument(Long contractId, String documentType, MultipartFile file);
    Contract getContractById(Long id);
    Page<Contract> getAllContracts(String status, Pageable pageable);
    void processExpiryAlerts();
}
