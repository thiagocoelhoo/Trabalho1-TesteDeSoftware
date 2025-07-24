package org.example.app.view;

import org.example.app.models.User;
import org.example.app.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LoginFrame extends JFrame {
    private JTextField userInputField;
    private JPasswordField passwordInputField;

    private UserService userService;
    private int windowWidth;
    private int windowHeight;

    public LoginFrame(int windowWidth, int windowHeight, UserService userService) {
        this.userService = userService;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        init();
    }

    public void init() {
        // configura a janela
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.windowWidth, this.windowHeight);
        setLocationRelativeTo(null);  // centraliza
        setResizable(true);


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
        userInputField.setName("usernameField");

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
        passwordInputField.setName("passwordField");

        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createHorizontalStrut(10));
        passwordPanel.add(passwordInputField);
        mainPanel.add(passwordPanel);

        // botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Entrar");
        loginButton.setName("Entrar");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String username = userInputField.getText();
            String password = new String(passwordInputField.getPassword());
            boolean auth = userService.authenticate(username, password);

            if (auth) {
                dispose();
                User usuario = userService.getUser(username);
                MainFrame main = new MainFrame(windowWidth, windowHeight, usuario, userService);
                main.setVisible(true);
            }
            else {
                userInputField.setText("");
                passwordInputField.setText("");
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!");
            }
        });

        loginButton.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Enter");
                    loginButton.doClick();
                }
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(5));

        JLabel loginLabel = new JLabel("Novo usuário?");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new NewUserFrame(windowWidth, windowHeight, userService);
            }

            public void mouseEntered(MouseEvent e) {
                loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

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