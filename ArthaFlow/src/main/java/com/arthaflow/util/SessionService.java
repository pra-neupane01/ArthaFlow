package com.arthaflow.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionService {

    public static void setUserSession(HttpServletRequest req, Object user){
        HttpSession session = req.getSession();
        session.setAttribute("user",user);

    }

    public static void endSession(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate(); // ends the session
        }
    }

}
