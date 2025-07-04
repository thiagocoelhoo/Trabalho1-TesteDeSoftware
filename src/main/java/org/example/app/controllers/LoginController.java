package org.example.app.controllers;

import org.example.app.models.User;
import org.example.app.models.UserDAO;

public class LoginController {
    private static final UserDAO userDAO = new UserDAO();

    public static User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
