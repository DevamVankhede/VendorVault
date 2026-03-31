package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.VendorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorDocumentRepository extends JpaRepository<VendorDocument, Long> {
}
