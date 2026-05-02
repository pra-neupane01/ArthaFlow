package com.arthaflow.model;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int accountId;
    private String type;
    private double amount;
    private double balanceAfter;
    private String remarks;
    private String status;
    private Timestamp transactionDate;

    public Transaction() {};


    public Transaction(int id, int accountId, String type, double amount, double balanceAfter, String remarks, String staus, Timestamp transactionDate) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.remarks = remarks;
        this.status = staus;
        this.transactionDate = transactionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStaus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
