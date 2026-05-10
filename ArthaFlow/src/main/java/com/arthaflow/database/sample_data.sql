-- ============================================================
-- ArthaFlow — Sample Demo Data for MySQL Workbench
-- Run this AFTER your schema is created (tables must exist)
-- ============================================================

USE ARTHAFLOW;

-- Add profile_picture column if not already added
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_picture VARCHAR(255) DEFAULT NULL;

-- ============================================================
-- 1. ADMIN USER (password: Admin@1234)
-- ============================================================
INSERT INTO users (email, password, full_name, address, phone_number, role, status)
VALUES (
    'admin@arthaflow.com',
    'Admin@1234',  -- In production, this should be hashed
    'Sanjay Sharma',
    'Sundar Haraicha 04, Dulari, Itahari, Sunsari, Nepal',
    '+9779800000001',
    'ADMIN',
    'ACTIVE'
);

-- ============================================================
-- 2. DEMO USER 1 — Fully Active Account (KYC Approved)
--    Login: ram@iic.edu.np | Password: Ram@12345
-- ============================================================
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

-- Active, KYC-approved account for User 1
INSERT INTO accounts (account_number, user_id, balance, account_type, status, kyc_status)
VALUES (
    'AF-2026-00101',
    (SELECT user_id FROM users WHERE email = 'ram@iic.edu.np'),
    125000.00,
    'SAVINGS',
    'ACTIVE',
    'APPROVED'
);

-- Sample transactions for User 1
INSERT INTO transactions (account_id, type, amount, balance_after, remarks, status)
VALUES
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'DEPOSIT',    50000.00, 50000.00,  'Initial deposit',           'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'DEPOSIT',   100000.00, 150000.00, 'Salary - April 2026',        'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00101'), 'WITHDRAWAL', 25000.00, 125000.00, 'Rent payment',              'SUCCESS');

-- ============================================================
-- 3. DEMO USER 2 — Pending KYC (just submitted documents)
--    Login: sita@iic.edu.np | Password: Sita@12345
-- ============================================================
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

-- Pending account for User 2
INSERT INTO accounts (user_id, balance, account_type, status, kyc_status)
VALUES (
    (SELECT user_id FROM users WHERE email = 'sita@iic.edu.np'),
    0.00,
    'CURRENT',
    'PENDING',
    'PENDING'
);

-- ============================================================
-- 4. DEMO USER 3 — Active with Credit Card
--    Login: hari@iic.edu.np | Password: Hari@12345
-- ============================================================
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

-- Active account for User 3
INSERT INTO accounts (account_number, user_id, balance, account_type, status, kyc_status)
VALUES (
    'AF-2026-00102',
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    78500.00,
    'SAVINGS',
    'ACTIVE',
    'APPROVED'
);

-- Credit card for User 3 (ACTIVE — already issued by admin)
INSERT INTO credit_cards (user_id, card_number, card_type, credit_limit, current_balance, status, expiry_date)
VALUES (
    (SELECT user_id FROM users WHERE email = 'hari@iic.edu.np'),
    '4111 1111 1111 1111',
    'GOLD',
    50000.00,
    12500.00,
    'ACTIVE',
    '12/28'
);

-- Sample transactions for User 3
INSERT INTO transactions (account_id, type, amount, balance_after, remarks, status)
VALUES
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00102'), 'DEPOSIT',    100000.00, 100000.00, 'Initial deposit',   'SUCCESS'),
    ((SELECT account_id FROM accounts WHERE account_number = 'AF-2026-00102'), 'WITHDRAWAL',  21500.00,  78500.00, 'College fee Q2',    'SUCCESS');

-- ============================================================
-- VERIFICATION QUERIES — Run these to confirm data
-- ============================================================
SELECT user_id, full_name, email, role, status FROM users;
SELECT a.account_id, u.full_name, a.account_number, a.balance, a.status, a.kyc_status
  FROM accounts a JOIN users u ON a.user_id = u.user_id;
SELECT * FROM credit_cards;
SELECT * FROM transactions;
