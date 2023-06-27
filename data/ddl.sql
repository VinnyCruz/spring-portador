CREATE TABLE IF NOT EXISTS BANKACCOUNT(
        id UUID NOT NULL,
        bank_account VARCHAR(30),
        bank_agency VARCHAR(30),
        bank_code VARCHAR(30),
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CARDHOLDER(
        id UUID NOT NULL,
        client_id UUID NOT NULL unique,
        credit_analysis_id UUID NOT NULL unique,
        bank_account_id UUID NOT NULL unique,
        status VARCHAR(30),
        credit_limit decimal(10,2),
        created_at TIMESTAMP,
        PRIMARY KEY (id),
        FOREIGN KEY (bank_account_id) REFERENCES BANKACCOUNT (id)
);

CREATE TABLE IF NOT EXISTS CREDITCARD(
        id UUID NOT NULL,
        card_holder_id UUID,
        card_number CHAR(16) NOT NULL,
        cvv INTEGER NOT NULL,
        due_date DATE,
        PRIMARY KEY (id),
        FOREIGN KEY (card_holder_id) REFERENCES CARDHOLDER(id)
);