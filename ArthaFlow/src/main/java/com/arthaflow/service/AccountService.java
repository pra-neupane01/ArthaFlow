package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.model.Account;

public class AccountService {
    AccountDAO accountDAO = new AccountDAO();

    // Create a new account request with KYC
    public boolean createNewAccount(Account account) {
        Account existing = accountDAO.getAccountByUserId(account.getUserId());
        if (existing != null) {
            return false;
        }
        return accountDAO.createAccount(account);
    }

    // Get account details for a user
    public Account getAccountDetails(int userId) {
        return accountDAO.getAccountByUserId(userId);
    }

    // View current balance
    public double viewBalance(int userId) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account != null && "ACTIVE".equals(account.getStatus())) {
            return account.getBalance();
        }
        return -1;
    }
}
