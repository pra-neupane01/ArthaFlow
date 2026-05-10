CREATE DATABASE IF NOT EXISTS ARTHAFLOW;

USE ARTHAFLOW;

CREATE TABLE IF NOT EXISTS users (
    user_id       INT PRIMARY KEY AUTO_INCREMENT,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    address       VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(20) UNIQUE NOT NULL,
    role          ENUM('USER', 'ADMIN') DEFAULT 'USER',
    status        ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id        INT PRIMARY KEY AUTO_INCREMENT,
    account_number    VARCHAR(20) UNIQUE,
    user_id           INT NOT NULL,
    balance           DECIMAL(15, 2) DEFAULT 0.00,
    account_type      VARCHAR(50) NOT NULL,
    status            ENUM('PENDING', 'ACTIVE', 'CLOSED', 'FROZEN', 'REJECTED') DEFAULT 'PENDING',
    id_document_path  VARCHAR(255),
    address_proof_path VARCHAR(255),
    kyc_status        ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id     INT NOT NULL,
    type           ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
    amount         DECIMAL(15, 2) NOT NULL,
    balance_after  DECIMAL(15, 2),
    remarks        VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status         ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'SUCCESS',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credit_cards (
    card_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    card_number     VARCHAR(20),
    card_type       VARCHAR(20),
    credit_limit    DECIMAL(15, 2) DEFAULT 0,
    current_balance DECIMAL(15, 2) DEFAULT 0,
    status          ENUM('PENDING', 'ACTIVE', 'REJECTED') DEFAULT 'PENDING',
    expiry_date     VARCHAR(10),
    created_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Run this if your database already exists (to add profile_picture):
-- ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_picture VARCHAR(255) DEFAULT NULL;
