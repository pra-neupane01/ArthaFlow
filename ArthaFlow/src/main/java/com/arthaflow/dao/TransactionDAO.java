package com.arthaflow.dao;

import com.arthaflow.model.Transaction;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Add a new transaction record - supports transactions
    public boolean addTransaction(Transaction transaction, Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount, balance_after, remarks, status) VALUES (?, ?, ?, ?, ?, ?)";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.getAccountId());
            ps.setString(2, transaction.getType());
            ps.setDouble(3, transaction.getAmount());
            ps.setDouble(4, transaction.getBalanceAfter());
            ps.setString(5, transaction.getRemarks());
            ps.setString(6, transaction.getStatus());
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    // Get all transactions for a specific account
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Get all transactions (for admin)
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Search transactions by type (DEPOSIT / WITHDRAWAL)
    public List<Transaction> searchByType(int accountId, String type) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND type = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error searching transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Search transactions by date range
    public List<Transaction> searchByDate(int accountId, String fromDate, String toDate) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND DATE(transaction_date) BETWEEN ? AND ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("remarks"),
                        rs.getString("status"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error searching transactions: " + e.getMessage());
        }
        return transactions;
    }
}
