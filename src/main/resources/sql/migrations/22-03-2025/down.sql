ALTER TABLE trigger_ticket
    DROP FOREIGN KEY fk_trigger_ticket_expense;
ALTER TABLE trigger_ticket
    DROP COLUMN expense_id;

ALTER TABLE trigger_lead
    DROP FOREIGN KEY fk_trigger_lead_expense;
ALTER TABLE trigger_lead
    DROP COLUMN expense_id;

DROP TABLE IF EXISTS expense;
DROP TABLE IF EXISTS parameter_crm;
DROP TABLE IF EXISTS budget;
