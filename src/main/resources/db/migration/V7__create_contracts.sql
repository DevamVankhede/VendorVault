CREATE TABLE IF NOT EXISTS contracts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_number VARCHAR(40) NOT NULL UNIQUE,
  title VARCHAR(255) NOT NULL,
  vendor_id BIGINT NOT NULL,
  contract_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  department VARCHAR(120) NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  contract_value DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  currency VARCHAR(8) NOT NULL DEFAULT 'INR',
  payment_terms TEXT NULL,
  renewal_of_contract_id BIGINT NULL,
  termination_reason VARCHAR(500) NULL,
  terminated_at TIMESTAMP NULL,
  created_by BIGINT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_contracts_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id) ON DELETE RESTRICT,
  CONSTRAINT fk_contracts_renewal_of FOREIGN KEY (renewal_of_contract_id) REFERENCES contracts(id) ON DELETE RESTRICT,
  CONSTRAINT fk_contracts_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

