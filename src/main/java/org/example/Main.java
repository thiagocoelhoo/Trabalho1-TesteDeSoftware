package org.example;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe responsável pela criação da janela e inicialização da parte gráfica da aplicação
 * (NÃO POSSUI A LÓGICA DA APLICAÇÃO)
 */

public class Main {
    public static void main(String[] args) {
        // Get current device screen dimensions
        final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        // Create initial frame to set simulation quantity

        JFrame loginFrame = createLoginFrame(screenWidth, screenHeight);
        loginFrame.setVisible(true);
    }

    private static JFrame createLoginFrame(int screenWidth, int screenHeight){
        JFrame login = createJFrame(screenWidth, screenHeight, 500, 400);
        login.isFocusable();

        // panel para agrupar os elementos menores
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // força os itens para o meio da tela (empurra para baixo)
        //mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(Box.createVerticalStrut(10));

        // label Bem-vindo
        JLabel bemVindoLabel = new JLabel("Bem-vindo(a)!");
        bemVindoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bemVindoLabel.setFont(new Font("Arial", Font.BOLD, 40));
        mainPanel.add(bemVindoLabel);

        mainPanel.add(Box.createVerticalStrut(40));

        // user panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Usuário: ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField userInputField = createJTextField(true);

        userPanel.add(userLabel);
        userPanel.add(userInputField);

        mainPanel.add(userPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // password panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Senha:   ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField passwordInputField = createJTextField(false);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInputField);

        mainPanel.add(passwordPanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // botão de login
        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO actionListener do login

        buttonPanel.add(loginButton);

        buttonPanel.add(Box.createVerticalStrut(5));

        JLabel loginLabel = new JLabel("Novo usuário?");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO actionListener da label de novo usuário

        buttonPanel.add(loginLabel);



        mainPanel.add(buttonPanel);

        // força os itens para o meio da tela (empurra para cima)
        mainPanel.add(Box.createVerticalGlue());

        login.add(mainPanel);

        return login;
    }

    //TODO receber um usuário do login
    private static JFrame createMainFrame(int screenWidth, int screenHeight){
        JFrame mainFrame = createJFrame(screenWidth, screenHeight, 600, 400);


        return mainFrame;
    }

    private static JFrame createStartFrame(int screenWidth, int screenHeight) {
        // create jumper quantity popup
        JFrame qtWindow = createJFrame(screenWidth, screenHeight, 300, 200);
        qtWindow.isFocusable();

        // panel para agrupar os elementos menores
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // força os itens para o meio da tela (empurra para baixo)
        mainPanel.add(Box.createVerticalGlue());

        // label "quantas criaturas?"
        JLabel quantidadeLabel = new JLabel("Quantas criaturas?");
        quantidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(quantidadeLabel);

        mainPanel.add(Box.createVerticalStrut(10)); // espaço entre elementos

        // number selector
        JSpinner quantidadeSpinner = getSpinner();

        mainPanel.add(quantidadeSpinner);

        // start button
        JButton startSimButton = new JButton("Start");
        startSimButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startSimButton.addActionListener(e -> {
            int qt = (int) quantidadeSpinner.getValue();
            qtWindow.dispose(); // destroi a janela atual
            createSimFrame(screenWidth, screenHeight, qt);
        });

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(startSimButton);

        // força os itens para o meio da tela (empurra para cima)
        mainPanel.add(Box.createVerticalGlue());

        qtWindow.add(mainPanel);

        return qtWindow;
    }

    private static void createSimFrame(int screenWidth, int screenHeight, int qtCreatures) {
        // Create simulation window
        JFrame window = new JFrame("Jumping Creatures");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.isFocusable();

        window.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });

        // Create and add the panel to the window
        Game game = new Game();
        game.createJumpers(qtCreatures);

        GamePanel gamePanel = new GamePanel(game);
        gamePanel.start();
        window.add(gamePanel);

        window.pack();

        int windowWidth = (int)(screenWidth * 0.6);
        int windowHeight = (int)(screenHeight * 0.6);
        window.setSize(windowWidth, windowHeight);

        // center window in screen
        int windowX = (screenWidth / 2) - (windowWidth / 2);
        int windowY = (screenHeight / 2) - (windowHeight / 2);
        window.setLocation(windowX, windowY);
        window.setVisible(true);
    }

    // método para abstrair a criação da janela de start
    private static JFrame createJFrame(int screenWidth, int screenHeight, int w, int h) {
        JFrame qtWindow = new JFrame("Jumping Creatures!");
        qtWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // window size and position
        qtWindow.setSize(w, h);

        int windowX = (screenWidth / 2) - (w / 2);
        int windowY = (screenHeight / 2) - (h / 2);
        qtWindow.setLocation(windowX, windowY);
        return qtWindow;
    }

    private static JTextField createJTextField(boolean user){
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(200, 30));
        textField.setPreferredSize(new Dimension(200, 30));

        if (!user){
            textField = new JPasswordField();
            textField.setMaximumSize(new Dimension(200, 30));
            textField.setPreferredSize(new Dimension(200, 30));
        }
        return textField;
    }


    private static JSpinner getSpinner() {
        JSpinner quantidadeSpinner = new JSpinner();
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantidadeSpinner, "#");
        quantidadeSpinner.setEditor(editor);

        // formatar campo para impedir letras ou caracteres inválidos
        JFormattedTextField tf = editor.getTextField();
        DefaultFormatterFactory factory = (DefaultFormatterFactory) tf.getFormatterFactory();
        NumberFormatter numberFormatter = (NumberFormatter) factory.getDefaultFormatter();
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);

        // definições de tamanho e valores abrangidos
        quantidadeSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        quantidadeSpinner.setMaximumSize(new Dimension(100, 30));
        quantidadeSpinner.setPreferredSize(new Dimension(100, 30));
        quantidadeSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        return quantidadeSpinner;
    }
}