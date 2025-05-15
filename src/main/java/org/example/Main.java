package org.example;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create window
        JFrame window = new JFrame("Window");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and add the panel to the window
        Game game = new Game();
        window.add(game);
        window.addKeyListener(game);

        // Show the window
        window.pack();
        window.setVisible(true);
        window.setSize(600, 600);
    }
}