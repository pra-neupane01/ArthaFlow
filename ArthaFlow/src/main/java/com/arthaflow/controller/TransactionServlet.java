package com.arthaflow.controller;

import com.arthaflow.model.User;
import com.arthaflow.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class TransactionServlet extends HttpServlet {
    TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("deposit".equals(action)){
            req.getRequestDispatcher("/jsp/user/deposit.jsp").forward(req, resp);
        }else if("withdraw".equals(action)){
            req.getRequestDispatcher("/jsp/user/withdraw.jsp").forward(req, resp);
        }else{
            resp.sendRedirect(req.getContextPath() + "/user/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = req.getParameter("action");
        String amountStr = req.getParameter("amount");
        String description = req.getParameter("description");
        if (description == null)
            description = "";

        String jspPage = "deposit".equals(action)
                ? "/jsp/user/deposit.jsp"
                : "/jsp/user/withdraw.jsp";

        if(amountStr == null || amountStr.isEmpty()){
            req.setAttribute("error", "Please enter an amount.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }

        double amount;
        try{
            amount = Double.parseDouble(amountStr);
        }catch(NumberFormatException e){
            req.setAttribute("error", "Invalid amount format. Please enter a valid number.");
            req.getRequestDispatcher(jspPage).forward(req, resp);
            return;
        }
        if ("deposit".equals(action)) {
            boolean success = transactionService.deposit(user.getId(), amount, description);
            if(success){
                req.setAttribute("success", "Deposit of Rs. " + amount + " was successful.");
            }else{
                req.setAttribute("error", "Deposit failed. Please try again.");
            }
            req.getRequestDispatcher("/jsp/user/deposit.jsp").forward(req, resp);
        }else if ("withdraw".equals(action)) {
            String result = transactionService.withdraw(user.getId(), amount, description);
            if("success".equals(result)){
                req.setAttribute("success", "Withdrawal of Rs. " + amount + " was successful.");
            }else{
                req.setAttribute("error", result);
            }
            req.getRequestDispatcher("/jsp/user/withdraw.jsp").forward(req, resp);
        }else{
            resp.sendRedirect(req.getContextPath() + "/user/dashboard");
        }
    }
}
