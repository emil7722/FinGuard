-- V3: Create Transactions Ledger table

CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_account_id UUID REFERENCES accounts(id),
    destination_account_id UUID REFERENCES accounts(id),
    amount NUMERIC(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    type VARCHAR(50) NOT NULL, -- e.g., TRANSFER, DEPOSIT, WITHDRAWAL
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, COMPLETED, FAILED, REJECTED
    idempotency_key VARCHAR(255) UNIQUE, -- Crucial for preventing double charges on network retries
    failure_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_source ON transactions(source_account_id);
CREATE INDEX idx_transactions_destination ON transactions(destination_account_id);
CREATE INDEX idx_transactions_idempotency ON transactions(idempotency_key);