package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class GamePanel extends JPanel implements ActionListener {
    private final int frameInterval = 16; // Interval in milliseconds
    private float simulationSpeed = 5.0f;
    private Timer timer;
    private long lastTick = 0;

    private Game game;

    private BufferedImage backgroundImage;
    private BufferedImage spoinkSprite;
    private BufferedImage spoinkRedSprite;

    public GamePanel() {
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
        game = new Game();

        // Create jumpers list
        game.createJumpers(10);

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
        double deltaTime = (currentTick - lastTick) / 1000000000.0;

        game.update(deltaTime * simulationSpeed);
        repaint();

        lastTick = currentTick;
    }

    public void drawJumper(Graphics g, Jumper jumper) {
        int minX = -100_000;
        int maxX = 100_000;

        double x = jumper.getX();
        double y = jumper.getY() - spoinkSprite.getHeight() + 20;

        x = (x - minX) / (maxX - minX) * 550 + 25;
        g.setColor(Color.GREEN);

        BufferedImage sprite;

        if (jumper.getCoins() < 100) {
            sprite = spoinkRedSprite;
        } else {
            sprite = spoinkSprite;
        }

        g.drawImage(
                sprite,
                (int)x,
                (int)y,
                new Color(255, 0, 0, 0),
                null
        );

        // int intensity = (int)Math.min(Math.log(jumper.getCoins()) / Math.log(1_000_000.0f) * 255, 255);
        // intensity = Math.max(intensity, 0);
        // Color color = new Color(255 - intensity, intensity, 0);
        // g.setColor(color);
        // g.drawOval((int)x - 10, (int)jumper.getY() - 20, 20, 20);
    }

    public void drawScenario(Graphics g) {
        // Draw the horizon
        // g.setColor(Color.WHITE);
        // g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        g.drawImage(backgroundImage, 0, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw horizon
        drawScenario(g);

        // Draw jumper
        for (Jumper jumper : game.getJumpers().toList()) {
            drawJumper(g, jumper);
        }

        // Sync screen
        Toolkit.getDefaultToolkit().sync();
    }
}
