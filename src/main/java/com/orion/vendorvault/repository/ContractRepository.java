package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.Contract;
import com.orion.vendorvault.model.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    List<Contract> findByStatus(ContractStatus status);
    Page<Contract> findByVendorId(Long vendorId, Pageable pageable);

    @Query("SELECT SUM(c.contractValue) FROM Contract c WHERE c.status = :status")
    BigDecimal sumContractValueByStatus(@Param("status") ContractStatus status);
    
    long countByStatus(ContractStatus status);

    @Query("SELECT c FROM Contract c WHERE c.status = 'ACTIVE' AND c.endDate BETWEEN :startDate AND :endDate")
    List<Contract> findContractsExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c.status as status, COUNT(c) as count FROM Contract c GROUP BY c.status")
    List<Object[]> getContractStatusCounts();

    @Query("SELECT c.department as department, SUM(c.contractValue) as totalValue FROM Contract c WHERE c.status = 'ACTIVE' GROUP BY c.department")
    List<Object[]> getFinancialExposureByDepartment();
}
