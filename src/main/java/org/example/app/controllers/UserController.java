package org.example.app.controllers;

import org.example.app.models.UserDAO;
import org.example.app.models.User;


public class UserController {
    private static final UserDAO userDAO = new UserDAO();

    static public boolean createUser(String username, String password, String avatarPath) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                avatarPath == null || avatarPath.isBlank()) {
            return false; // campos inválidos
        }

        User user = new User(username, password, avatarPath);
        userDAO.insert(user);
        return true;
    }

    static public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    static public boolean userExists(String username) {
        return userDAO.findByUsername(username) != null;
    }
}
