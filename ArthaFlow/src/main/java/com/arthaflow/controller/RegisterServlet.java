package com.arthaflow.controller;

import com.arthaflow.service.UserService;
import com.arthaflow.util.ValidationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

public class RegisterServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object registrationSuccess = req.getSession().getAttribute("registrationSuccess");
        if (registrationSuccess != null) {
            req.getSession().removeAttribute("registrationSuccess");
            req.setAttribute("registrationSuccess", true);
        }
        req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = trim(req.getParameter("fullName"));
        String email = normalizeEmail(req.getParameter("email"));
        String phoneNumber = trim(req.getParameter("phoneNumber"));
        if (phoneNumber != null && phoneNumber.length() == 10 && (phoneNumber.startsWith("98") || phoneNumber.startsWith("97"))) {
            phoneNumber = "+977" + phoneNumber;
        }
        String password = req.getParameter("password");
        String address = trim(req.getParameter("address"));

        String errorMsg = null;
        if (!ValidationService.isValidEmail(email)) {
            errorMsg = "Enter a real email address with a mail-receiving domain.";
        } else if (ValidationService.isEmailExists(email)) {
            errorMsg = "Email already registered.";
        } else if (!ValidationService.isValidphoneNumber(phoneNumber)) {
            errorMsg = "Invalid phone number. Use +977 or 10 digits.";
        } else if (!ValidationService.isValidPassword(password)) {
            errorMsg = "Password must be 8+ chars, with Uppercase, Lowercase, Number, and Special Char.";
        } else if (fullName == null || fullName.trim().isEmpty()) {
            errorMsg = "Full name is required.";
        }

        if (errorMsg == null) {
            boolean success = userService.registerNewUser(email, password, fullName, phoneNumber, address);
            if (success) {
                req.getSession().setAttribute("registrationSuccess", true);
                resp.sendRedirect(req.getContextPath() + "/register");
            } else {
                req.setAttribute("error", "Registration failed due to a system error. Please try again later.");
                req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", errorMsg);
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
