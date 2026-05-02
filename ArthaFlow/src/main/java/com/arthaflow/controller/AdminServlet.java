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
        }else{
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }
}
