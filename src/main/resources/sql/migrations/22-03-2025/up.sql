CREATE TABLE IF NOT EXISTS budget
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    amount      DECIMAL(10, 2) NOT NULL,
    start_date  DATE           NOT NULL,
    end_date    DATE           NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_id INT            NOT NULL REFERENCES customer (customer_id)
);

CREATE TABLE IF NOT EXISTS expense
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    description  TEXT,
    amount       DECIMAL(10, 2) NOT NULL,
    expense_date DATE           NOT NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_id  INT            NOT NULL REFERENCES customer (customer_id)
);

CREATE TABLE IF NOT EXISTS parameter_crm
(
    parameter_key   VARCHAR(50) PRIMARY KEY,
    parameter_value DOUBLE
);

ALTER TABLE trigger_ticket
    ADD COLUMN expense_id INT;
ALTER TABLE trigger_ticket
    ADD CONSTRAINT fk_trigger_ticket_expense FOREIGN KEY (expense_id) REFERENCES expense (id);

ALTER TABLE trigger_lead
    ADD COLUMN expense_id INT;
ALTER TABLE trigger_lead
    ADD CONSTRAINT fk_trigger_lead_expense FOREIGN KEY (expense_id) REFERENCES expense (id);
