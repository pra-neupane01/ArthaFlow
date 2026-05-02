package com.arthaflow.controller;

import com.arthaflow.model.User;
import com.arthaflow.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
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

        boolean success = userService.registerNewUser(email, password,fullName, phoneNumber, address);

        if(success){
            req.setAttribute("Success Message","Registration successful! Please Login to continue service.");
            req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);

        }else {
            req.setAttribute("Failure Message","Invalid Email or Password.");
            req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
        }
    }
}
