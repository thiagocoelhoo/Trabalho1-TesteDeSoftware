package org.example.app.view;

import org.example.app.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class NewUserFrame extends JFrame {
    private UserService userService;

    public NewUserFrame(int screenWidth, int screenHeight, UserService userService) {
        this.userService = userService;
        this.init();
    }

    public void init() {
        setTitle("Novo Usuário");
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

        // nome
        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // senha
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // avatar
        JLabel avatarLabel = new JLabel("Avatar:");
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] avatarOptions = {"guardian 1", "guardian 2", "creature 1", "creature 2", "cluster"};
        JComboBox avatarComboBox = new JComboBox(avatarOptions);
        avatarComboBox.setMaximumSize(new Dimension(200, 30));
        avatarComboBox.setSelectedItem("guardian 1");

        // botão de criar usuário
        JButton createButton = new JButton("Criar");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos e selecione uma imagem.");
                return;
            }

            boolean created = userService.createUser(name, password, avatarComboBox.getSelectedItem().toString());

            if (created) {
                JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro de inclusão: o nome de usuário '" + name + "' já existe.");
                nameField.setText("");
            }

        });

        createButton.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createButton.doClick();
                }
            }
        });

        // adiciona tudo ao painel
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
