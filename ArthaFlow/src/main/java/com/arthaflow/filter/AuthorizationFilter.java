package com.arthaflow.filter;

import com.arthaflow.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterconfig) throws ServletException{

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String contextPath = req.getContextPath();

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if(user == null){
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        if(!"ADMIN".equals(user.getRole())){
            resp.sendRedirect(contextPath + "/user/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
}
