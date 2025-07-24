package org.example.app.view;

import org.example.app.controllers.SimulationController;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private final SimulationController controller;

    public SimulationFrame(SimulationController controller) {
        this.controller = controller;
        showQuantityFrame();
    }

    private void showQuantityFrame() {
        setTitle("Quantity Simulation");
        setName("QuantitySimulation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel quantidadeLabel = new JLabel("Quantas criaturas?");
        quantidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSpinner quantidadeSpinner = createSpinner();
        quantidadeSpinner.setName("quantidadeSpinner");

        JButton startSimButton = new JButton("Start");
        startSimButton.setName("startSimButton");
        startSimButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSimButton.addActionListener(e -> {
            int qt = (int) quantidadeSpinner.getValue();
            dispose();
            controller.startSimulation(qt);
        });

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(quantidadeLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(quantidadeSpinner);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(startSimButton);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        setVisible(true);
    }

    private JSpinner createSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spinner.setMaximumSize(new Dimension(100, 30));
        spinner.setPreferredSize(new Dimension(100, 30));
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        return spinner;
    }
}