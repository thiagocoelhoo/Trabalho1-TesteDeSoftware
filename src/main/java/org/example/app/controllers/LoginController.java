package org.example.app.controllers;

import org.example.app.models.User;
import org.example.app.services.UserService;
import org.example.app.view.LoginFrame;
import org.example.app.view.MainFrame;
import org.example.app.view.NewUserFrame;

import javax.swing.*;

public class LoginController {
    private final UserService userService;
    private final int windowWidth;
    private final int windowHeight;

    public LoginController(UserService userService, int windowWidth, int windowHeight) {
        this.userService = userService;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void handleLogin(String username, String password, LoginFrame loginFrame) {
        boolean auth = userService.authenticate(username, password);
        if (auth) {
            User usuario = userService.getUser(username);
            loginFrame.dispose();
            MainController mainController = new MainController(userService, usuario);
            MainFrame main = new MainFrame(windowWidth, windowHeight, mainController);
            main.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Usuário ou senha incorretos!");
        }
    }

    public void handleNewUser() {
        new NewUserFrame(windowWidth, windowHeight, userService);
    }
}
