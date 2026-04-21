package com.arthaflow.model;

import java.sql.Timestamp;

public class Account {
    private int id;
    private int userId;
    private double balance;
    private String accountType;
    private Timestamp createdDate;

    public Account(int id, int userId, double balance, String accountType, Timestamp createdDate) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
