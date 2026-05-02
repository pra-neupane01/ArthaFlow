// UserDAO.java
package com.arthaflow.dao;

import com.arthaflow.model.User;
import com.arthaflow.util.DatabaseConnection;
import java.sql.*;

public class UserDAO {

    // Register new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, password, phone_number, full_name, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getFullName());
            ps.setString(5,user.getAddress());
            ps.setString(6, user.getRole());


            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while creating User"+e.getMessage());
            return false;
        }
    }

    // Get user by email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setFullName(rs.getString("full_name"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreatedDate(rs.getTimestamp("created_date"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching User"+e.getMessage());
        }
        return null;
    }

    // Get user by id
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setFullName(rs.getString("full_name"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreatedDate(rs.getTimestamp("created_date"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching User"+e.getMessage());

        }
        return null;
    }

    // Update user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, phone_number = ?, full_name = ?, address = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating User info"+e.getMessage());

            return false;
        }
    }

    // Delete user
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while removing User"+e.getMessage());
            return false;
        }
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }
}