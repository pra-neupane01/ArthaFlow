package com.arthaflow.controller;

import com.arthaflow.model.User;
import com.arthaflow.service.UserService;
import com.arthaflow.util.CookieUtil;
import com.arthaflow.util.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("login")
public class LoginServlet extends HttpServlet {
    UserService userservice = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userservice.authenticateUser(email, password);
        if(user != null){
            SessionService.setUserSession(req, user);
            CookieUtil.setUserCookie(resp, email);

            if("ADMIN".equals(user.getRole())){
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            }else {
                resp.sendRedirect(req.getContextPath() + "/user/dashboard");
            }
        } else {
            req.setAttribute("Error Message","Invalid Email or Password");
            req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
        }

    }
}
