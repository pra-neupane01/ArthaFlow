package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.TransactionDAO;
import com.arthaflow.dao.UserDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.Transaction;
import com.arthaflow.model.User;

import java.util.List;

public class AdminService {
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDAO = new AccountDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    //Get all users
    public List<User> getAllUsers(){
        return userDAO.getAllUsers();
    }

    //Get all accounts
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    //Get all transactions
    public List<Transaction> getAllTransactions(){
        return transactionDAO.getAllTransactions();
    }

    //Delete a user and thier account
    public boolean deleteUser(int userId){
        Account account = accountDAO.getAccountByUserId(userId);
        if(account != null){
            accountDAO.deleteAccount(account.getId());
        }
        return userDAO.deleteUser(userId);
    }

    //Total deposits across all accounts
    public double getTotalDeposits(){
        List<Transaction> all = transactionDAO.getAllTransactions();
        double total = 0;
        for(Transaction t : all){
            if("DEPOSIT".equals(t.getType())){
                total += t.getAmount();
            }
        }
        return total;
    }

    //Total withdrawals across all accounts
    public double getTotalWithdrawals(){
        List<Transaction> all = transactionDAO.getAllTransactions();
        double total = 0;
        for(Transaction t : all){
            if("WITHDRAW".equals(t.getType())){
                total += t.getAmount();
            }
        }
        return total;
    }

}
