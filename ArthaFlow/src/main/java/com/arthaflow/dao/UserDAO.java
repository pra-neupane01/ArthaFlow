package com.arthaflow.dao;

import com.arthaflow.model.User;
import com.arthaflow.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setFullName(rs.getString("full_name"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedDate(rs.getTimestamp("created_date"));
        try { user.setProfilePicture(rs.getString("profile_picture")); } catch (SQLException ignored) {}
        return user;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, password, phone_number, full_name, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.out.println("Error fetching user by email: " + e.getMessage());
        }
        return null;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.out.println("Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email=?, phone_number=?, full_name=?, address=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProfilePicture(int userId, String picturePath) {
        String sql = "UPDATE users SET profile_picture=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, picturePath);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating profile picture: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) { return getUserByEmail(email) != null; }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY created_date DESC";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) users.add(mapUser(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }
}
