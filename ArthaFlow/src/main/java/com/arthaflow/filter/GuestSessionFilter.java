package com.arthaflow.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * When a visitor hits a public entry page without a session, creates a session with a short
 * inactivity timeout (~90 seconds). Authenticated requests are unchanged; {@link com.arthaflow.util.SessionService#setUserSession}
 * raises the timeout after login.
 */
public class GuestSessionFilter implements Filter {

    /** ~1.5 minutes of inactivity before the session expires (guest / not logged in). */
    public static final int GUEST_MAX_INACTIVE_SECONDS = 90;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!shouldBootstrapGuestSession(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            session = httpRequest.getSession(true);
            session.setMaxInactiveInterval(GUEST_MAX_INACTIVE_SECONDS);
        }
        chain.doFilter(request, response);
    }

    private static boolean shouldBootstrapGuestSession(HttpServletRequest req) {
        String ctx = req.getContextPath();
        String uri = req.getRequestURI();
        String path = (ctx != null && uri.startsWith(ctx)) ? uri.substring(ctx.length()) : uri;
        if (path.isEmpty()) {
            path = "/";
        }
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")
                || path.contains("/WEB-INF/")) {
            return false;
        }
        return "/".equals(path)
                || "/index.jsp".equals(path)
                || "/login".equals(path)
                || "/register".equals(path);
    }
}
