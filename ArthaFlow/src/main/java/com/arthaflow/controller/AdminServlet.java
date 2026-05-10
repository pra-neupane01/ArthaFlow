package com.arthaflow.controller;

import com.arthaflow.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminServlet extends HttpServlet {
    AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if("users".equals(action)){
            req.setAttribute("users", adminService.getAllUsers());
            req.setAttribute("accounts", adminService.getAllAccounts());
            req.getRequestDispatcher("/jsp/admin/userManagement.jsp").forward(req, resp);
        }else if("transactions".equals(action)){
            req.setAttribute("transactions", adminService.getAllTransactions());
            req.getRequestDispatcher("/jsp/admin/transactionHistory.jsp").forward(req, resp);

        }else if("creditCards".equals(action)){
            com.arthaflow.service.CreditCardService ccs = new com.arthaflow.service.CreditCardService();
            req.setAttribute("cardRequests", ccs.getAllRequests());
            req.getRequestDispatcher("/jsp/admin/creditCardManagement.jsp").forward(req, resp);

        }else if("reports".equals(action)){
            req.setAttribute("totalDeposits", adminService.getTotalDeposits());
            req.setAttribute("totalWithdrawals", adminService.getTotalWithdrawals());
            req.setAttribute("totalUsers", adminService.getAllUsers().size());
            req.setAttribute("totalAccounts", adminService.getAllAccounts().size());
            req.getRequestDispatcher("/jsp/admin/reports.jsp").forward(req, resp);

        }else{
            req.setAttribute("users", adminService.getAllUsers());
            req.setAttribute("transactions", adminService.getAllTransactions());
            req.setAttribute("totalDeposits", adminService.getTotalDeposits());
            req.setAttribute("totalWithdrawals", adminService.getTotalWithdrawals());
            req.getRequestDispatcher("/jsp/admin/adminDashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if("deleteUser".equals(action)){
            String userIdstr = req.getParameter("userId");
            if(userIdstr == null || userIdstr.isEmpty()){
                req.setAttribute("error", "Invalid user ID.");
                req.setAttribute("users", adminService.getAllUsers());
                req.getRequestDispatcher("/jsp/admin/userManagement.jsp").forward(req, resp);
                return;
            }
            int userId = Integer.parseInt(userIdstr);
            boolean deleted = adminService.deleteUser(userId);
            if(deleted){
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&success=User+deleted+successfully.");
            }else{
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&error=Failed+to+delete+user.");
            }
        } else if ("approveAccount".equals(action)) {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            if (adminService.approveAccount(accountId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&success=Account+approved+successfully.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&error=Failed+to+approve+account.");
            }
        } else if ("issueAccountNumber".equals(action)) {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            String accountNumber = req.getParameter("accountNumber");
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&error=Account+number+is+required.");
                return;
            }
            if (adminService.issueAccountNumber(accountId, accountNumber)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&success=Account+issued+successfully.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&error=Failed+to+issue+account+number.");
            }
        } else if ("rejectAccount".equals(action)) {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            if (adminService.rejectAccount(accountId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&success=Account+rejected+successfully.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&error=Failed+to+reject+account.");
            }
        } else if ("approveCard".equals(action)) {
            int cardId = Integer.parseInt(req.getParameter("cardId"));
            com.arthaflow.service.CreditCardService ccs = new com.arthaflow.service.CreditCardService();
            if (ccs.approveCard(cardId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&success=Card+approved.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&error=Failed+to+approve.");
            }
        } else if ("rejectCard".equals(action)) {
            int cardId = Integer.parseInt(req.getParameter("cardId"));
            com.arthaflow.service.CreditCardService ccs = new com.arthaflow.service.CreditCardService();
            if (ccs.rejectCard(cardId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&success=Card+rejected.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&error=Failed+to+reject.");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }
}
