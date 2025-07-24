package org.example.app.controllers;

import org.example.app.models.User;
import org.example.app.services.UserService;
import org.example.app.view.GameView;

import javax.swing.*;

public class SimulationController {
    private final UserService userService;
    private final String currentUser;
    private final Runnable onCloseCallback;

    public SimulationController(UserService userService, String currentUser, Runnable onCloseCallback) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.onCloseCallback = onCloseCallback;
    }

    public void startSimulation(int qtCreatures) {
        User user = userService.getUser(currentUser);
        GameController game = new GameController();
        game.createJumpers(qtCreatures);

        GameView gamePanel = new GameView(game, user, userService);
        gamePanel.start();

        JFrame window = new JFrame("Jumping Creatures");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.add(gamePanel);
        window.pack();
        window.setSize(800, 600); // ajuste aqui
        window.setLocationRelativeTo(null);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseCallback != null) onCloseCallback.run();
            }
        });

        gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent e) {
                JOptionPane.showMessageDialog(window, "Simulação bem-sucedida!");
                userService.incrementWins(currentUser);
                window.dispose();
            }
        });

        window.setVisible(true);
    }
}