package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.TransactionDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.Transaction;
import com.arthaflow.service.AccountService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class handling all financial transactions.
 */
public class TransactionService {
    AccountDAO accountDAO = new AccountDAO();
    AccountService accountService = new AccountService();
    TransactionDAO transactionDAO = new TransactionDAO();

    // Deposit money into account - Uses Database Transactions for ACID
    public boolean deposit(int userId, double amount, String description) {
        if (amount <= 0) return false;

        Account account = accountService.getAccountDetails(userId);
        if (account == null || !"ACTIVE".equals(account.getStatus())) {
            return false;
        }
        
        if (!"APPROVED".equals(account.getKycStatus())) {
            System.out.println("Transaction blocked: KYC not approved for user " + userId);
            return false;
        }

        double newBalance = account.getBalance() + amount;
        
        try (java.sql.Connection conn = com.arthaflow.util.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (accountDAO.updateBalance(account.getAccountId(), newBalance, conn)) {
                    Transaction transaction = new Transaction(
                            0, account.getAccountId(), "DEPOSIT", amount, newBalance,
                            description == null || description.isEmpty() ? "Deposit" : description, "SUCCESS", null
                    );
                    if (transactionDAO.addTransaction(transaction, conn)) {
                        conn.commit();
                        return true;
                    }
                }
                conn.rollback();
            } catch (java.sql.SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("Deposit transaction failed: " + e.getMessage());
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Database error during deposit: " + e.getMessage());
        }
        return false;
    }

    // Withdraw money from account - Uses Database Transactions for ACID
    public String withdraw(int userId, double amount, String description) {
        if (amount <= 0) return "Amount must be greater than zero";

        Account account = accountService.getAccountDetails(userId);
        if (account == null) return "Account not found";
        if (!"ACTIVE".equals(account.getStatus())) return "Account is not active";
        if (!"APPROVED".equals(account.getKycStatus())) return "KYC verification pending. Please complete KYC to transact.";
        if (account.getBalance() < amount) return "Insufficient balance";

        double newBalance = account.getBalance() - amount;

        try (java.sql.Connection conn = com.arthaflow.util.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (accountDAO.updateBalance(account.getAccountId(), newBalance, conn)) {
                    Transaction transaction = new Transaction(
                            0, account.getAccountId(), "WITHDRAWAL", amount, newBalance,
                            description == null || description.isEmpty() ? "Withdrawal" : description, "SUCCESS", null
                    );
                    if (transactionDAO.addTransaction(transaction, conn)) {
                        conn.commit();
                        return "Withdrawal successful";
                    }
                }
                conn.rollback();
            } catch (java.sql.SQLException e) {
                if (conn != null) conn.rollback();
                System.out.println("Withdrawal transaction failed: " + e.getMessage());
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Database error during withdrawal: " + e.getMessage());
        }
        return "Withdrawal failed. Please try again.";
    }

    public List<Transaction> getTransactionHistoryFiltered(int userId, String fromDate, String toDate) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return new ArrayList<>();
        }
        return transactionDAO.searchByDate(account.getAccountId(), fromDate, toDate);
    }

    // Get full transaction history for a user
    public List<Transaction> getTransactionHistory(int userId) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return new ArrayList<>();
        }
        return transactionDAO.getTransactionsByAccountId(account.getAccountId());
    }

    // Search by transaction type (DEPOSIT or WITHDRAWAL)
    public List<Transaction> searchByType(int userId, String type) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return new ArrayList<>();
        }
        return transactionDAO.searchByType(account.getAccountId(), type);
    }

    // Search by date range
    public List<Transaction> searchByDate(int userId, String fromDate, String toDate) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return new ArrayList<>();
        }
        return transactionDAO.searchByDate(account.getAccountId(), fromDate, toDate);
    }
}
