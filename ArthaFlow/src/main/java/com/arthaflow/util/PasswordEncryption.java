package com.arthaflow.util;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {

    public static String hashPassword(String password){
        String salt = BCrypt.gensalt(12);
        String hashPassword =BCrypt.hashpw(password, salt);
        return hashPassword;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null || !hashedPassword.startsWith("$2")) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
