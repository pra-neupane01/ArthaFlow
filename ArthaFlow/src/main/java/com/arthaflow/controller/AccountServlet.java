package com.arthaflow.controller;

import com.arthaflow.model.Account;
import com.arthaflow.model.User;
import com.arthaflow.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AccountServlet extends HttpServlet {
    AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        Account account = accountService.getAccountDetails(user.getUserId());
        req.setAttribute("account", account);
        req.getRequestDispatcher("/jsp/user/accountDetails.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String accountType = req.getParameter("accountType");

        if (accountType == null || accountType.isEmpty()) {
            req.setAttribute("error", "Please select an account type.");
            req.getRequestDispatcher("/jsp/user/accountDetails.jsp").forward(req, resp);
            return;
        }

        boolean created = accountService.createNewAccount(user.getUserId(), accountType);
        if (created) {
            resp.sendRedirect(req.getContextPath() + "/user/account");
        } else {
            req.setAttribute("error", "Account already exists or could not be created.");
            req.getRequestDispatcher("/jsp/user/accountDetails.jsp").forward(req, resp);
        }
    }
}
