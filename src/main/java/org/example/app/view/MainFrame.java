package org.example.app.view;

import org.example.app.controllers.MainController;
import org.example.app.controllers.LoginController;
import org.example.app.controllers.SimulationController;
import org.example.app.models.User;
import org.example.app.services.UserService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class MainFrame extends JFrame {
    private final MainController controller;
    private DefaultTableModel tableModel;
    private JTable tab;
    private JLabel totalSimLabel;
    private JLabel mediaSimLabel;
    private JLabel userScoreLabel;
    private final int screenWidth;
    private final int screenHeight;

    public MainFrame(int screenWidth, int screenHeight, MainController controller) {
        this.controller = controller;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        init();
    }

    public void init() {
        setTitle("Jumping Creatures!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(screenWidth, screenHeight / 2));
        tablePanel.setPreferredSize(new Dimension(screenWidth, screenHeight / 2));

        tab = getJTable();
        tab.setName("usersTable");
        JScrollPane scrollPane = new JScrollPane(tab);
        scrollPane.setName("usersTableScrollPane");
        tablePanel.add(scrollPane);

        JPanel simTextPanel = new JPanel();
        simTextPanel.setLayout(new BoxLayout(simTextPanel, BoxLayout.Y_AXIS));
        totalSimLabel = new JLabel("Total de simulações: " + controller.getTotalSimulations());
        totalSimLabel.setName("totalSimulationsLabel");
        mediaSimLabel = new JLabel("Média de simulações ganhas: " + controller.getAverageWins());
        mediaSimLabel.setName("averageWinsLabel");
        simTextPanel.add(totalSimLabel);
        simTextPanel.add(mediaSimLabel);

        JPanel tableButtonsPanel = new JPanel();
        tableButtonsPanel.setLayout(new BoxLayout(tableButtonsPanel, BoxLayout.Y_AXIS));
        tableButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addUserButton = new JButton("Adicionar Usuário");
        addUserButton.setName("addUserButton");
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addUserButton.addActionListener(e -> {
            NewUserFrame newUserFrame = new NewUserFrame(screenWidth, screenHeight, controller.getUserService());
            newUserFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    refreshPage();
                }

                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    refreshPage();
                }
            });
            newUserFrame.setVisible(true);
        });

        JButton removeUserButton = new JButton("Remover Usuário");
        removeUserButton.setName("removeUserButton");
        removeUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeUserButton.addActionListener(e -> {
            int selectedRow = tab.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.");
                return;
            }
            String selectedUser = tab.getModel().getValueAt(selectedRow, 0).toString();

            if (!controller.canRemoveUser(selectedUser)) {
                JOptionPane.showMessageDialog(this, "Usuário não pode se remover!");
                return;
            }

            controller.removeUser(selectedUser);
            refreshPage();
        });

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
        horizontalSeparator.setName("mainSeparator");

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.LINE_AXIS));

        JPanel userAvatarPanel = new JPanel();
        userAvatarPanel.setName("userAvatarPanel");
        Border avatarBorder = BorderFactory.createLineBorder(Color.black);
        userAvatarPanel.setBorder(avatarBorder);

        User currentUser = controller.getCurrentUser();
        JLabel userAvatarLabel = new JLabel();
        userAvatarLabel.setName("userAvatarLabel");
        String avatarPath = controller.getAvatarPath(currentUser.getAvatar());
        if (avatarPath != null) {
            ImageIcon icon = new ImageIcon(avatarPath); // TODO resolver bug visual que não carrega avatar
            userAvatarLabel.setIcon(icon);
        }
        userAvatarPanel.add(userAvatarLabel);

        JPanel userTextPanel = new JPanel();
        userTextPanel.setName("userTextPanel");
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));

        JLabel userNameLabel = new JLabel("Usuário: " + currentUser.getUsername());
        userNameLabel.setName("userNameLabel");
        userScoreLabel = new JLabel("Pontuação: " + currentUser.getSuccesfulSimulations());
        userScoreLabel.setName("userScoreLabel");

        userTextPanel.add(userNameLabel);
        userTextPanel.add(userScoreLabel);

        userInfoPanel.add(userAvatarPanel);
        userInfoPanel.add(userTextPanel);

        JPanel userOptionsPanel = new JPanel();
        userOptionsPanel.setName("userOptionsPanel");
        userOptionsPanel.setLayout(new BoxLayout(userOptionsPanel, BoxLayout.Y_AXIS));
        userOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton simulateButton = new JButton("Nova Simulação");
        simulateButton.setName("simulateButton");
        simulateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        simulateButton.addActionListener(e -> {
            controller.startSimulation();
            SimulationController simController = new SimulationController(controller.getUserService(), controller.getCurrentUser().getUsername(), this::refreshPage);
            SimulationFrame simFrame = new SimulationFrame(simController);
            simFrame.setVisible(true);
        });

        JButton exitButton = new JButton("Sair");
        exitButton.setName("exitButton");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            LoginController loginController = new LoginController(controller.getUserService(), screenWidth, screenHeight );
            LoginFrame loginFrame = new LoginFrame(600, 400, loginController);
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
        setVisible(true);
    }

    private JTable getJTable() {
        String[] columnNames = {"Usuário", "Quantidade de Simulações", "Simulações Bem-Sucedidas"};
        Object[][] userData = controller.getUsersTableData();

        tableModel = new DefaultTableModel(userData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tab = new JTable(tableModel);
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tab;
    }

    public void refreshPage() {
        tableModel.setRowCount(0);
        Object[][] userData = controller.getUsersTableData();
        for (Object[] row : userData) {
            tableModel.addRow(row);
        }

        totalSimLabel.setText("Total de simulações: " + controller.getTotalSimulations());
        mediaSimLabel.setText("Média de simulações ganhas: " + controller.getAverageWins());

        User updated = controller.getUpdatedCurrentUser();
        userScoreLabel.setText("Pontuação: " + updated.getSuccesfulSimulations());
    }
}