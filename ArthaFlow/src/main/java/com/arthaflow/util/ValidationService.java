package com.arthaflow.util;

import com.arthaflow.dao.UserDAO;

public class ValidationService {
    private static UserDAO userDAO = new UserDAO();

    public static boolean isValidEmail(String email){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email != null && email.matches(emailRegex)){
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password){
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (password != null && password.matches(passwordRegex)){
            return true;
        }
        return false;
    }

    public static boolean isEmailExists(String email){
        if(email == null){
            return false;
        }
        return userDAO.emailExists(email);
    }
}

