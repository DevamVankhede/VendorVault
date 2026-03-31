package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.SlaMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaMonitorRepository extends JpaRepository<SlaMonitor, Long> {
    List<SlaMonitor> findByContractId(Long contractId);
}
