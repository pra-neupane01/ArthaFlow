package com.arthaflow.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionService {

    /** Signed-in users: longer inactivity window (minutes). */
    public static final int AUTHENTICATED_MAX_INACTIVE_SECONDS = 30 * 60;

    public static void setUserSession(HttpServletRequest req, Object user) {
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(AUTHENTICATED_MAX_INACTIVE_SECONDS);
    }

    public static void endSession(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate(); // ends the session
        }
    }

}
