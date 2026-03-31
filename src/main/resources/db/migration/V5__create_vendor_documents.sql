CREATE TABLE IF NOT EXISTS vendor_documents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vendor_id BIGINT NOT NULL,
  document_type VARCHAR(64) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  original_file_name VARCHAR(255) NULL,
  uploaded_by BIGINT NULL,
  uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  verified BOOLEAN NOT NULL DEFAULT FALSE,
  verified_by BIGINT NULL,
  verified_at TIMESTAMP NULL,
  CONSTRAINT fk_vendor_documents_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id) ON DELETE RESTRICT,
  CONSTRAINT fk_vendor_documents_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE RESTRICT,
  CONSTRAINT fk_vendor_documents_verified_by FOREIGN KEY (verified_by) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

