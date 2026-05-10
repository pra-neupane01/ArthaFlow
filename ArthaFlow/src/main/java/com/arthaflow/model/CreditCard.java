package com.arthaflow.model;

import java.sql.Timestamp;

public class CreditCard {
    private int cardId;
    private int userId;
    private String cardNumber;
    private String cardType; // e.g., GOLD, PLATINUM
    private double creditLimit;
    private double currentBalance;
    private String status; // PENDING, ACTIVE, REJECTED
    private String expiryDate;
    private Timestamp createdDate;

    public CreditCard() {}

    public CreditCard(int cardId, int userId, String cardNumber, String cardType, double creditLimit, double currentBalance, String status, String expiryDate, Timestamp createdDate) {
        this.cardId = cardId;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.creditLimit = creditLimit;
        this.currentBalance = currentBalance;
        this.status = status;
        this.expiryDate = expiryDate;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    public double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }
    public double getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(double currentBalance) { this.currentBalance = currentBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}
