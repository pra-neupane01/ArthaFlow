package com.arthaflow.controller;

import com.arthaflow.model.Account;
import com.arthaflow.model.User;
import com.arthaflow.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class AccountServlet extends HttpServlet {
    AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        Account account = accountService.getAccountDetails(user.getUserId());
        req.setAttribute("account", account);
        req.getRequestDispatcher("/jsp/user/accountDetails.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        String accountType = req.getParameter("accountType");

        // Handle KYC Document Uploads
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdir();

        String idDocPath = "";
        String addrProofPath = "";

        try {
            Part idPart = req.getPart("idDocument");
            Part addrPart = req.getPart("addressProof");

            if (idPart != null && idPart.getSize() > 0) {
                String fileName = UUID.randomUUID().toString() + "_"
                        + Paths.get(idPart.getSubmittedFileName()).getFileName().toString();
                idPart.write(uploadPath + File.separator + fileName);
                idDocPath = "uploads/" + fileName;
            }

            if (addrPart != null && addrPart.getSize() > 0) {
                String fileName = UUID.randomUUID().toString() + "_"
                        + Paths.get(addrPart.getSubmittedFileName()).getFileName().toString();
                addrPart.write(uploadPath + File.separator + fileName);
                addrProofPath = "uploads/" + fileName;
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error uploading files: " + e.getMessage());
            doGet(req, resp);
            return;
        }

        if (accountType == null || accountType.isEmpty()) {
            req.setAttribute("error", "Please select an account type.");
            doGet(req, resp);
            return;
        }

        Account existing = accountService.getAccountDetails(user.getUserId());
        if (existing != null) {
            req.setAttribute("error", "You have already applied for an account.");
            doGet(req, resp);
            return;
        }

        // Create Account with KYC documents
        Account newAccount = new Account();
        newAccount.setUserId(user.getUserId());
        newAccount.setAccountType(accountType);
        newAccount.setBalance(0.0);
        newAccount.setIdDocumentPath(idDocPath);
        newAccount.setAddressProofPath(addrProofPath);

        boolean created = accountService.createNewAccount(newAccount);
        if (created) {
            resp.sendRedirect(req.getContextPath()
                    + "/user/account?success=KYC+documents+submitted.+Waiting+for+admin+to+verify+and+issue+account.");
        } else {
            req.setAttribute("error", "Failed to submit application.");
            doGet(req, resp);
        }
    }
}
