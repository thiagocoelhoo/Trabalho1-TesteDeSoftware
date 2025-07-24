package org.example.app.controllers;

import org.example.app.services.UserService;

import javax.swing.*;

public class NewUserController {
    private final UserService userService;
    private final JFrame frame;

    public NewUserController(UserService userService, JFrame frame) {
        this.userService = userService;
        this.frame = frame;
    }

    public void handleCreateUser(String name, String password, String avatar) {
        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Preencha todos os campos e selecione uma imagem.");
            return;
        }

        boolean created = userService.createUser(name, password, avatar);

        if (created) {
            JOptionPane.showMessageDialog(frame, "Usuário criado com sucesso!");
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Erro de inclusão: o nome de usuário '" + name + "' já existe.");
        }
    }
}