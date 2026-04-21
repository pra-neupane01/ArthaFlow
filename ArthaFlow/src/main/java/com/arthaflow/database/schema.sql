CREATE database ARTHAFLOW;

USE ARTHAFLOW;

CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100),
                       role ENUM('USER', 'ADMIN') DEFAULT 'USER',
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          user_id INT NOT NULL,
                          balance DECIMAL(15, 2) DEFAULT 0.00,
                          account_type VARCHAR(50),
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              account_id INT NOT NULL,
                              type ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
                              amount DECIMAL(15, 2) NOT NULL,
                              balance_after DECIMAL(15, 2),
                              description VARCHAR(255),
                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);