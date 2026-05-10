package com.arthaflow.controller;

import com.arthaflow.model.Account;
import com.arthaflow.model.User;
import com.arthaflow.service.AccountService;
import com.arthaflow.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class TransactionServlet extends HttpServlet {
    TransactionService transactionService = new TransactionService();
    AccountService     accountService     = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        if ("deposit".equals(action)) {
            setKycWarningIfNeeded(req, user);
            req.getRequestDispatcher("/jsp/user/deposit.jsp").forward(req, resp);

        } else if ("withdraw".equals(action)) {
            setKycWarningIfNeeded(req, user);
            req.getRequestDispatcher("/jsp/user/withdraw.jsp").forward(req, resp);

        } else {
            // history (default)
            req.setAttribute("transactions", transactionService.getTransactionHistory(user.getUserId()));
            req.getRequestDispatcher("/jsp/user/transactionHistory.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String action      = req.getParameter("action");
        String amountStr   = req.getParameter("amount");
        String description = req.getParameter("description");
        if (description == null) description = "";

        String jspPage = "deposit".equals(action) ? "/jsp/user/deposit.jsp" : "/jsp/user/withdraw.jsp";

        // --- Pre-flight: check account & KYC ---
        Account account = accountService.getAccountDetails(user.getUserId());
        if (account == null) {
            req.setAttribute("error", "You do not have a bank account yet. Please submit your KYC documents to open an account before making transactions.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }
        if (!"ACTIVE".equals(account.getStatus())) {
            req.setAttribute("error", "Your account is currently " + account.getStatus() + ". Please complete KYC verification. Once our admin approves your documents and issues your account number, you can start transacting.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }
        if (!"APPROVED".equals(account.getKycStatus())) {
            req.setAttribute("error", "KYC verification is still pending. Please wait for admin approval before performing transactions.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }

        // --- Amount validation ---
        if (amountStr == null || amountStr.isEmpty()) {
            req.setAttribute("error", "Please enter an amount.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid amount. Please enter a positive number.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }

        if ("deposit".equals(action)) {
            boolean success = transactionService.deposit(user.getUserId(), amount, description);
            if (success) {
                req.setAttribute("success", String.format("Deposit of Rs. %,.2f was successful!", amount));
            } else {
                req.setAttribute("error", "Deposit failed. Please try again or contact support.");
            }
            req.getRequestDispatcher("/jsp/user/deposit.jsp").forward(req, resp);

        } else if ("withdraw".equals(action)) {
            String result = transactionService.withdraw(user.getUserId(), amount, description);
            if ("Withdrawal successful".equals(result)) {
                req.setAttribute("success", String.format("Withdrawal of Rs. %,.2f was successful!", amount));
            } else {
                req.setAttribute("error", result);
            }
            req.getRequestDispatcher("/jsp/user/withdraw.jsp").forward(req, resp);

        } else {
            resp.sendRedirect(req.getContextPath() + "/user/dashboard");
        }
    }

    /** Sets a KYC warning on the request if the user has no active KYC-approved account */
    private void setKycWarningIfNeeded(HttpServletRequest req, User user) {
        Account account = accountService.getAccountDetails(user.getUserId());
        if (account == null) {
            req.setAttribute("kycWarning", "You don't have a bank account yet. Please open an account and complete KYC verification before transacting.");
        } else if (!"ACTIVE".equals(account.getStatus())) {
            req.setAttribute("kycWarning", "Your account is pending KYC approval. You can only transact after our admin verifies your documents.");
        }
    }
}
