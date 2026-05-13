package com.arthaflow.service;

import com.arthaflow.dao.AccountDAO;
import com.arthaflow.dao.KycDetailsDAO;
import com.arthaflow.model.Account;
import com.arthaflow.model.KycDetails;
import com.arthaflow.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final KycDetailsDAO kycDetailsDAO = new KycDetailsDAO();

    public Account getAccountDetails(int userId) {
        Account account = accountDAO.getAccountByUserId(userId);
        if (account != null) {
            KycDetails k = kycDetailsDAO.findByAccountId(account.getAccountId());
            account.setKycStatus(k != null && k.getStatus() != null ? k.getStatus() : "PENDING");
        }
        return account;
    }

    public KycDetails getAccountOpeningKyc(int userId) {
        Account a = accountDAO.getAccountByUserId(userId);
        if (a == null) return null;
        return kycDetailsDAO.findByAccountId(a.getAccountId());
    }

    /**
     * @return null on success, or an error message
     */
    public String createAccountWithKyc(Account accountShell, KycDetails kyc) {
        if (accountDAO.getAccountByUserId(accountShell.getUserId()) != null) {
            return "You have already applied for an account.";
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int accountId = accountDAO.insertPendingAccount(accountShell, conn);
                if (accountId < 0) {
                    conn.rollback();
                    return "Could not create account row.";
                }
                kyc.setUserId(accountShell.getUserId());
                kyc.setPurpose(KycDetails.PURPOSE_ACCOUNT_OPENING);
                kyc.setAccountId(accountId);
                kyc.setCardId(null);
                kyc.setStatus("PENDING");
                if (!kycDetailsDAO.insert(kyc, conn)) {
                    conn.rollback();
                    return "Could not save KYC details.";
                }
                conn.commit();
                return null;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return e.getMessage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public double viewBalance(int userId) {
        Account account = getAccountDetails(userId);
        if (account != null && "ACTIVE".equals(account.getStatus())) {
            return account.getBalance();
        }
        return -1;
    }
}
