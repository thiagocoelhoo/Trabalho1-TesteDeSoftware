package org.example.app.view;

import org.example.app.models.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField userInputField;
    private final JPasswordField passwordInputField;

    public LoginFrame(int screenWidth, int screenHeight) {
        // configura a janela
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);  // centraliza
        setResizable(false);

        // painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel bemVindoLabel = new JLabel("Bem-vindo(a)!");
        bemVindoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bemVindoLabel.setFont(new Font("Arial", Font.BOLD, 40));
        mainPanel.add(bemVindoLabel);

        mainPanel.add(Box.createVerticalStrut(25));

        // user panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Usuário: ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userInputField = createJTextField();

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(userInputField);
        mainPanel.add(userPanel);

        // password panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Senha:   ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordInputField = createJPasswordField();

        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createHorizontalStrut(10));
        passwordPanel.add(passwordInputField);
        mainPanel.add(passwordPanel);

        // botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            User usuario = new User(userInputField.getText(), passwordInputField.getPassword(), null);
            dispose();
            MainFrame main = new MainFrame(screenWidth, screenHeight, usuario);
            main.setVisible(true);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(5));

        JLabel loginLabel = new JLabel("Novo usuário?");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // TODO adicionar actionListener para novo usuário se quiser
        buttonPanel.add(loginLabel);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    private JTextField createJTextField() {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(280, 30));
        textField.setPreferredSize(new Dimension(280, 30));
        return textField;
    }

    private JPasswordField createJPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(280, 30));
        passwordField.setPreferredSize(new Dimension(280, 30));
        return passwordField;
    }
}