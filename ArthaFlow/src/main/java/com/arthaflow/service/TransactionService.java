package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.TransactionDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    AccountDAO accountDAO = new AccountDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    //Deposit method
    public boolean deposit(int userId, double amount, String description) {
        if (amount <= 0) {
            return false;
        }
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return false;
        }
        double newBalance = account.getBalance() + amount;

        boolean balanceUpdated = accountDAO.updateBalance(account.getId(), newBalance);
        if (!balanceUpdated) {
            return false;
        }
        Transaction transaction = new Transaction(
                0, account.getId(), "DEPOSIT", amount, newBalance,
                description.isEmpty() ? "Deposit" : description, null
        );
        return transactionDAO.addTransaction(transaction);
    }

    //Withdrawal method
    public String withdraw(int userId, double amount, String description){
        if (amount <=0){
            return "Amount must be greater than zero";
        }
        Account account = accountDAO.getAccountByUserId(userId);
        if(account == null){
            return "Account not found";
        }
        if(account.getBalance() < amount){
            return "Insufficient balance";
        }
        double newBalance = account.getBalance() - amount;

        boolean balanceUpdated = accountDAO.updateBalance(account.getId(), newBalance);
        if(!balanceUpdated){
            return "Withdrawal failed. Please try again.";
        }
        Transaction transaction = new Transaction(
                0, account.getId(), "WITHDRAW", amount, newBalance,
                description.isEmpty() ? "Withdrawal" : description, null
        );
        transactionDAO.addTransaction(transaction);
        return "Withdrawal successful";
    }

    //Get transaction history
    public List<Transaction> getTransactionHistory(int userId){
        Account account = accountDAO.getAccountByUserId(userId);
        if (account == null) {
            return new ArrayList<>();
        }
        return transactionDAO.getTransactionsByAccountId(account.getId());
    }

    //Search transaction by type (Deposit / Withdrawal)
    public List<Transaction> searchByType(int userId, String type){
        Account account = accountDAO.getAccountByUserId(userId);
        if(account == null){
            return new ArrayList<>();
        }
        return transactionDAO.searchByType(account.getId(), type);
    }

//Search transaction by date range
    public List<Transaction> searchByDate(int userId, String fromDate, String toDate){
        Account account = accountDAO.getAccountByUserId(userId);
        if(account == null){
            return new ArrayList<>();
        }
        return transactionDAO.searchByDate(account.getId(), fromDate, toDate);
    }
}
