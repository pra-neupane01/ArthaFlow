package com.arthaflow.controller;

import com.arthaflow.model.PendingRegistration;
import com.arthaflow.service.EmailService;
import com.arthaflow.service.UserService;
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

public class EmailVerificationServlet extends HttpServlet {
    private static final String PENDING_REGISTRATION = "pendingRegistration";
    private static final int MAX_ATTEMPTS = 5;
    private static final SecureRandom OTP_RANDOM = new SecureRandom();

    private final UserService userService = new UserService();
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PendingRegistration pendingRegistration = getPendingRegistration(req);
        if (pendingRegistration == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        req.setAttribute("email", pendingRegistration.getEmail());
        req.getRequestDispatcher("/jsp/user/verifyEmail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PendingRegistration pendingRegistration = getPendingRegistration(req);
        if (pendingRegistration == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String action = trim(req.getParameter("action"));
        if ("resend".equals(action)) {
            resendCode(req, resp, pendingRegistration);
            return;
        }

        if (pendingRegistration.isExpired()) {
            clearPendingRegistration(req);
            req.setAttribute("error", "Verification code expired. Please register again.");
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
            return;
        }

        String otp = trim(req.getParameter("otp"));
        if (otp == null || !otp.matches("\\d{6}")) {
            showVerificationForm(req, resp, pendingRegistration, "Enter the 6-digit code from your email.", null);
            return;
        }

        if (!PasswordEncryption.verifyPassword(otp, pendingRegistration.getOtpHash())) {
            pendingRegistration.incrementAttempts();
            if (pendingRegistration.getAttempts() >= MAX_ATTEMPTS) {
                clearPendingRegistration(req);
                req.setAttribute("error", "Too many incorrect verification attempts. Please register again.");
                req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
                return;
            }

            int attemptsLeft = MAX_ATTEMPTS - pendingRegistration.getAttempts();
            req.getSession().setAttribute(PENDING_REGISTRATION, pendingRegistration);
            showVerificationForm(req, resp, pendingRegistration,
                    "Incorrect verification code. Attempts left: " + attemptsLeft + ".", null);
            return;
        }

        if (ValidationService.isEmailExists(pendingRegistration.getEmail())) {
            clearPendingRegistration(req);
            req.setAttribute("error", "Email already registered.");
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
            return;
        }

        boolean registered = userService.registerVerifiedUser(pendingRegistration);
        if (registered) {
            clearPendingRegistration(req);
            req.getSession().setAttribute("registrationSuccess", true);
            resp.sendRedirect(req.getContextPath() + "/register");
        } else {
            showVerificationForm(req, resp, pendingRegistration,
                    "Registration failed due to a system error. Please try again later.", null);
        }
    }

    private void resendCode(HttpServletRequest req, HttpServletResponse resp, PendingRegistration pendingRegistration)
            throws ServletException, IOException {
        String otp = generateOtp();
        pendingRegistration.setOtpHash(PasswordEncryption.hashPassword(otp));
        pendingRegistration.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
        pendingRegistration.resetAttempts();

        try {
            emailService.sendRegistrationOtp(pendingRegistration.getEmail(), pendingRegistration.getFullName(), otp);
            req.getSession().setAttribute(PENDING_REGISTRATION, pendingRegistration);
            showVerificationForm(req, resp, pendingRegistration, null, "A new verification code was sent.");
        } catch (MessagingException e) {
            System.out.println("Error resending registration OTP: " + e.getMessage());
            showVerificationForm(req, resp, pendingRegistration,
                    "Could not resend the verification email. Please try again later.", null);
        }
    }

    private void showVerificationForm(HttpServletRequest req, HttpServletResponse resp,
                                      PendingRegistration pendingRegistration, String error, String success)
            throws ServletException, IOException {
        req.setAttribute("email", pendingRegistration.getEmail());
        if (error != null) {
            req.setAttribute("error", error);
        }
        if (success != null) {
            req.setAttribute("success", success);
        }
        req.getRequestDispatcher("/jsp/user/verifyEmail.jsp").forward(req, resp);
    }

    private PendingRegistration getPendingRegistration(HttpServletRequest req) {
        Object pendingRegistration = req.getSession().getAttribute(PENDING_REGISTRATION);
        if (pendingRegistration instanceof PendingRegistration) {
            return (PendingRegistration) pendingRegistration;
        }
        return null;
    }

    private void clearPendingRegistration(HttpServletRequest req) {
        req.getSession().removeAttribute(PENDING_REGISTRATION);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String generateOtp() {
        return String.valueOf(100000 + OTP_RANDOM.nextInt(900000));
    }
}
