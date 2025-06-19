CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    document VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    balance NUMERIC(20, 8) DEFAULT 0,
    user_type TEXT NOT NULL
);

CREATE TABLE transactions(
    id BIGSERIAL PRIMARY KEY,
    value NUMERIC(20, 8) NOT NULL,
    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);