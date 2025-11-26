

CREATE TABLE fx_deals (
    id BIGSERIAL PRIMARY KEY,
    deal_unique_id VARCHAR(100) NOT NULL UNIQUE,
    from_currency_code VARCHAR(3) NOT NULL,
    to_currency_code VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_amount_positive CHECK (deal_amount > 0)
);

CREATE INDEX idx_deal_unique_id ON fx_deals(deal_unique_id);
CREATE INDEX idx_deal_timestamp ON fx_deals(deal_timestamp);
CREATE INDEX idx_currency_codes ON fx_deals(from_currency_code, to_currency_code);