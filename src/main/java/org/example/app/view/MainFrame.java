package org.example.app.view;

import org.example.app.models.User;
import org.example.app.services.UserService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private UserService userService;
    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable tab;
    private JLabel totalSimLabel;
    private JLabel mediaSimLabel;
    private JLabel userScoreLabel;
    private int screenWidth;
    private int screenHeight;

    public MainFrame(int screenWidth, int screenHeight, User currUser, UserService userService) {
        this.userService = userService;
        this.currentUser = currUser;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        init();
    }

    public void init() {
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
        tab.setName("usersTable"); // <--- Nome para a tabela
        JScrollPane scrollPane = new JScrollPane(tab);
        scrollPane.setName("usersTableScrollPane"); // <--- Nome para o scroll pane
        tablePanel.add(scrollPane);

        // textos da simulação
        JPanel simTextPanel = new JPanel();
        simTextPanel.setLayout(new BoxLayout(simTextPanel, BoxLayout.Y_AXIS));
        totalSimLabel = new JLabel("Total de simulações: " + userService.getTotalSimulations());
        totalSimLabel.setName("totalSimulationsLabel"); // <--- Nome para o label
        mediaSimLabel = new JLabel("Média de simulações ganhas: " + userService.getAverageWins());
        mediaSimLabel.setName("averageWinsLabel"); // <--- Nome para o label
        simTextPanel.add(totalSimLabel);
        simTextPanel.add(mediaSimLabel);

        // botões de adicionar e remover usuários
        JPanel tableButtonsPanel = new JPanel();
        tableButtonsPanel.setLayout(new BoxLayout(tableButtonsPanel, BoxLayout.Y_AXIS));
        tableButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // botão adicionar usuario
        JButton addUserButton = new JButton("Adicionar Usuário");
        addUserButton.setName("addUserButton"); // <--- Nome para o botão
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addUserButton.addActionListener(e -> {
            NewUserFrame newUserFrame = new NewUserFrame(screenWidth, screenHeight, userService);
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
            newUserFrame.setVisible(true); // Garante que a nova janela seja visível
        });

        // botão remover usuario
        JButton removeUserButton = new JButton("Remover Usuário");
        removeUserButton.setName("removeUserButton"); // <--- Nome para o botão
        removeUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeUserButton.addActionListener(e -> {
            int selectedRow = tab.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.");
                return;
            }
            String selectedUser = tab.getModel().getValueAt(selectedRow, 0).toString();

            if (selectedUser.equals(currentUser.getUsername())){
                JOptionPane.showMessageDialog(this, "Usuário não pode se remover!");
                return;
            }
            userService.removeUser(selectedUser);
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
        horizontalSeparator.setName("mainSeparator"); // <--- Nome para o separador

        // panel da área do usuário
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));

        // panel do lado esquerdo
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.LINE_AXIS));

        // panel da imagem
        JPanel userAvatarPanel = new JPanel();
        userAvatarPanel.setName("userAvatarPanel"); // <--- Nome para o painel do avatar
        Border avatarBorder = BorderFactory.createLineBorder(Color.black);
        userAvatarPanel.setBorder(avatarBorder);
        String userAvatar = currentUser.getAvatar();
        JLabel userAvatarLabel = new JLabel();
        userAvatarLabel.setName("userAvatarLabel"); // <--- Nome para o label da imagem do avatar
        switch (userAvatar) {
            case "guardian 1":
                ImageIcon img = new ImageIcon(getClass().getResource("/org/example/app/resources/aegislash.png"));
                userAvatarLabel.setIcon(img);
                break;

            case "guardian 2":
                ImageIcon img2 = new ImageIcon(getClass().getResource("/org/example/app/resources/regigigas.png"));
                userAvatarLabel.setIcon(img2);
                break;
            case "creature 1":
                ImageIcon img3 = new ImageIcon(getClass().getResource("/org/example/app/resources/spoink.png"));
                userAvatarLabel.setIcon(img3);
                break;
            case "creature 2":
                ImageIcon img4 = new ImageIcon(getClass().getResource("/org/example/app/resources/spoink_red.png"));
                userAvatarLabel.setIcon(img4);
                break;
            case "cluster":
                ImageIcon img5 = new ImageIcon(getClass().getResource("/org/example/app/resources/grumpig.png"));
                userAvatarLabel.setIcon(img5);
                break;
        }
        userAvatarPanel.add(userAvatarLabel);

        // panel de textos do usuário (nome e pontuação)
        JPanel userTextPanel = new JPanel();
        userTextPanel.setName("userTextPanel"); // <--- Nome para o painel de texto do usuário
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));

        JLabel userNameLabel = new JLabel("Usuário: " + currentUser.getUsername());
        userNameLabel.setName("userNameLabel"); // <--- Nome para o label do nome do usuário
        userScoreLabel = new JLabel("Pontuação: " + currentUser.getSuccesfulSimulations());
        userScoreLabel.setName("userScoreLabel"); // <--- Nome para o label da pontuação do usuário

        userTextPanel.add(userNameLabel);
        userTextPanel.add(userScoreLabel);

        userInfoPanel.add(userAvatarPanel);
        userInfoPanel.add(userTextPanel);

        // panel do lado direito
        JPanel userOptionsPanel = new JPanel();
        userOptionsPanel.setName("userOptionsPanel"); // <--- Nome para o painel de opções do usuário
        userOptionsPanel.setLayout(new BoxLayout(userOptionsPanel, BoxLayout.Y_AXIS));
        userOptionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // botão de simulação
        JButton simulateButton = new JButton("Nova Simulação");
        simulateButton.setName("simulateButton");
        simulateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        simulateButton.addActionListener(e -> {
            userService.incrementTotalGames(currentUser.getUsername());
            SimulationFrame sim = new SimulationFrame(screenWidth, screenHeight, currentUser.getUsername(), userService, this::refreshPage);
//            sim.setVisible(true); // Garante que a nova janela seja visível
        });

        // botão de log off
        JButton exitButton = new JButton("Sair");
        exitButton.setName("exitButton"); // <--- Nome para o botão
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(screenWidth, screenHeight, userService);
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
        setVisible(true); // Garante que a MainFrame seja visível ao ser criada
    }

    private JTable getJTable() {
        String [] columnNames = {"Usuário", "Quantidade de Simulações", "Simulações Bem-Sucedidas"};
        List<User> users = userService.getAllUsers();

        Object[][] userData = new Object[users.size()][columnNames.length];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            userData[i][0] = u.getUsername();
            userData[i][1] = u.getSimulationCount();
            userData[i][2] = u.getSuccesfulSimulations();

        }
        // cria um modelo que impede as células de serem editadas
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

    public void refreshPage() { // Public para ser acessível de outras janelas
        System.out.println("Refresh page");

        // * tabela *
        // Limpa o modelo
        tableModel.setRowCount(0);

        // Pega os dados atualizados do banco
        List<User> databaseUsers = userService.getAllUsers();

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
        totalSimLabel.setText("Total de simulações: " + userService.getTotalSimulations());
        mediaSimLabel.setText("Média de simulações ganhas: " + userService.getAverageWins()); // Corrigido para getAverageWins

        // *** label de pontuação de usuário ***
        // Atualiza o currentUser para pegar os dados mais recentes
        currentUser = userService.getUser(currentUser.getUsername());
        userScoreLabel.setText("Pontuação: " + currentUser.getSuccesfulSimulations());
    }
}