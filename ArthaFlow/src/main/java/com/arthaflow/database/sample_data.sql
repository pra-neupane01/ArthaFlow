-- ============================================================
-- ArthaFlow — Sample demo data (run AFTER schema.sql)
-- ============================================================

USE ARTHAFLOW;

ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_picture VARCHAR(255) DEFAULT NULL;

-- 1. ADMIN (password: Admin@1234 — demo only; hash in production)
INSERT INTO users (email, password, full_name, address, phone_number, role, status)
VALUES (
    'admin@arthaflow.com',
    'Admin@1234',
    'Sanjay Sharma',
    'Sundar Haraicha 04, Dulari, Itahari, Sunsari, Nepal',
    '+9779800000001',
    'ADMIN',
    'ACTIVE'
);

-- 2. RAM — active account + approved account KYC
--    Login: ram@iic.edu.np | Password: Ram@12345
INSERT INTO users (email, password, full_name, address, phone_number, role, status)
VALUES (
    'ram@iic.edu.np',
    'Ram@12345',
    'Ram Prasad Sharma',
    'Golpark 03, Itahari, Sunsari, Nepal',
    '+9779811084680',
    'USER',
    'ACTIVE'
);

INSERT INTO accounts (account_number, user_id, balance, account_type, status)
VALUES (
    'AF-2026-00101',
    (SELECT user_id FROM users WHERE email = 'ram@iic.edu.np'),
    125000.00,
    'SAVINGS',
    'ACTIVE'
);

INSERT INTO kyc_details (
    user_id, purpose, account_id, card_id, status,
    citizenship_number, date_of_birth, occupation, father_name, mother_name, family_details, gender,
    permanent_address, mailing_address, id_document_path, address_proof_path,
    annual_income, minimum_income, personal_information, income_details,
    employment_details, card_preferences, credit_information, terms_accepted
) VALUES (
    (SELECT user_id FROM users WHERE email = 'ram@iic.edu.np'),
    'ACCOUNT_OPENING',
    (SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'),
    NULL,
    'APPROVED',
    'NPL-RAM-1001',
    '2001-05-12',
    'Student',
    'Prakash Sharma',
    'Sarita Sharma',
    'Single; no dependents',
    'MALE',
    'Golpark 03, Itahari, Sunsari, Nepal',
    'Same as permanent',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    0
);

INSERT INTO transactions (account_id, type, amount, balance_after, remarks, status)
VALUES
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'DEPOSIT',    50000.00, 50000.00,  'Initial deposit',    'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'DEPOSIT',   100000.00, 150000.00, 'Salary - April 2026', 'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'WITHDRAWAL', 25000.00, 125000.00, 'Rent payment',       'SUCCESS');

-- 3. SITA — pending account + pending account KYC
--    Login: sita@iic.edu.np | Password: Sita@12345
INSERT INTO users (email, password, full_name, address, phone_number, role, status)
VALUES (
    'sita@iic.edu.np',
    'Sita@12345',
    'Sita Kumari Thapa',
    'Damak 05, Jhapa, Koshi Province, Nepal',
    '+9779823456789',
    'USER',
    'ACTIVE'
);

INSERT INTO accounts (user_id, balance, account_type, status)
VALUES (
    (SELECT user_id FROM users WHERE email = 'sita@iic.edu.np'),
    0.00,
    'CURRENT',
    'PENDING'
);

INSERT INTO kyc_details (
    user_id, purpose, account_id, card_id, status,
    citizenship_number, date_of_birth, occupation, father_name, mother_name, family_details, gender,
    permanent_address, mailing_address, id_document_path, address_proof_path,
    annual_income, minimum_income, personal_information, income_details,
    employment_details, card_preferences, credit_information, terms_accepted
) VALUES (
    (SELECT user_id FROM users WHERE email = 'sita@iic.edu.np'),
    'ACCOUNT_OPENING',
    (SELECT account_id FROM accounts a JOIN users u ON a.user_id = u.user_id WHERE u.email = 'sita@iic.edu.np' LIMIT 1),
    NULL,
    'PENDING',
    'NPL-SITA-2002',
    '2002-08-20',
    'Student',
    'Hari Thapa',
    'Laxmi Thapa',
    'Living with parents; 1 sibling',
    'FEMALE',
    'Damak 05, Jhapa, Koshi Province, Nepal',
    'Campus hostel, Itahari',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    0
);

