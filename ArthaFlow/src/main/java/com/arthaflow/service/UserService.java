package com.arthaflow.service;

import com.arthaflow.dao.UserDAO;
import com.arthaflow.model.User;
import com.arthaflow.util.PasswordEncryption;
import com.arthaflow.util.ValidationService;

public class UserService {
    UserDAO userdao = new UserDAO();

    public boolean registerNewUser(String email, String password, String fullName, String phoneNumber, String address) {
        boolean emailValidation = ValidationService.isValidEmail(email) &&
                !ValidationService.isEmailExists(email);
        boolean phoneNumberValidation = ValidationService.isValidphoneNumber(phoneNumber);
        boolean passwordValidation = ValidationService.isValidPassword(password);
        boolean nameValidation = fullName != null && !fullName.trim().isEmpty();

        if (!emailValidation || !passwordValidation || !nameValidation || !phoneNumberValidation) {
            return false;
        }

        String hashedPassword = PasswordEncryption.hashPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setRole("USER");

        return userdao.registerUser(user);
    }

    public User authenticateUser(String email, String password) {
        User user = userdao.getUserByEmail(email);
        if (user != null && PasswordEncryption.verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean updateUserProfile(int userId, String fullName, String email) {
        if (!ValidationService.isValidEmail(email)) {
            return false;
        }

        // Get current user to check if email belongs to someone else
        User currentUser = userdao.getUserById(userId);
        if (currentUser == null) {
            return false;
        }

        // Only block if email exists AND it belongs to a different user
        if (!email.equals(currentUser.getEmail()) && ValidationService.isEmailExists(email)) {
            return false;
        }

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole("USER");

        return userdao.updateUser(user);
    }
}
