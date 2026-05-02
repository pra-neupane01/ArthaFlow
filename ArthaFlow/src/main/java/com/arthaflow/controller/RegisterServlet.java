package com.arthaflow.controller;

import com.arthaflow.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String password = req.getParameter("password");
        String address = req.getParameter("address");

        boolean success = userService.registerNewUser(email, password, fullName, phoneNumber, address);

        if (success) {
            req.setAttribute("success", "Registration successful! Please Login to continue.");
            req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Registration failed. Please check your details and try again.");
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
        }
    }
}
