package org.example.app.view;

import org.example.app.controllers.UserController;
import org.example.app.models.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NewUserFrame extends JFrame {

    private User newUser;
    private String selectedImagePath = null;

    public NewUserFrame(int screenWidth, int screenHeight, UserController userController) {
        setTitle("Novo Usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

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

        // imagem
        JLabel imageLabel = new JLabel("Avatar:");
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton selectImageButton = new JButton("Selecionar Imagem");
        selectImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath = selectedFile.getAbsolutePath();
                JOptionPane.showMessageDialog(this, "Imagem selecionada: " + selectedFile.getName());
            }
        });

        // botão de criar usuário
        JButton createButton = new JButton("Criar");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || password.isEmpty() || selectedImagePath == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos e selecione uma imagem.");
                return;
            }

            // constrói o User

            if (userController.userExists(name)) {
                JOptionPane.showMessageDialog(this, "O nome de usuário \""+name+"\" já está em uso");
                return;
            }

            if (!userController.createUser(name, password, selectedImagePath)) {
                JOptionPane.showMessageDialog(this, "Houve algum erro durante a criação do usuário. Tente novamente.");
                return;
            }

            // aqui você poderia salvar o user em um banco, arquivo, etc
            JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
            dispose();
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
        mainPanel.add(imageLabel);
        mainPanel.add(selectImageButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createButton);

        add(mainPanel);
        setVisible(true);
    }
}
