// UserDAO.java
package com.arthaflow.dao;

import com.arthaflow.model.User;
import com.arthaflow.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                user.setUserId(rs.getInt("user_id"));
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
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
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
        String sql = "UPDATE users SET email = ?, phone_number = ?, full_name = ?, address = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating User info"+e.getMessage());

            return false;
        }
    }

    // Delete user
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
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

    //Get all users
    public List<User> getAllUsers(){
        String sql = "SELECT * FROM users ORDER BY created_date DESC";
        List<User> users = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreatedDate(rs.getTimestamp("created_date"));
                users.add(user);
            }
        }catch (SQLException e){
            System.out.println("Error fetching all users: "+e.getMessage());
        }
        return users;
    }
}
