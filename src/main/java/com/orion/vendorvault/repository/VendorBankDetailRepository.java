package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.VendorBankDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorBankDetailRepository extends JpaRepository<VendorBankDetail, Long> {
}
