package com.arthaflow.dao;

import com.arthaflow.model.CreditCardTransaction;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditCardTransactionDAO {

    public boolean insert(CreditCardTransaction tx, Connection conn) throws SQLException {
        String sql = "INSERT INTO credit_card_transactions (card_id, type, amount, balance_after, remarks, status) VALUES (?, ?, ?, ?, ?, ?)";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tx.getCardId());
            ps.setString(2, tx.getType());
            ps.setDouble(3, tx.getAmount());
            ps.setDouble(4, tx.getBalanceAfter());
            ps.setString(5, tx.getRemarks());
            ps.setString(6, tx.getStatus());
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    public List<CreditCardTransaction> listByCardId(int cardId) {
        String sql = "SELECT * FROM credit_card_transactions WHERE card_id = ? ORDER BY transaction_date DESC";
        List<CreditCardTransaction> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error listing card transactions: " + e.getMessage());
        }
        return list;
    }

    private CreditCardTransaction map(ResultSet rs) throws SQLException {
        CreditCardTransaction t = new CreditCardTransaction();
        t.setCctId(rs.getInt("cct_id"));
        t.setCardId(rs.getInt("card_id"));
        t.setType(rs.getString("type"));
        t.setAmount(rs.getDouble("amount"));
        t.setBalanceAfter(rs.getDouble("balance_after"));
        t.setRemarks(rs.getString("remarks"));
        t.setTransactionDate(rs.getTimestamp("transaction_date"));
        t.setStatus(rs.getString("status"));
        return t;
    }
}
