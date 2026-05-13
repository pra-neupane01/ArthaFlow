package com.arthaflow.dao;

import com.arthaflow.model.CreditCard;
import com.arthaflow.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {

    /** Insert pending application; returns generated card_id or -1 */
    public int insertPendingCard(CreditCard card, Connection conn) throws SQLException {
        String sql = "INSERT INTO credit_cards (user_id, card_type, status) VALUES (?, ?, 'PENDING')";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, card.getUserId());
            ps.setString(2, card.getCardType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
        return -1;
    }

    public CreditCard getCardByUserId(int userId) {
        String sql = "SELECT * FROM credit_cards WHERE user_id = ? ORDER BY card_id DESC LIMIT 1";
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

    public CreditCard getCardById(int cardId) {
        String sql = "SELECT * FROM credit_cards WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCreditCard(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching credit card by id: " + e.getMessage());
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

    public boolean cardNumberExists(String cardNumber) {
        if (cardNumber == null) return false;
        String sql = "SELECT 1 FROM credit_cards WHERE card_number = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking card number: " + e.getMessage());
            return true;
        }
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

    public boolean updatePinHash(int cardId, String pinHash) {
        String sql = "UPDATE credit_cards SET pin_hash = ? WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pinHash);
            ps.setInt(2, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating PIN: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCurrentBalance(int cardId, double newBalance, Connection conn) throws SQLException {
        String sql = "UPDATE credit_cards SET current_balance = ? WHERE card_id = ?";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, cardId);
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    public boolean rejectCard(int cardId) {
        try {
            return rejectCard(cardId, null);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean rejectCard(int cardId, Connection conn) throws SQLException {
        String sql = "UPDATE credit_cards SET status = 'REJECTED' WHERE card_id = ?";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    private CreditCard mapResultSetToCreditCard(ResultSet rs) throws SQLException {
        CreditCard c = new CreditCard(
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
        String ph = rs.getString("pin_hash");
        if (rs.wasNull()) {
            c.setPinHash(null);
        } else {
            c.setPinHash(ph);
        }
        return c;
    }
}
