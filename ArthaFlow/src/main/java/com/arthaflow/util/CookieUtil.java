package com.arthaflow.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /** Set when user logs in; cleared on logout. Used to show "session timed out" after idle expiry. */
    public static final String AUTH_MARKER_COOKIE = "af_authenticated";

    public static void setUserCookie(HttpServletResponse resp, String email) {
        Cookie cookie = new Cookie("email", email);
        resp.addCookie(cookie);
    }

    public static void setAuthMarker(HttpServletResponse resp, String contextPath) {
        Cookie c = new Cookie(AUTH_MARKER_COOKIE, "1");
        c.setPath(cookiePath(contextPath));
        c.setHttpOnly(true);
        c.setMaxAge(-1);
        resp.addCookie(c);
    }

    public static void clearAuthMarker(HttpServletResponse resp, String contextPath) {
        Cookie c = new Cookie(AUTH_MARKER_COOKIE, "");
        c.setPath(cookiePath(contextPath));
        c.setHttpOnly(true);
        c.setMaxAge(0);
        resp.addCookie(c);
    }

    public static boolean hasAuthMarker(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (AUTH_MARKER_COOKIE.equals(cookie.getName()) && "1".equals(cookie.getValue())) {
                return true;
            }
        }
        return false;
    }

    private static String cookiePath(String contextPath) {
        return (contextPath == null || contextPath.isEmpty()) ? "/" : contextPath;
    }
}
