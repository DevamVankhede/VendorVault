CREATE TABLE IF NOT EXISTS performance_evaluations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vendor_id BIGINT NOT NULL,
  evaluation_period VARCHAR(32) NOT NULL,
  delivery_score INT NOT NULL,
  quality_score INT NOT NULL,
  communication_score INT NOT NULL,
  compliance_score INT NOT NULL,
  overall_score DECIMAL(5,2) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'DRAFT',
  evaluator_id BIGINT NULL,
  submitted_at TIMESTAMP NULL,
  finalized_at TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_eval_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id) ON DELETE RESTRICT,
  CONSTRAINT fk_eval_evaluator FOREIGN KEY (evaluator_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

