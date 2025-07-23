package org.example.app.view;

import org.example.app.controllers.GameController;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;

//TODO retornar pontuações

public class SimulationFrame {

    private final int screenWidth;
    private final int screenHeight;

    public SimulationFrame(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        showQuantityFrame();
    }

    private void showQuantityFrame() {
        JFrame qtWindow = createJFrame(screenWidth, screenHeight, 300, 200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(Box.createVerticalGlue());

        JLabel quantidadeLabel = new JLabel("Quantas criaturas?");
        quantidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(quantidadeLabel);

        mainPanel.add(Box.createVerticalStrut(10));

        JSpinner quantidadeSpinner = getSpinner();
        mainPanel.add(quantidadeSpinner);

        JButton startSimButton = new JButton("Start");
        startSimButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSimButton.addActionListener(e -> {
            int qt = (int) quantidadeSpinner.getValue();
            qtWindow.dispose();
            showSimulationFrame(qt);
        });

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(startSimButton);
        mainPanel.add(Box.createVerticalGlue());

        qtWindow.add(mainPanel);
        qtWindow.setVisible(true);
    }

    //TODO retornar pontuação
    private void showSimulationFrame(int qtCreatures) {
        JFrame window = new JFrame("Jumping Creatures");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GameController game = new GameController();
        game.createJumpers(qtCreatures);

        GameView gamePanel = new GameView(game);
        gamePanel.start();
        window.add(gamePanel);

        window.pack();

        int windowWidth = (int)(screenWidth * 0.6);
        int windowHeight = (int)(screenHeight * 0.6);
        window.setSize(windowWidth, windowHeight);

        int windowX = (screenWidth / 2) - (windowWidth / 2);
        int windowY = (screenHeight / 2) - (windowHeight / 2);
        window.setLocation(windowX, windowY);
        window.setVisible(true);
    }

    private JFrame createJFrame(int screenWidth, int screenHeight, int w, int h) {
        JFrame qtWindow = new JFrame("Jumping Creatures!");
        qtWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        qtWindow.setSize(w, h);

        int windowX = (screenWidth / 2) - (w / 2);
        int windowY = (screenHeight / 2) - (h / 2);
        qtWindow.setLocation(windowX, windowY);
        return qtWindow;
    }

    private JTextField createJTextField(boolean user) {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(200, 30));
        textField.setPreferredSize(new Dimension(200, 30));
        if (!user) {
            textField = new JPasswordField();
            textField.setMaximumSize(new Dimension(200, 30));
            textField.setPreferredSize(new Dimension(200, 30));
        }
        return textField;
    }

    private JSpinner getSpinner() {
        JSpinner quantidadeSpinner = new JSpinner();
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantidadeSpinner, "#");
        quantidadeSpinner.setEditor(editor);

        JFormattedTextField tf = editor.getTextField();
        DefaultFormatterFactory factory = (DefaultFormatterFactory) tf.getFormatterFactory();
        NumberFormatter numberFormatter = (NumberFormatter) factory.getDefaultFormatter();
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);

        quantidadeSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        quantidadeSpinner.setMaximumSize(new Dimension(100, 30));
        quantidadeSpinner.setPreferredSize(new Dimension(100, 30));
        quantidadeSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        return quantidadeSpinner;
    }
}
