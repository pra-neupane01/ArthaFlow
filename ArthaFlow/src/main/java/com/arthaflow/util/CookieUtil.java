package com.arthaflow.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void setUserCookie(HttpServletResponse resp, String email){
        Cookie cookie = new Cookie("email",email);
        resp.addCookie(cookie);
    }
}
