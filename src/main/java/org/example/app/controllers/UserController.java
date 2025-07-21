package org.example.app.controllers;

import org.example.app.models.UserDAO;
import org.example.app.models.User;

import java.util.List;


public class UserController {
    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean createUser(String username, String password, String avatarPath) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                avatarPath == null || avatarPath.isBlank()) {
            return false; // campos inválidos
        }

        User user = new User(username, password, avatarPath);
        userDAO.insert(user);
        return true;
    }

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean userExists(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}
