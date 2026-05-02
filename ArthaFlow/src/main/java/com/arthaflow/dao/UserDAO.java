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
        String sql = "INSERT INTO users (email, password, full_name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());


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
                user.setFullName(rs.getString("full_name"));
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
                user.setFullName(rs.getString("full_name"));
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
        String sql = "UPDATE users SET email = ?, full_name = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getId());

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

    //Get all users
    public List<User> getAllUsers(){
        String sql = "SELECT * FROM users ORDERED BY created_date DESC";
        List<User> users = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("passowrd"));
                user.setFullName(rs.getString("full_name"));
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