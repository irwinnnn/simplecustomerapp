INSERT INTO account.customer (id, username, first_name, last_name,document_id, date_of_birth, password)
VALUES (1, 'irwin1', 'john', 'doe','12345', '01011991', 'fb4251e751ead75b0dd876a9d66ec42b82f2a04f3f188e607207a921b25e95f629caf2d1')
ON DUPLICATE KEY UPDATE
first_name="Irwin";

INSERT INTO account.address (id, city, country, postal_code, region,street)
VALUES (1, 'rotterdam', 'NL', '3112XX', 'Region','Street 6')
ON DUPLICATE KEY UPDATE
    city="Rotterdam";

INSERT INTO account.ibanaccount (id, account_type, balance, currency, iban)
VALUES (1, 'Savings', 200.00, 'EUR', 'NL13TEST1234567890')
ON DUPLICATE KEY UPDATE
    account_type="Savings";

UPDATE account.customer
SET address = 1, iban_account = 1
WHERE id = 1;