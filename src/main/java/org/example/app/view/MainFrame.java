package org.example.app.view;

import org.example.app.models.User;
import org.example.app.models.dao.UserManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private UserManager userManager;
    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable tab;
    private JLabel totalSimLabel;
    private JLabel mediaSimLabel;
    private JLabel userScoreLabel;

    public MainFrame(int screenWidth, int screenHeight, User currUser, UserManager userManager) {
        this.userManager = userManager;
        this.currentUser = currUser;
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

        tab = getJTable();
        JScrollPane scrollPane = new JScrollPane(tab);
        tablePanel.add(scrollPane);

        // textos da simulação
        JPanel simTextPanel = new JPanel();
        simTextPanel.setLayout(new BoxLayout(simTextPanel, BoxLayout.Y_AXIS));
        totalSimLabel = new JLabel("Total de simulações: " + userManager.getTotalSimulations());
        mediaSimLabel = new JLabel("Média de simulações: " + userManager.getAverageSuccessfulSimulations());
        simTextPanel.add(totalSimLabel);
        simTextPanel.add(mediaSimLabel);

        // botões de adicionar e remover usuários
        JPanel tableButtonsPanel = new JPanel();
        tableButtonsPanel.setLayout(new BoxLayout(tableButtonsPanel, BoxLayout.Y_AXIS));
        tableButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // botão adicionar usuario
        JButton addUserButton = new JButton("Adicionar Usuário");
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addUserButton.addActionListener(e -> {
            NewUserFrame newUserFrame = new NewUserFrame(screenWidth, screenHeight, userManager);
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
        });

        // botão remover usuario
        JButton removeUserButton = new JButton("Remover Usuário");
        removeUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeUserButton.addActionListener(e -> {
            String selectedUser = tab.getModel().getValueAt(tab.getSelectedRow(), 0).toString();
            if (selectedUser.equals(currentUser.getUsername())){
                JOptionPane.showMessageDialog(this, "Usuário não pode se remover!");
                return;
            }
            userManager.removeUser(selectedUser);
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

        // panel da área do usuário
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));

        // panel do lado esquerdo
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.LINE_AXIS));

        // panel da imagem
        JPanel userAvatarPanel = new JPanel();
        Border avatarBorder = BorderFactory.createLineBorder(Color.black);
        userAvatarPanel.setBorder(avatarBorder);
        String userAvatar = currentUser.getAvatar();
        JLabel userAvatarLabel = new JLabel();
        switch (userAvatar) {
            case "guardian 1":
                // imagem do avatar aegislash
                ImageIcon img = new ImageIcon("src/main/java/org/example/app/resources/aegislash.png");
                userAvatarLabel = new JLabel(img);
                break;

            case "guardian 2":
                // imagem do regigigas
                ImageIcon img2 = new ImageIcon("src/main/java/org/example/app/resources/regigigas.png");
                userAvatarLabel = new JLabel(img2);
                break;
            case "creature 1":
                // imagem do spoink
                ImageIcon img3 = new ImageIcon("src/main/java/org/example/app/resources/spoink.png");
                userAvatarLabel = new JLabel(img3);
                break;
            case "creature 2":
                // imagem do spoink red
                ImageIcon img4 = new ImageIcon("src/main/java/org/example/app/resources/spoink_red.png");
                userAvatarLabel = new JLabel(img4);
                break;
            case "cluster":
                // imagem do grumpig
                ImageIcon img5 = new ImageIcon("src/main/java/org/example/app/resources/grumpig.png");
                userAvatarLabel = new JLabel(img5);
                break;

        }
        userAvatarPanel.add(userAvatarLabel);

        // panel de textos do usuário (nome e pontuação)
        JPanel userTextPanel = new JPanel();
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));

        JLabel userNameLabel = new JLabel("Usuário: " + currentUser.getUsername());
        userScoreLabel = new JLabel("Pontuação: " + currentUser.getSuccesfulSimulations());

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
            userManager.incrementSimCount(currentUser.getUsername());
            SimulationFrame sim = new SimulationFrame(screenWidth, screenHeight, currUser.getUsername(), userManager, this::refreshPage);
        });

        // botão de log off
        JButton exitButton = new JButton("Sair");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(screenWidth, screenHeight, userManager);
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

    private JTable getJTable() {
        String [] columnNames = {"Usuário", "Quantidade de Simulações", "Simulações Bem-Sucedidas"};
        ArrayList<User> databaseUsers = userManager.getAllUsers();

        Object[][] userData = new Object[databaseUsers.size()][columnNames.length];
        for (int i = 0; i < databaseUsers.size(); i++) {
            User u = databaseUsers.get(i);
            userData[i][0] = u.getUsername();
            userData[i][1] = u.getSimulationCount();
            userData[i][2] = u.getSuccesfulSimulations();

        }
        // cria um modelo que impede as células de serem editadas
        tableModel = new DefaultTableModel(userData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // bloqueia edição
            }
        };

        tab = new JTable(tableModel);
        tab.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tab.setEnabled(true); // permite seleção
        return tab;
    }

    private void refreshPage() {
        System.out.println("Refresh page");

        // * tabela *
        // Limpa o modelo
        tableModel.setRowCount(0);

        // Pega os dados atualizados do banco
        ArrayList<User> databaseUsers = userManager.getAllUsers();

        // Adiciona os dados no modelo
        for (User u : databaseUsers) {
            Object[] row = {
                    u.getUsername(),
                    u.getSimulationCount(),
                    u.getSuccesfulSimulations()
            };
            tableModel.addRow(row);
        }

        // ** labels de pontuação global **
        totalSimLabel.setText("Total de simulações: " + userManager.getTotalSimulations());
        mediaSimLabel.setText("Média de simulações: " + userManager.getAverageSuccessfulSimulations());

        // *** label de pontuação de usuário ***
        userScoreLabel.setText("Pontuação " + userManager.getUserSuccessfulSimulations(currentUser.getUsername()));
    }
}