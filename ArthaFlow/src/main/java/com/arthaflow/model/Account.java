package com.arthaflow.model;

import java.sql.Timestamp;

public class Account {
    private int id;
    private String account_number;
    private int userId;
    private double balance;
    private String accountType;
    private String status;
    private Timestamp createdDate;

    public Account(int id, String account_number, int userId, double balance, String accountType, String status, Timestamp createdDate) {
        this.id = id;
        this.account_number = account_number;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}