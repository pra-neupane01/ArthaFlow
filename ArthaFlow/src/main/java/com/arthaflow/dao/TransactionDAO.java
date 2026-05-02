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
//    Add a new transaction record
    public boolean addTransaction(Transaction transaction){
        String sql = "INSERT INTO transactions (account_id, type, amount, balance_after, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, transaction.getAccountId());
            ps.setString(2, transaction.getType());
            ps.setDouble(3, transaction.getAmount());
            ps.setDouble(4, transaction.getBalanceAfter());
            ps.setString(5, transaction.getDescription());
            return ps.executeUpdate()>0;
        } catch (SQLException e){
            System.out.println("Error aading transaction: "+e.getMessage());
            return false;
        }
    }
//    Get all transaction for a specific account
    public List<Transaction> getTransactionsByAccountId(int accountId){
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("description"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }

        } catch (SQLException e){
            System.out.println("Error fetching transactions: "+e.getMessage());
        }
        return transactions;
    }
//    Get all transactions (for admin)
    public List<Transaction> getAllTransactions(){
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("description"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e){
            System.out.println("Error fetching transactions: "+e.getMessage());
        }
        return transactions;
    }

//    Search transaction by type (Deposit / Withdrawal)
    public List<Transaction> searchByType(int accountId, String type){
        String sql = "SELECT * FROM transaction WHERE account_id = ? AND type = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("description"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        }catch (SQLException e){
            System.out.println("Error searching transactions: "+e.getMessage());
        }
        return transactions;
    }

//    Search transaction by date range
    public List<Transaction> searchByDate(int accountId, String fromDate, String toDate){
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND DATE(transaction_date) BETWEEN ? AND ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("description"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        }catch (SQLException e){
            System.out.println("Error searching transactions: "+e.getMessage());
        }
        return transactions;
    }

}
