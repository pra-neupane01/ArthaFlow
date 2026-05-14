package com.arthaflow.dao;

import com.arthaflow.model.Account;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /**
     * @return generated account_id, or -1 on failure
     */
    public int insertPendingAccount(Account account, Connection conn) throws SQLException {
        String sql = "INSERT INTO accounts (user_id, balance, account_type, status) VALUES (?, ?, ?, 'PENDING')";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, account.getUserId());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getAccountType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY created_date DESC, account_id DESC LIMIT 1";
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
        return new Account(
                rs.getInt("account_id"),
                rs.getString("account_number"),
                rs.getInt("user_id"),
                rs.getDouble("balance"),
                rs.getString("account_type"),
                rs.getString("status"),
                rs.getTimestamp("created_date")
        );
    }

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

    public boolean issueAccountNumber(int accountId, String accountNumber) {
        String sql = "UPDATE accounts SET account_number = ?, status = 'ACTIVE' WHERE account_id = ?";
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

    public boolean accountNumberExists(String accountNumber) {
        if (accountNumber == null) return false;
        String sql = "SELECT 1 FROM accounts WHERE account_number = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account number: " + e.getMessage());
            return true;
        }
    }

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
