package org.example.app.view;

import org.example.app.controllers.NewUserController;
import org.example.app.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewUserFrame extends JFrame {
    private final NewUserController controller;

    public NewUserFrame(int screenWidth, int screenHeight, UserService userService) {
        this.controller = new NewUserController(userService, this);
        init();
    }

    public void init() {
        setTitle("Novo Usuário");
        setName("newUserFrame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel newUserLabel = new JLabel("Adicionar Usuário");
        newUserLabel.setFont(new Font("Arial", Font.BOLD, 25));
        newUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nome
        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameField = new JTextField();
        nameField.setName("nameField");
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Senha
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setName("passwordField");
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar
        JLabel avatarLabel = new JLabel("Avatar:");
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] avatarOptions = {"guardian 1", "guardian 2", "creature 1", "creature 2", "cluster"};
        JComboBox<String> avatarComboBox = new JComboBox<>(avatarOptions);
        avatarComboBox.setName("avatarComboBox");
        avatarComboBox.setMaximumSize(new Dimension(200, 30));
        avatarComboBox.setSelectedItem("guardian 1");

        // Botão Criar
        JButton createButton = new JButton("Criar");
        createButton.setName("createButton");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String avatar = (String) avatarComboBox.getSelectedItem();
            controller.handleCreateUser(name, password, avatar);
        });

        createButton.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createButton.doClick();
                }
            }
        });

        // Adiciona ao painel
        mainPanel.add(newUserLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(avatarLabel);
        mainPanel.add(avatarComboBox);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createButton);

        add(mainPanel);
        setVisible(true);
    }
}