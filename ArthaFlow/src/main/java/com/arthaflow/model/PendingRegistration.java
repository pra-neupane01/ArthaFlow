package com.arthaflow.model;

import java.io.Serializable;
import java.time.Instant;

public class PendingRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String hashedPassword;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String otpHash;
    private Instant expiresAt;
    private int attempts;

    public PendingRegistration(String email, String hashedPassword, String fullName, String phoneNumber, String address,
                               String otpHash, Instant expiresAt) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.otpHash = otpHash;
        this.expiresAt = expiresAt;
        this.attempts = 0;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        attempts++;
    }

    public void resetAttempts() {
        attempts = 0;
    }

    public boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }
}
