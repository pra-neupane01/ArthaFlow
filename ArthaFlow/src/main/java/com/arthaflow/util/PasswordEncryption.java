package com.arthaflow.util;
import com.arthaflow.dao.UserDAO;
import com.arthaflow.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {
    private static User user = new User();

    public static String hashPassword(String password){
        String salt = BCrypt.gensalt(12);
        String hashPassword =BCrypt.hashpw(password, salt);
        return hashPassword;
            }

    public static boolean verifyPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

}
