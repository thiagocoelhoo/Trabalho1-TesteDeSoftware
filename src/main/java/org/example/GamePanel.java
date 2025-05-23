package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
* Classe destinada à parte gráfica da aplicação
*/
public class GamePanel extends JPanel implements ActionListener {
    private final int frameInterval = 16; // Interval in milliseconds
    private float simulationSpeed = 10.0f;
    private Timer timer;
    private long lastTick = 0;

    private Game game;

    private BufferedImage backgroundImage;
    private BufferedImage spoinkSprite;
    private BufferedImage spoinkRedSprite;

    public GamePanel(Game game) {
        // Setup panel
        setPreferredSize(new Dimension(800, 450));
        setBackground(Color.BLACK);

        // Load resources
        try {
            backgroundImage = ImageIO.read(new File("background.png"));
            spoinkSprite = ImageIO.read(new File("spoink.png"));
            spoinkRedSprite = ImageIO.read(new File("spoink_red.png"));
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
        repaint();

        lastTick = currentTick;
    }

    public void drawJumper(Graphics g, Jumper jumper) {
        // Select sprite based on the amount of coins
        BufferedImage sprite = spoinkSprite;
        if (jumper.getCoins() < 100) {
            sprite = spoinkRedSprite;
        }

        // Calculate sprite position on screen
        double w = sprite.getWidth() * (getWidth() / getPreferredSize().getWidth());
        double h = sprite.getHeight() * (getHeight() / getPreferredSize().getHeight());
        double x = (jumper.getX() - Game.BORDER_LEFT) / (Game.BORDER_RIGHT - Game.BORDER_LEFT) * getWidth() - (w / 2.0);
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