-- 4. HARI — active account, approved account KYC, active gold card + approved card KYC
--    Login: hari@iic.edu.np | Password: Hari@12345
INSERT INTO users (email, password, full_name, address, phone_number, role, status)
VALUES (
    'hari@iic.edu.np',
    'Hari@12345',
    'Hari Bahadur Rai',
    'Sunsari 01, Inaruwa, Sunsari, Nepal',
    '+9779845678901',
    'USER',
    'ACTIVE'
);

INSERT INTO accounts (account_number, user_id, balance, account_type, status)
VALUES (
    'AF-2026-00102',
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    78500.00,
    'SAVINGS',
    'ACTIVE'
);

INSERT INTO kyc_details (
    user_id, purpose, account_id, card_id, status,
    citizenship_number, date_of_birth, occupation, father_name, mother_name, family_details, gender,
    permanent_address, mailing_address, id_document_path, address_proof_path,
    annual_income, minimum_income, personal_information, income_details,
    employment_details, card_preferences, credit_information, terms_accepted
) VALUES (
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    'ACCOUNT_OPENING',
    (SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00102'),
    NULL,
    'APPROVED',
    'NPL-HARI-3003',
    '1999-11-03',
    'Private sector employee',
    'Ram Bahadur Rai',
    'Sita Devi Rai',
    'Married; no children',
    'MALE',
    'Sunsari 01, Inaruwa, Sunsari, Nepal',
    'Same as permanent',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    0
);

INSERT INTO credit_cards (user_id, card_number, card_type, credit_limit, current_balance, status, expiry_date, pin_hash)
VALUES (
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    '4111111111111111',
    'GOLD',
    50000.00,
    12500.00,
    'ACTIVE',
    '12/28',
    NULL
);

INSERT INTO kyc_details (
    user_id, purpose, account_id, card_id, status,
    citizenship_number, date_of_birth, occupation, father_name, mother_name, family_details, gender,
    permanent_address, mailing_address, id_document_path, address_proof_path,
    annual_income, minimum_income, personal_information, income_details,
    employment_details, card_preferences, credit_information, terms_accepted
)
SELECT
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    'CREDIT_CARD',
    NULL,
    c.card_id,
    'APPROVED',
    'NPL-HARI-3003',
    '1999-11-03',
    'Software Engineer',
    'Ram Bahadur Rai',
    'Sita Devi Rai',
    'Married; no children',
    'MALE',
    'Sunsari 01, Inaruwa, Sunsari, Nepal',
    'Same as permanent',
    NULL,
    NULL,
    450000.00,
    450000.00,
    'Employed — demo data for coursework',
    'Primary: salary',
    'Demo Employer Pvt Ltd, Senior Analyst, 3 years',
    'E-statement monthly; domestic use',
    'No other credit cards',
    1
FROM credit_cards c
WHERE c.user_id = (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np')
ORDER BY c.card_id DESC
LIMIT 1;

INSERT INTO transactions (account_id, type, amount, balance_after, remarks, status)
VALUES
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00102'), 'DEPOSIT',    100000.00, 100000.00, 'Initial deposit', 'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00102'), 'WITHDRAWAL',  21500.00,  78500.00, 'College fee Q2',  'SUCCESS');

SELECT user_id, full_name, email, role, status FROM users;
SELECT a.account_id, u.full_name, a.account_number, a.balance, a.status, kd.status AS kyc_row_status
  FROM accounts a
  JOIN users u ON a.user_id = u.user_id
  LEFT JOIN kyc_details kd ON kd.account_id = a.account_id AND kd.purpose = 'ACCOUNT_OPENING';
SELECT * FROM kyc_details;
SELECT * FROM credit_cards;
SELECT * FROM credit_card_transactions;
SELECT * FROM transactions;
