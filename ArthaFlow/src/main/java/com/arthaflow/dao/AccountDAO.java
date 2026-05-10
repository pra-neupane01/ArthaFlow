package com.arthaflow.dao;

import com.arthaflow.model.Account;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account management.
 */
public class AccountDAO {

    // Create a new bank account request for user - starts as PENDING
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, balance, account_type, status, id_document_path, address_proof_path, kyc_status) VALUES (?, ?, ?, 'PENDING', ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account.getUserId());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getAccountType());
            ps.setString(4, account.getIdDocumentPath());
            ps.setString(5, account.getAddressProofPath());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while creating Account: " + e.getMessage());
            return false;
        }
    }

    // Get account by user ID
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching Account by user ID: " + e.getMessage());
        }
        return null;
    }

    // Get account by account ID
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching account by ID: " + e.getMessage());
        }
        return null;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account(
                rs.getInt("account_id"),
                rs.getString("account_number"),
                rs.getInt("user_id"),
                rs.getDouble("balance"),
                rs.getString("account_type"),
                rs.getString("status"),
                rs.getTimestamp("created_date")
        );
        account.setIdDocumentPath(rs.getString("id_document_path"));
        account.setAddressProofPath(rs.getString("address_proof_path"));
        account.setKycStatus(rs.getString("kyc_status"));
        return account;
    }

    // Update account balance - supports transactions
    public boolean updateBalance(int accountId, double newBalance, Connection conn) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    // Admin Issues Account Number and activates account
    public boolean issueAccountNumber(int accountId, String accountNumber) {
        String sql = "UPDATE accounts SET account_number = ?, status = 'ACTIVE', kyc_status = 'APPROVED' WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error issuing account number: " + e.getMessage());
            return false;
        }
    }

    // Update account status (ACTIVE, PENDING, REJECTED)
    public boolean updateStatus(int accountId, String status) {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating account status: " + e.getMessage());
            return false;
        }
    }

    // Get all accounts (for admin)
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts ORDER BY created_date DESC";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all accounts: " + e.getMessage());
        }
        return accounts;
    }

    // Delete account by account ID
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
}
