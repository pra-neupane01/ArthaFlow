
package com.arthaflow.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String role;
    private String status;
    private String profilePicture;
    private Timestamp createdDate;

    public User() {}

    public User(int userId, String email, String password, String fullName, String address,
                String phoneNumber, String role, String status, Timestamp createdDate) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getUserId()    { return userId; }
    public int getId()        { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail()  { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole()   { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    /** Returns the first letter of the full name for avatar fallback */
    public String getInitials() {
        if (fullName == null || fullName.isEmpty()) return "?";
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length >= 2) return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return String.valueOf(parts[0].charAt(0)).toUpperCase();
    }
}
