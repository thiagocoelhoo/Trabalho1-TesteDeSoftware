package org.example.app.view;

import org.example.app.models.User;

import javax.swing.*;
import java.awt.*;

public class NewUserFrame {
    public NewUserFrame(int screenWidth, int screenHeight){
        User newUser;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel newUserLabel = new JLabel("Adicionar Usuário");
        newUserLabel.setFont(new Font("Arial", Font.PLAIN, 25));

    }
}
