package com.arthaflow.model;

import java.sql.Timestamp;

public class CreditCardTransaction {
    private int cctId;
    private int cardId;
    private String type;
    private double amount;
    private double balanceAfter;
    private String remarks;
    private Timestamp transactionDate;
    private String status;

    public CreditCardTransaction() {}

    public int getCctId() { return cctId; }
    public void setCctId(int cctId) { this.cctId = cctId; }
    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
