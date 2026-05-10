package com.arthaflow.controller;

import com.arthaflow.dao.UserDAO;
import com.arthaflow.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize      = 1024 * 1024 * 5,
        maxRequestSize   = 1024 * 1024 * 10)
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/jsp/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");

        if ("updateInfo".equals(action)) {
            String fullName = req.getParameter("fullName");
            String address  = req.getParameter("address");
            String phone    = req.getParameter("phoneNumber");

            user.setFullName(fullName);
            user.setAddress(address);
            user.setPhoneNumber(phone);

            if (userDAO.updateUser(user)) {
                session.setAttribute("user", userDAO.getUserById(user.getUserId()));
                resp.sendRedirect(req.getContextPath() + "/user/profile?success=Profile+updated+successfully.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=Failed+to+update+profile.");
            }

        } else if ("uploadPicture".equals(action)) {
            Part pic = req.getPart("profilePicture");
            if (pic != null && pic.getSize() > 0) {
                String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "profile";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String ext = "";
                String submitted = pic.getSubmittedFileName();
                if (submitted != null && submitted.contains(".")) {
                    ext = submitted.substring(submitted.lastIndexOf("."));
                }
                String fileName = "user_" + user.getUserId() + "_" + UUID.randomUUID() + ext;
                pic.write(uploadDir + File.separator + fileName);

                String picPath = "uploads/profile/" + fileName;
                if (userDAO.updateProfilePicture(user.getUserId(), picPath)) {
                    user.setProfilePicture(picPath);
                    session.setAttribute("user", userDAO.getUserById(user.getUserId()));
                    resp.sendRedirect(req.getContextPath() + "/user/profile?success=Profile+picture+updated.");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/user/profile?error=Failed+to+save+picture.");
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/user/profile?error=No+file+selected.");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/profile");
        }
    }
}
