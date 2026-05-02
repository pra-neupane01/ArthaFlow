CREATE database ARTHAFLOW;

USE ARTHAFLOW;

CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                        address VARCHAR(255) NOT NULL ,
                        phone_number VARCHAR(15) UNIQUE NOT NULL,
                       role ENUM('USER', 'ADMIN') DEFAULT 'USER',
                       status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
                          account_id INT PRIMARY KEY AUTO_INCREMENT,
                          account_number VARCHAR(20) UNIQUE NOT NULL,
                          user_id INT NOT NULL,
                          balance DECIMAL(15, 2) DEFAULT 0.00,
                          account_type VARCHAR(50) NOT NULL,
                          status ENUM('ACTIVE', 'CLOSED', 'FROZEN') DEFAULT 'ACTIVE',
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE transactions (
                              transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                              account_id INT NOT NULL,
                              type ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
                              amount DECIMAL(15, 2) NOT NULL,
                              balance_after DECIMAL(15, 2),
                              remarks VARCHAR(255),
                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              status ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'SUCCESS',
                              FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);
