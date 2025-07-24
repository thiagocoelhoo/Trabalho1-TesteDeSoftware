package org.example.app.view;

import org.example.app.controllers.GameController;
import org.example.app.models.Jumper;
import org.example.app.models.JumperType;
import org.example.app.models.User;
import org.example.app.models.UserDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


/**
* Classe destinada à parte gráfica da simulação
*/
public class GameView extends JPanel implements ActionListener {
    private int frameInterval = 1; // Interval in milliseconds
    private float simulationSpeed = 10.0f;
    private Timer timer;
    private long lastTick = 0;

    private final GameController game;

    private UserDAO userDAO = new UserDAO();

    private BufferedImage backgroundImage;
    private BufferedImage aegislashSprite;
    private BufferedImage grumpigSprite;
    private BufferedImage spoinkSprite;
    private BufferedImage spoinkRedSprite;

    public GameView(GameController game, User user, JFrame frame) {
        userDAO.update(user);

        // Setup panel
        setPreferredSize(new Dimension(800, 450));
        setBackground(Color.BLACK);

        // Load resources
        try {
            backgroundImage = ImageIO.read(new File("src/main/java/org/example/app/resources/background.png"));
            aegislashSprite = ImageIO.read(new File("src/main/java/org/example/app/resources/aegislash.png"));
            grumpigSprite = ImageIO.read(new File("src/main/java/org/example/app/resources/grumpig.png"));
            spoinkSprite = ImageIO.read(new File("src/main/java/org/example/app/resources/spoink.png"));
            spoinkRedSprite = ImageIO.read(new File("src/main/java/org/example/app/resources/spoink_red.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Create game
        this.game = game;

        // Create timer
        timer = new Timer(frameInterval, this);
        timer.setActionCommand("update");
    }

    public void start() {
        timer.start();
        lastTick = System.nanoTime();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTick = System.nanoTime();
        double deltaTime = (currentTick - lastTick) / 1e9;

        game.update(deltaTime * simulationSpeed);
        if (game.isSimulationFinished()){
            this.setVisible(false);
        }
        else{
            repaint();
            lastTick = currentTick;
        }
    }

    public boolean checkWin() {
        List<Jumper> jumpers =  game.getJumpers().toList();

        if (jumpers.size() > 2) {
            return false;
        }

        for (Jumper jumper : jumpers) {
            if (jumper.type == JumperType.CLUSTER) {
                return false;
            }
        }

        return true;
    }

    public void drawJumper(Graphics g, Jumper jumper) {
        // Select sprite based on the amount of coins
        BufferedImage sprite = spoinkSprite;

        if (jumper.type == JumperType.CRIATURA) {
            if (jumper.getCoins() < 100) {
                sprite = spoinkRedSprite;
            }
        } else if (jumper.type == JumperType.CLUSTER) {
            sprite = grumpigSprite;
        } else if (jumper.type == JumperType.GUARDIAO) {
            sprite = aegislashSprite;
        }

        // Calculate sprite position on screen
        double w = sprite.getWidth() * (getWidth() / getPreferredSize().getWidth());
        double h = sprite.getHeight() * (getHeight() / getPreferredSize().getHeight());
        double x = (jumper.getX() - GameController.BORDER_LEFT) / (GameController.BORDER_RIGHT - GameController.BORDER_LEFT) * getWidth() - (w / 2.0);
        double y = jumper.getY() - h + 20 + getHeight()*0.7;

        // Draw image on screen
        final Color imageBackgroundColor = new Color(0, 0, 0, 0);
        g.drawImage(sprite, (int)x, (int)y, (int)w, (int)h, imageBackgroundColor, null);
    }

    public void drawScenario(Graphics g) {
        // Draw the horizon
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw horizon
        drawScenario(g);

        // Draw jumper
        for (Jumper jumper: game.getJumpers().toList()) {
            drawJumper(g, jumper);
        }

        // Sync screen
        Toolkit.getDefaultToolkit().sync();
    }
}
