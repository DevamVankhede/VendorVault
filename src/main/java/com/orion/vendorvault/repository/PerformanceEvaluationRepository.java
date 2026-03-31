package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.PerformanceEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Long> {
    Page<PerformanceEvaluation> findByVendorIdOrderBySubmittedAtDesc(Long vendorId, Pageable pageable);
    List<PerformanceEvaluation> findByVendorId(Long vendorId);
}
