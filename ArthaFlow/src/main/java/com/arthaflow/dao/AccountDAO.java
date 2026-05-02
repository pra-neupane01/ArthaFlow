package com.arthaflow.dao;

import com.arthaflow.model.Account;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Create a new bank account for user
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, balance, account_type) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account.getUserId());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getAccountType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while creating Account" + e.getMessage());
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
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                );
                return account;
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
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                );
                return account;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching account by ID: " + e.getMessage());
        }
        return null;
    }

    // Update account balance after deposit or withdrawal
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating account balance: " + e.getMessage());
            return false;
        }
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

    // Get all accounts (for admin)
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all accounts: " + e.getMessage());
        }
        return accounts;
    }
}
