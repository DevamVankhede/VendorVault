CREATE TABLE IF NOT EXISTS contract_documents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_id BIGINT NOT NULL,
  document_type VARCHAR(64) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  original_file_name VARCHAR(255) NULL,
  uploaded_by BIGINT NULL,
  uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_contract_documents_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE RESTRICT,
  CONSTRAINT fk_contract_documents_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

