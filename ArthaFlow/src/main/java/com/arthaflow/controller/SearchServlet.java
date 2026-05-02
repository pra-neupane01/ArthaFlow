package com.arthaflow.controller;

import com.arthaflow.model.Transaction;
import com.arthaflow.model.User;
import com.arthaflow.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class SearchServlet extends HttpServlet {
    TransactionService transactionService = new TransactionService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        List<Transaction> transactions = transactionService.getTransactionHistory(user.getId());
        req.setAttribute("transactions", transactions);
        req.getRequestDispatcher("/jsp/user/transactionHistory.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String searchType = req.getParameter("searchType");
        List<Transaction> results = null;

        if ("type".equals(searchType)) {
            String transactionType = req.getParameter("transactionType");
            if (transactionType == null || transactionType.isEmpty()) {
                req.setAttribute("error", "Please select a transaction type.");
                req.getRequestDispatcher("/jsp/user/transactionHistory.jsp").forward(req, resp);
                return;
            }
            results = transactionService.searchByType(user.getId(), transactionType);

        } else if ("date".equals(searchType)) {
            String fromDate = req.getParameter("fromDate");
            String toDate = req.getParameter("toDate");
            if (fromDate == null || fromDate.isEmpty() || toDate == null || toDate.isEmpty()) {
                req.setAttribute("error", "Please provide both start and end dates.");
                req.getRequestDispatcher("/jsp/user/transactionHistory.jsp").forward(req, resp);
                return;
            }
            results = transactionService.searchByDate(user.getId(), fromDate, toDate);

        } else {
            results = transactionService.getTransactionHistory(user.getId());
        }
        if (results == null || results.isEmpty()) {
            req.setAttribute("info", "No transactions found.");
        }

        req.setAttribute("transactions", results);
        req.setAttribute("searchType", searchType);
        req.getRequestDispatcher("/jsp/user/transactionHistory.jsp").forward(req, resp);
    }
}