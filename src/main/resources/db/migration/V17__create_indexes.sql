CREATE INDEX idx_contracts_end_date ON contracts (end_date);
CREATE INDEX idx_contracts_status ON contracts (status);
CREATE INDEX idx_contracts_vendor_id ON contracts (vendor_id);

CREATE INDEX idx_vendors_status ON vendors (status);
CREATE INDEX idx_vendors_name ON vendors (name);

CREATE INDEX idx_vendor_documents_vendor_id ON vendor_documents (vendor_id);
CREATE INDEX idx_vendor_bank_details_vendor_id ON vendor_bank_details (vendor_id);

CREATE INDEX idx_contract_documents_contract_id ON contract_documents (contract_id);
CREATE INDEX idx_contract_amendments_contract_id ON contract_amendments (contract_id);

CREATE INDEX idx_workflows_contract_id ON approval_workflows (contract_id);
CREATE INDEX idx_steps_workflow_id ON approval_steps (workflow_id);
CREATE INDEX idx_steps_role_required_action ON approval_steps (role_required, action);
CREATE INDEX idx_steps_due_at ON approval_steps (due_at);

CREATE INDEX idx_sla_contract_id ON sla_monitors (contract_id);
CREATE INDEX idx_sla_breach ON sla_monitors (breach, severity);

CREATE INDEX idx_eval_vendor_id ON performance_evaluations (vendor_id);
CREATE INDEX idx_eval_status ON performance_evaluations (status);

CREATE INDEX idx_notifications_user_read ON notifications (user_id, is_read);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

CREATE INDEX idx_audit_entity ON audit_logs (entity_type, entity_id);
CREATE INDEX idx_audit_performed_by ON audit_logs (performed_by);
CREATE INDEX idx_audit_created_at ON audit_logs (created_at);

