// AuthenticationFilter.java
package com.arthaflow.filter;

import com.arthaflow.util.CookieUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebFilter(urlPatterns = {"/user/*", "/admin/*"})
public class AuthenticationFilter implements Filter {



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            String ctx = httpRequest.getContextPath();
            if (CookieUtil.hasAuthMarker(httpRequest)) {
                CookieUtil.clearAuthMarker(httpResponse, ctx);
                httpResponse.sendRedirect(ctx + "/login?timeout=1");
            } else {
                httpResponse.sendRedirect(ctx + "/login");
            }
        }
    }


}