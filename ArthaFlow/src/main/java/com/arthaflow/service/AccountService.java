package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.model.Account;

public class AccountService {
    AccountDAO accountDAO = new AccountDAO();

    // Create a new account — one account per user only
    public boolean createNewAccount(int userId, String accountType) {
        Account existing = accountDAO.getAccountByUserId(userId);
        if (existing != null) {
            return false;
        }
        Account account = new Account(0, null, userId, 0.00, accountType, "ACTIVE", null);
        return accountDAO.createAccount(account);
    }

    // Get account details for a user
    public Account getAccountDetails(int userId) {
        return accountDAO.getAccountByUserId(userId);
    }

    // View current balance
    public double viewBalance(int userId) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account != null) {
            return account.getBalance();
        }
        return -1;
    }
}
