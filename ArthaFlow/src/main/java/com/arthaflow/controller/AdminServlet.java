package com.arthaflow.controller;

import com.arthaflow.model.Account;
import com.arthaflow.model.CreditCard;
import com.arthaflow.model.KycDetails;
import com.arthaflow.service.AdminService;
import com.arthaflow.service.CreditCardService;
import com.arthaflow.util.DateRangeHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminServlet extends HttpServlet {
    AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if("users".equals(action)){
            req.setAttribute("users", adminService.getAllUsers());
            req.setAttribute("accounts", adminService.getAllAccounts());
            java.util.Map<Integer, KycDetails> accountKycByAccountId = new java.util.HashMap<>();
            for (Account a : adminService.getAllAccounts()) {
                KycDetails k = adminService.getKycForAccountId(a.getAccountId());
                if (k != null) {
                    accountKycByAccountId.put(a.getAccountId(), k);
                }
            }
            req.setAttribute("accountKycByAccountId", accountKycByAccountId);
            req.getRequestDispatcher("/jsp/admin/userManagement.jsp").forward(req, resp);
        }else if("transactions".equals(action)){
            String range = req.getParameter("range");
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            String[] bounds = DateRangeHelper.resolve(range, from, to);
            if (bounds == null) {
                req.setAttribute("transactions", adminService.getAllTransactions());
            } else {
                req.setAttribute("transactions", adminService.getAllTransactionsBetween(bounds[0], bounds[1]));
            }
            req.setAttribute("range", range != null ? range : "all");
            req.setAttribute("from", from != null ? from : "");
            req.setAttribute("to", to != null ? to : "");
            req.getRequestDispatcher("/jsp/admin/transactionHistory.jsp").forward(req, resp);

        }else if("creditCards".equals(action)){
            CreditCardService ccs = new CreditCardService();
            List<CreditCard> cardRequests = ccs.getAllRequests();
            Map<Integer, KycDetails> kycByCardId = new HashMap<>();
            for (CreditCard cc : cardRequests) {
                KycDetails k = ccs.getKycForCard(cc.getCardId());
                if (k != null) {
                    kycByCardId.put(cc.getCardId(), k);
                }
            }
            req.setAttribute("cardRequests", cardRequests);
            req.setAttribute("kycByCardId", kycByCardId);
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
            if (adminService.issueAccountNumberAuto(accountId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=users&success=Account+number+issued+by+bank.");
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
        } else if ("verifyCardKyc".equals(action)) {
            int cardId = Integer.parseInt(req.getParameter("cardId"));
            CreditCardService ccs = new CreditCardService();
            if (ccs.verifyCardKyc(cardId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&success=KYC+verified.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&error=Cannot+verify+KYC.");
            }
        } else if ("issueCard".equals(action)) {
            int cardId = Integer.parseInt(req.getParameter("cardId"));
            CreditCardService ccs = new CreditCardService();
            if (ccs.issueCardAfterKyc(cardId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&success=Card+issued.+User+must+set+PIN.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&error=Issue+failed.+Ensure+KYC+is+approved.");
            }
        } else if ("rejectCard".equals(action)) {
            int cardId = Integer.parseInt(req.getParameter("cardId"));
            CreditCardService ccs = new CreditCardService();
            if (ccs.rejectCardApplication(cardId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&success=Application+rejected.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?action=creditCards&error=Failed+to+reject.");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }
}
