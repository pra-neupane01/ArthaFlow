package com.arthaflow.controller;

import com.arthaflow.model.PendingRegistration;
import com.arthaflow.service.EmailService;
import com.arthaflow.util.PasswordEncryption;
import com.arthaflow.util.ValidationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class RegisterServlet extends HttpServlet {
    private static final String PENDING_REGISTRATION = "pendingRegistration";
    private static final SecureRandom OTP_RANDOM = new SecureRandom();

    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("1".equals(req.getParameter("restart"))) {
            req.getSession().removeAttribute(PENDING_REGISTRATION);
        }

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
            String otp = generateOtp();
            PendingRegistration pendingRegistration = new PendingRegistration(
                    email,
                    PasswordEncryption.hashPassword(password),
                    fullName,
                    phoneNumber,
                    address,
                    PasswordEncryption.hashPassword(otp),
                    Instant.now().plus(10, ChronoUnit.MINUTES)
            );

            try {
                emailService.sendRegistrationOtp(email, fullName, otp);
                req.getSession().setAttribute(PENDING_REGISTRATION, pendingRegistration);
                resp.sendRedirect(req.getContextPath() + "/verify-email");
            } catch (MessagingException e) {
                System.out.println("Error sending registration OTP: " + e.getMessage());
                req.setAttribute("error", "Could not send the verification email. Please check the address or try again later.");
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

    private String generateOtp() {
        return String.valueOf(100000 + OTP_RANDOM.nextInt(900000));
    }
}
