package org.example;

import org.example.Creatures.Jumper;

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

/**
* Classe destinada à parte gráfica da aplicação
*/
public class GamePanel extends JPanel implements ActionListener {
    private int frameInterval = 10; // Interval in milliseconds
    private float simulationSpeed = 10.0f;
    private final Timer timer;
    private long lastTick = 0;

    private final int MAX_ITERATIONS = 10_000;
    private static int iteration = 0;

    private final Game game;

    private BufferedImage backgroundImage;
    private BufferedImage spoinkSprite;
    private BufferedImage spoinkRedSprite;
    private BufferedImage clusterSprite;
    private BufferedImage guardianSprite;


    public GamePanel(Game game) {
        // Setup panel
        setPreferredSize(new Dimension(800, 450));
        setBackground(Color.BLACK);

        // Load resources
        try {
            backgroundImage = ImageIO.read(new File("background.png"));
            spoinkSprite = ImageIO.read(new File("spoink.png"));
            spoinkRedSprite = ImageIO.read(new File("spoink_red.png"));
            guardianSprite = ImageIO.read(new File("guardian.png"));
            clusterSprite = ImageIO.read(new File("cluster.png"));
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

        //TODO ajustar para novas condições de parada
        if (iteration >= MAX_ITERATIONS || game.getJumpers().size() < 3){
            frameInterval = 0;
            simulationSpeed = 0;
            Jumper bestJumper = new Jumper(0);
            for (Jumper j : game.getJumpers().toList()){
                if (j.getCoins() > bestJumper.getCoins()) {
                    bestJumper = j;
                }
            }

            timer.stop();
            System.out.println("Simulation ended! The best jumper has " + bestJumper.getCoins() + " coins.");
            return;
        }

        iteration++;
        System.out.println("Iteration: " + iteration);
        repaint();

        lastTick = currentTick;
    }

    public void drawJumper(Graphics g, Jumper jumper) {
        // Select sprite based on the amount of coins and class
        BufferedImage sprite = spoinkSprite;
        BufferedImage guardian = guardianSprite;
        BufferedImage cluster = clusterSprite;

        if (jumper.getCoins() < 100) {
            sprite = spoinkRedSprite;
        }

        if (jumper.isGuardian){
            sprite = guardianSprite;
        }
        if (jumper.isCluster){
            sprite = clusterSprite;
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
