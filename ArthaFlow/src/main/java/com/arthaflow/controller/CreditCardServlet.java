package com.arthaflow.controller;

import com.arthaflow.model.CreditCard;
import com.arthaflow.model.User;
import com.arthaflow.service.CreditCardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CreditCardServlet extends HttpServlet {
    private CreditCardService cardService = new CreditCardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        CreditCard card = cardService.getCardDetails(user.getUserId());
        req.setAttribute("creditCard", card);
        req.getRequestDispatcher("/jsp/user/creditCard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = req.getParameter("action");

        if ("apply".equals(action)) {
            String cardType = req.getParameter("cardType");
            if (cardService.applyForCard(user.getUserId(), cardType)) {
                resp.sendRedirect(req.getContextPath() + "/user/creditCard?success=Application+submitted.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=Failed+to+apply.");
            }
        }
    }
}
