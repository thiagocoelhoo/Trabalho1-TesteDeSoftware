package org.example.app.view;

import org.example.app.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(int screenWidth, int screenHeight, User currentUser) {

        // configura janela
        setTitle("Jumping Creatures!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(screenWidth, screenHeight / 2));
        tablePanel.setPreferredSize(new Dimension(screenWidth, screenHeight / 2));

        JTable tab = getJTable();
        JScrollPane scrollPane = new JScrollPane(tab);
        tablePanel.add(scrollPane);

        // textos da simulação
        //TODO receber dados do BD
        JPanel simTextPanel = new JPanel();
        simTextPanel.setLayout(new BoxLayout(simTextPanel, BoxLayout.Y_AXIS));
        JLabel totalSimLabel = new JLabel("Total de simulações: " + 1000000);
        JLabel mediaSimLabel = new JLabel("Média de simulações: " + 53.6);
        simTextPanel.add(totalSimLabel);
        simTextPanel.add(mediaSimLabel);

        // botões de inserir e remover usuários
        JPanel tableButtonsPanel = new JPanel();
        tableButtonsPanel.setLayout(new BoxLayout(tableButtonsPanel, BoxLayout.Y_AXIS));
        tableButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addUserButton = new JButton("Adicionar Usuário");
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO alterar conforme BD
        addUserButton.addActionListener(e -> {
            new NewUserFrame(screenWidth, screenHeight);
        });

        JButton removeUserButton = new JButton("Remover Usuário");
        removeUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO alterar conforme BD
        removeUserButton.addActionListener(e -> {});

        tableButtonsPanel.add(addUserButton);
        tableButtonsPanel.add(Box.createVerticalStrut(5));
        tableButtonsPanel.add(removeUserButton);

        JPanel simOptionsPanel = new JPanel();
        simOptionsPanel.setLayout(new BoxLayout(simOptionsPanel, BoxLayout.X_AXIS));
        simOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        simOptionsPanel.add(simTextPanel);
        simOptionsPanel.add(Box.createHorizontalGlue());
        simOptionsPanel.add(tableButtonsPanel);

        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);

        // panel da área do usuário
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));

        // panel do lado esquerdo
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.LINE_AXIS));

        // panel da imagem
        JPanel userAvatarPanel = new JPanel();
        //TODO carregar imagem com BD

        // panel de textos do usuário (nome e pontuação)
        JPanel userTextPanel = new JPanel();
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));

        JLabel userNameLabel = new JLabel("Usuário: " + currentUser.getUsername());
        JLabel userScoreLabel = new JLabel("Pontuação: " + 40000);

        userTextPanel.add(userNameLabel);
        userTextPanel.add(userScoreLabel);

        userInfoPanel.add(userAvatarPanel);
        userInfoPanel.add(userTextPanel);

        // panel do lado direito
        JPanel userOptionsPanel = new JPanel();
        userOptionsPanel.setLayout(new BoxLayout(userOptionsPanel, BoxLayout.Y_AXIS));
        userOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // botão de simulação
        JButton simulateButton = new JButton("Nova Simulação");
        simulateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        simulateButton.addActionListener(e -> {
            new SimulationFrame(screenWidth, screenHeight);
        });

        // botão de log off
        JButton exitButton = new JButton("Sair");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(screenWidth, screenHeight);
            loginFrame.setVisible(true);
            dispose();
        });

        userOptionsPanel.add(simulateButton);
        userOptionsPanel.add(Box.createVerticalStrut(5));
        userOptionsPanel.add(exitButton);

        userPanel.add(userInfoPanel);
        userPanel.add(Box.createHorizontalGlue());
        userPanel.add(userOptionsPanel);

        mainPanel.add(tablePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(simOptionsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(horizontalSeparator);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(userPanel);

        add(mainPanel);
    }

    //TODO mover para Controller quando funcional
    private static JTable getJTable() {
        String [] columnNames = {"Usuário", "Quantidade de Simulações", "Simulações Bem-Sucedidas"};
        Object[][] data = {
                {"alice", "12", "9"},
                {"bob", "20", "15"},
                {"carol", "8", "6"},
                {"daniel", "17", "14"},
                {"erika", "5", "4"},
                {"fernando", "10", "7"},
                {"gabriela", "14", "12"},
                {"heitor", "9", "5"},
                {"isabela", "13", "11"},
                {"joao", "7", "6"},
                {"heitor", "9", "5"},
                {"isabela", "13", "11"},
                {"joao", "7", "6"},
                {"heitor", "9", "5"},
                {"isabela", "13", "11"},
                {"joao", "7", "6"}
        };

        // cria um modelo que impede as células de serem editadas
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // bloqueia edição
            }
        };

        JTable tab = new JTable(model);
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tab.setEnabled(true); // permite seleção
        return tab;
    }
}