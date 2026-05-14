
USE ARTHAFLOW;

CREATE TABLE IF NOT EXISTS users (
    user_id         INT PRIMARY KEY AUTO_INCREMENT,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    full_name       VARCHAR(100) NOT NULL,
    address         VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(20) UNIQUE NOT NULL,
    role            ENUM('USER', 'ADMIN') DEFAULT 'USER',
    status          ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id      INT PRIMARY KEY AUTO_INCREMENT,
    account_number  VARCHAR(20) UNIQUE,
    user_id         INT NOT NULL,
    balance         DECIMAL(15, 2) DEFAULT 0.00,
    account_type    VARCHAR(50) NOT NULL,
    status          ENUM('PENDING', 'ACTIVE', 'CLOSED', 'FROZEN', 'REJECTED') DEFAULT 'PENDING',
    created_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   INT PRIMARY KEY AUTO_INCREMENT,
    account_id       INT NOT NULL,
    type             ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
    amount           DECIMAL(15, 2) NOT NULL,
    balance_after    DECIMAL(15, 2),
    remarks          VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status           ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'SUCCESS',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credit_cards (
    card_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    card_number     VARCHAR(20) UNIQUE,
    card_type       VARCHAR(20),
    credit_limit    DECIMAL(15, 2) DEFAULT 0,
    current_balance DECIMAL(15, 2) DEFAULT 0,
    status          ENUM('PENDING', 'ACTIVE', 'REJECTED') DEFAULT 'PENDING',
    expiry_date     VARCHAR(10),
    pin_hash        VARCHAR(255) DEFAULT NULL,
    created_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credit_card_transactions (
    cct_id             INT AUTO_INCREMENT PRIMARY KEY,
    card_id            INT NOT NULL,
    type               ENUM('PURCHASE') NOT NULL,
    amount             DECIMAL(15, 2) NOT NULL,
    balance_after      DECIMAL(15, 2) NOT NULL,
    remarks            VARCHAR(255),
    transaction_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status             ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'SUCCESS',
    FOREIGN KEY (card_id) REFERENCES credit_cards(card_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS kyc_details (
    kyc_id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT NOT NULL,
    purpose              ENUM('ACCOUNT_OPENING', 'CREDIT_CARD') NOT NULL,
    account_id           INT NULL,
    card_id              INT NULL,
    status               ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    citizenship_number   VARCHAR(50),
    date_of_birth        DATE,
    occupation           VARCHAR(200),
    father_name          VARCHAR(200),
    mother_name          VARCHAR(200),
    family_details       TEXT,
    gender               VARCHAR(30),
    permanent_address    VARCHAR(500),
    mailing_address      VARCHAR(500),
    id_document_path     VARCHAR(255),
    address_proof_path   VARCHAR(255),
    annual_income        DECIMAL(15, 2),
    minimum_income       DECIMAL(15, 2),
    personal_information TEXT,
    income_details       TEXT,
    employment_details   TEXT,
    card_preferences     TEXT,
    credit_information   TEXT,
    terms_accepted       TINYINT(1) DEFAULT 0,
    rejection_remarks    TEXT,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (card_id) REFERENCES credit_cards(card_id) ON DELETE CASCADE,
    UNIQUE KEY uk_kyc_account (account_id),
    UNIQUE KEY uk_kyc_card (card_id)
);
