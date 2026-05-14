package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.KycDetailsDAO;
import com.arthaflow.dao.TransactionDAO;
import com.arthaflow.dao.UserDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.KycDetails;
import com.arthaflow.model.Transaction;
import com.arthaflow.model.User;

import java.util.List;

public class AdminService {
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDAO = new AccountDAO();
    KycDetailsDAO kycDetailsDAO = new KycDetailsDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    public KycDetails getKycForAccountId(int accountId) {
        return kycDetailsDAO.findByAccountId(accountId);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public boolean deleteUser(int userId) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account != null) {
            accountDAO.deleteAccount(account.getAccountId());
        }
        return userDAO.deleteUser(userId);
    }

    public boolean approveAccount(int accountId) {
        return accountDAO.updateStatus(accountId, "ACTIVE");
    }

    public boolean issueAccountNumber(int accountId, String accountNumber) {
        boolean ok = accountDAO.issueAccountNumber(accountId, accountNumber);
        if (ok) {
            kycDetailsDAO.updateStatusByAccountId(accountId, "APPROVED");
        }
        return ok;
    }

    public boolean issueAccountNumberAuto(int accountId) {
        java.util.Random r = new java.util.Random();
        for (int attempt = 0; attempt < 60; attempt++) {
            String num = "AF" + String.format("%010d", Math.abs(r.nextLong()) % 10_000_000_000L);
            if (accountDAO.accountNumberExists(num)) {
                continue;
            }
            if (issueAccountNumber(accountId, num)) {
                return true;
            }
        }
        return false;
    }

    public List<Transaction> getAllTransactionsBetween(String fromDate, String toDate) {
        return transactionDAO.getAllTransactionsBetween(fromDate, toDate);
    }

    public boolean rejectAccount(int accountId, String rejectionRemarks) {
        boolean ok = accountDAO.updateStatus(accountId, "REJECTED");
        if (ok) {
            kycDetailsDAO.updateStatusAndRemarksByAccountId(accountId, "REJECTED", rejectionRemarks);
        }
        return ok;
    }

    public double getTotalDeposits() {
        List<Transaction> all = transactionDAO.getAllTransactions();
        double total = 0;
        for (Transaction t : all) {
            if ("DEPOSIT".equals(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getTotalWithdrawals() {
        List<Transaction> all = transactionDAO.getAllTransactions();
        double total = 0;
        for (Transaction t : all) {
            if ("WITHDRAWAL".equals(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }
}
