package com.arthaflow.dao;

import com.arthaflow.model.CreditCard;
import com.arthaflow.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {

    public boolean applyForCard(CreditCard card) {
        String sql = "INSERT INTO credit_cards (user_id, card_type, status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, card.getUserId());
            ps.setString(2, card.getCardType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error applying for credit card: " + e.getMessage());
            return false;
        }
    }

    public CreditCard getCardByUserId(int userId) {
        String sql = "SELECT * FROM credit_cards WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCreditCard(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching credit card: " + e.getMessage());
        }
        return null;
    }

    public List<CreditCard> getAllCardRequests() {
        String sql = "SELECT * FROM credit_cards ORDER BY created_date DESC";
        List<CreditCard> cards = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cards.add(mapResultSetToCreditCard(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all card requests: " + e.getMessage());
        }
        return cards;
    }

    public boolean updateCardStatus(int cardId, String status, String cardNumber, String expiryDate, double limit) {
        String sql = "UPDATE credit_cards SET status = ?, card_number = ?, expiry_date = ?, credit_limit = ? WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, cardNumber);
            ps.setString(3, expiryDate);
            ps.setDouble(4, limit);
            ps.setInt(5, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating card status: " + e.getMessage());
            return false;
        }
    }

    private CreditCard mapResultSetToCreditCard(ResultSet rs) throws SQLException {
        return new CreditCard(
                rs.getInt("card_id"),
                rs.getInt("user_id"),
                rs.getString("card_number"),
                rs.getString("card_type"),
                rs.getDouble("credit_limit"),
                rs.getDouble("current_balance"),
                rs.getString("status"),
                rs.getString("expiry_date"),
                rs.getTimestamp("created_date")
        );
    }
}
