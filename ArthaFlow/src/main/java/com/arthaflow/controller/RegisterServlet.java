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
        if (phoneNumber != null && phoneNumber.length() == 10 && (phoneNumber.startsWith("98") || phoneNumber.startsWith("97"))) {
            phoneNumber = "+977" + phoneNumber;
        }
        String password = req.getParameter("password");
        String address = req.getParameter("address");

        String errorMsg = null;
        if (!com.arthaflow.util.ValidationService.isValidEmail(email)) {
            errorMsg = "Invalid email format.";
        } else if (com.arthaflow.util.ValidationService.isEmailExists(email)) {
            errorMsg = "Email already registered.";
        } else if (!com.arthaflow.util.ValidationService.isValidphoneNumber(phoneNumber)) {
            errorMsg = "Invalid phone number. Use +977 or 10 digits.";
        } else if (!com.arthaflow.util.ValidationService.isValidPassword(password)) {
            errorMsg = "Password must be 8+ chars, with Uppercase, Lowercase, Number, and Special Char.";
        } else if (fullName == null || fullName.trim().isEmpty()) {
            errorMsg = "Full name is required.";
        }

        if (errorMsg == null) {
            boolean success = userService.registerNewUser(email, password, fullName, phoneNumber, address);
            if (success) {
                req.setAttribute("success", "Registration successful! Please Login to continue.");
                req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Registration failed due to a system error. Please try again later.");
                req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", errorMsg);
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
        }
    }
}
