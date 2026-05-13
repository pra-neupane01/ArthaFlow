package com.arthaflow.controller;

import com.arthaflow.model.Account;
import com.arthaflow.model.CreditCard;
import com.arthaflow.model.KycDetails;
import com.arthaflow.model.User;
import com.arthaflow.service.AccountService;
import com.arthaflow.service.CreditCardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreditCardServlet extends HttpServlet {
    private final CreditCardService cardService = new CreditCardService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        CreditCard card = cardService.getCardDetails(user.getUserId());
        req.setAttribute("creditCard", card);
        req.setAttribute("bankAccount", accountService.getAccountDetails(user.getUserId()));
        if (card != null) {
            req.setAttribute("cardKyc", cardService.getKycForCard(card.getCardId()));
            req.setAttribute("cardTransactions", cardService.getCardTransactionHistory(user.getUserId()));
        }
        req.getRequestDispatcher("/jsp/user/creditCard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard");
            return;
        }

        switch (action) {
            case "apply" -> handleApply(req, resp, user);
            case "setPin" -> handleSetPin(req, resp, user);
            case "purchase" -> handlePurchase(req, resp, user);
            default -> resp.sendRedirect(req.getContextPath() + "/user/creditCard");
        }
    }

    private void handleApply(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Account acc = accountService.getAccountDetails(user.getUserId());
        if (acc == null || !"ACTIVE".equals(acc.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error="
                    + URLEncoder.encode("You need an active bank account (approved KYC) before applying for a credit card. Complete Account & KYC first.", StandardCharsets.UTF_8));
            return;
        }
        KycDetails accountKyc = accountService.getAccountOpeningKyc(user.getUserId());
        if (!accountKycComplete(accountKyc)) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error="
                    + URLEncoder.encode("Your account KYC record is missing required identity details. Please contact support.", StandardCharsets.UTF_8));
            return;
        }

        String cardType = req.getParameter("cardType");
        String occupation = req.getParameter("occupation");
        String annualStr = req.getParameter("annualIncome");
        String employmentDetails = req.getParameter("employmentDetails");
        String cardPreferences = req.getParameter("cardPreferences");
        String creditInformation = req.getParameter("creditInformation");
        boolean termsOk = "on".equalsIgnoreCase(req.getParameter("termsAccepted"))
                || "true".equalsIgnoreCase(req.getParameter("termsAccepted"));

        if (!termsOk) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error="
                    + URLEncoder.encode("You must accept the terms and conditions to apply.", StandardCharsets.UTF_8));
            return;
        }

        if (anyBlank(cardType, occupation, annualStr, employmentDetails, cardPreferences, creditInformation)) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error="
                    + URLEncoder.encode("Please complete all required sections of the application form.", StandardCharsets.UTF_8));
            return;
        }

        double annualIncome;
        try {
            annualIncome = Double.parseDouble(annualStr.trim());
            if (annualIncome < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error="
                    + URLEncoder.encode("Invalid annual income amount.", StandardCharsets.UTF_8));
            return;
        }

        String mailing = accountKyc.getMailingAddress() != null && !accountKyc.getMailingAddress().isBlank()
                ? accountKyc.getMailingAddress().trim() : accountKyc.getPermanentAddress().trim();

        KycDetails kyc = new KycDetails();
        kyc.setCitizenshipNumber(accountKyc.getCitizenshipNumber().trim());
        kyc.setDateOfBirth(accountKyc.getDateOfBirth());
        kyc.setFamilyDetails(accountKyc.getFamilyDetails().trim());
        kyc.setMinimumIncome(annualIncome);
        kyc.setPersonalInformation("Identity and address on file from bank account KYC.");
        kyc.setOccupation(occupation.trim());
        kyc.setFatherName(accountKyc.getFatherName().trim());
        kyc.setMotherName(accountKyc.getMotherName().trim());
        kyc.setAnnualIncome(annualIncome);
        kyc.setPermanentAddress(accountKyc.getPermanentAddress().trim());
        kyc.setMailingAddress(mailing);
        kyc.setIncomeDetails("");
        kyc.setEmploymentDetails(employmentDetails.trim());
        kyc.setCardPreferences(cardPreferences.trim());
        kyc.setCreditInformation(creditInformation.trim());
        kyc.setTermsAccepted(true);

        if (cardService.applyForCardWithKyc(user.getUserId(), cardType, kyc)) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?success=Application+and+KYC+submitted.");
        } else {
            String msg = "You already have a pending application or an active card. Wait for admin review, or re-apply only after a previous application was rejected.";
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
        }
    }

    private static boolean accountKycComplete(KycDetails k) {
        if (k == null || !"APPROVED".equals(k.getStatus())) return false;
        if (k.getCitizenshipNumber() == null || k.getCitizenshipNumber().isBlank()) return false;
        if (k.getDateOfBirth() == null) return false;
        if (k.getFamilyDetails() == null || k.getFamilyDetails().isBlank()) return false;
        if (k.getFatherName() == null || k.getFatherName().isBlank()) return false;
        if (k.getMotherName() == null || k.getMotherName().isBlank()) return false;
        if (k.getPermanentAddress() == null || k.getPermanentAddress().isBlank()) return false;
        return true;
    }

    private static boolean anyBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }

    private void handleSetPin(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String pin = req.getParameter("pin");
        String confirm = req.getParameter("pinConfirm");
        int cardId = parseId(req.getParameter("cardId"));
        if (cardId < 0) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=Invalid+card.");
            return;
        }
        String err = cardService.setPin(user.getUserId(), cardId, pin, confirm);
        if (err == null) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?success=PIN+set+successfully.");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=" + URLEncoder.encode(err, StandardCharsets.UTF_8));
        }
    }

    private void handlePurchase(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String pin = req.getParameter("pin");
        double amount = parseAmount(req.getParameter("amount"));
        String remarks = req.getParameter("remarks");
        if (amount <= 0) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=Invalid+amount.");
            return;
        }
        String err = cardService.purchaseWithCard(user.getUserId(), pin, amount, remarks);
        if (err == null) {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?success=Purchase+recorded.");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/creditCard?error=" + URLEncoder.encode(err, StandardCharsets.UTF_8));
        }
    }

    private static int parseId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private static double parseAmount(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return -1;
        }
    }
}
