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
        JFrame startFrame = createStartFrame(screenWidth, screenHeight);
        startFrame.setVisible(true);
    }

    private static JFrame createStartFrame(int screenWidth, int screenHeight) {
        // create jumper quantity popup
        JFrame qtWindow = getJFrame(screenWidth, screenHeight);
        qtWindow.isFocusable();

        // panel para agrupar os elementos menores
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // força os itens para o meio da tela (empurra pra baixo)
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
            createMainFrame(screenWidth, screenHeight,qt);
        });

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(startSimButton);

        // força os itens para o meio da tela (empurra pra cima)
        mainPanel.add(Box.createVerticalGlue());

        qtWindow.add(mainPanel);

        return qtWindow;
    }

    private static void createMainFrame(int screenWidth, int screenHeight, int qtCreatures) {
        // Create main window
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

    // metodo para abstrair a criação da janela de start
    private static JFrame getJFrame(int screenWidth, int screenHeight) {
        JFrame qtWindow = new JFrame("Bem vindo(a)!");
        qtWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // window size and position
        int qtWidth = 300;
        int qtHeight = 200;
        qtWindow.setSize(qtWidth, qtHeight);

        int windowX = (screenWidth / 2) - (qtWidth / 2);
        int windowY = (screenHeight / 2) - (qtHeight / 2);
        qtWindow.setLocation(windowX, windowY);
        return qtWindow;
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