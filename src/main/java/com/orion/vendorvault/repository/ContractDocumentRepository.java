package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.ContractDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDocumentRepository extends JpaRepository<ContractDocument, Long> {
}
