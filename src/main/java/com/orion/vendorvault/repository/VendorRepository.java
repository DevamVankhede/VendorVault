package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.Vendor;
import com.orion.vendorvault.model.enums.VendorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByPan(String pan);
    Page<Vendor> findByStatus(VendorStatus status, Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(v.vendorCode) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(v.pan) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Vendor> searchVendors(@Param("query") String query, Pageable pageable);

    @Query("SELECT v FROM Vendor v WHERE v.status = :status AND " +
           "(LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(v.vendorCode) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(v.pan) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Vendor> searchVendorsWithStatus(@Param("query") String query, @Param("status") VendorStatus status, Pageable pageable);

    long countByStatus(VendorStatus status);
}
