package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.TransactionDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.Transaction;
import com.arthaflow.service.AccountService;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public String transfer(int senderUserId, String receiverAccountNumber, double amount, String description) {
        if (amount <= 0) return "Amount must be greater than zero";

        String normalizedReceiverAccountNumber = normalizeAccountNumber(receiverAccountNumber);
        if (normalizedReceiverAccountNumber == null || normalizedReceiverAccountNumber.isEmpty()) {
            return "Receiver account number is required.";
        }

        Account senderSnapshot = accountService.getAccountDetails(senderUserId);
        if (senderSnapshot == null) return "Account not found";
        if (!"ACTIVE".equals(senderSnapshot.getStatus())) return "Your account is not active";
        if (!"APPROVED".equals(senderSnapshot.getKycStatus())) {
            return "KYC verification pending. Please complete KYC to transfer funds.";
        }
        if (senderSnapshot.getAccount_number() == null || senderSnapshot.getAccount_number().isEmpty()) {
            return "Your account number has not been issued yet.";
        }
        if (senderSnapshot.getBalance() < amount) return "Insufficient balance";

        Account receiverSnapshot = accountDAO.getAccountByAccountNumber(normalizedReceiverAccountNumber);
        if (receiverSnapshot == null) return "Receiver account not found";
        if (senderSnapshot.getAccountId() == receiverSnapshot.getAccountId()) {
            return "You cannot transfer money to your own account.";
        }
        if (!"ACTIVE".equals(receiverSnapshot.getStatus())) {
            return "Receiver account is not active.";
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int firstAccountId = Math.min(senderSnapshot.getAccountId(), receiverSnapshot.getAccountId());
                int secondAccountId = Math.max(senderSnapshot.getAccountId(), receiverSnapshot.getAccountId());

                Account firstLocked = accountDAO.getAccountById(firstAccountId, conn, true);
                Account secondLocked = accountDAO.getAccountById(secondAccountId, conn, true);
                if (firstLocked == null || secondLocked == null) {
                    conn.rollback();
                    return "Could not load transfer accounts. Please try again.";
                }

                Account sender = firstLocked.getAccountId() == senderSnapshot.getAccountId() ? firstLocked : secondLocked;
                Account receiver = firstLocked.getAccountId() == receiverSnapshot.getAccountId() ? firstLocked : secondLocked;

                if (!"ACTIVE".equals(sender.getStatus())) {
                    conn.rollback();
                    return "Your account is not active";
                }
                if (!"ACTIVE".equals(receiver.getStatus())) {
                    conn.rollback();
                    return "Receiver account is not active.";
                }
                if (sender.getBalance() < amount) {
                    conn.rollback();
                    return "Insufficient balance";
                }

                double senderNewBalance = sender.getBalance() - amount;
                double receiverNewBalance = receiver.getBalance() + amount;
                String baseRemarks = description == null || description.trim().isEmpty()
                        ? "Account transfer"
                        : description.trim();

                boolean senderUpdated = accountDAO.updateBalance(sender.getAccountId(), senderNewBalance, conn);
                boolean receiverUpdated = accountDAO.updateBalance(receiver.getAccountId(), receiverNewBalance, conn);
                if (!senderUpdated || !receiverUpdated) {
                    conn.rollback();
                    return "Transfer failed. Please try again.";
                }

                Transaction senderTransaction = new Transaction(
                        0, sender.getAccountId(), "WITHDRAWAL", amount, senderNewBalance,
                        baseRemarks + " | Transfer to " + receiver.getAccount_number(), "SUCCESS", null
                );
                Transaction receiverTransaction = new Transaction(
                        0, receiver.getAccountId(), "DEPOSIT", amount, receiverNewBalance,
                        baseRemarks + " | Transfer from " + sender.getAccount_number(), "SUCCESS", null
                );

                boolean senderLogged = transactionDAO.addTransaction(senderTransaction, conn);
                boolean receiverLogged = transactionDAO.addTransaction(receiverTransaction, conn);
                if (senderLogged && receiverLogged) {
                    conn.commit();
                    return "Transfer successful";
                }

                conn.rollback();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transfer transaction failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database error during transfer: " + e.getMessage());
        }

        return "Transfer failed. Please try again.";
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

    private String normalizeAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        return accountNumber.trim().replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
    }
}
