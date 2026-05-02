package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.model.Account;

public class AccountService {
    AccountDAO accountDAO = new AccountDAO();

    public boolean createNewAccount(int userId, String accountType){
        Account existing = accountDAO.getAccountByUserId(userId);
        if(existing != null) {
            return false;
        }
        Account account = new Account(0, userId, 0.00, accountType, null);
        return accountDAO.createAccount(account);
    }

    public Account getAccountDetails(int userId){
        return accountDAO.getAccountByUserId(userId);
    }

    public double viewBalance(int userId){
        Account account = accountDAO.getAccountByUserId(userId);
        if(account != null){
            return account.getBalance();
        }
        return -1;
    }
}
