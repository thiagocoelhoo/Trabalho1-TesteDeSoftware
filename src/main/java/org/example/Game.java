package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.LinkedList;

public class Game extends JPanel implements ActionListener, KeyListener {
    private final int DELAY = 0;
    private final int MAX_ITERATIONS = 100_000;

    private int currentIteration = 0;
    private final int quantidade = 100;
    private LinkedList<Jumper> jumpers;
    private int currentJumperIndex;

    private Timer timer;

    public Game() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        jumpers = new LinkedList<Jumper>();

        for (int i = 0; i < quantidade; i++) {
            jumpers.add(new Jumper(600.0f / quantidade * i , 300));
        }
        currentJumperIndex = 0;

        timer = new Timer(DELAY, this);
        timer.setActionCommand("update");
        timer.start();
    }

    private Jumper findNearestJumper(Jumper jumper) {
        Double minDistance = null;
        Jumper nearestJumper = null;

        for (Jumper j : jumpers) {
            if (j != jumper) {
                double distance = Math.abs(j.getX() - jumper.getX());

                if (minDistance == null || distance < minDistance) {
                    minDistance = distance;
                    nearestJumper = j;
                }
            }
        }

        return nearestJumper;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentIteration >= MAX_ITERATIONS) {
            timer.stop();
            return;
        }

        Jumper currentJumper = jumpers.get(currentJumperIndex);
        currentJumper.jump();

        Jumper nearestJumper = findNearestJumper(currentJumper);
        int nearestJumperIndex = jumpers.indexOf(nearestJumper);

        currentJumper.steal(nearestJumper);

        if (nearestJumper.getCoins() == 0) {
            jumpers.remove(nearestJumper);
        }

        if (nearestJumper.getCoins() > 0 || nearestJumperIndex > currentJumperIndex) {
            currentJumperIndex++;
        }

        currentJumperIndex %= jumpers.size();

        System.out.println("Iteration: " + currentIteration);
        currentIteration++;

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the horizon
        g.setColor(Color.WHITE);
        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);

        // Calcular área renderizada
        double minX = 0;
        double maxX = 0;

        for (Jumper jumper : jumpers) {
            double x = jumper.getX();
            if (x < minX) {
                minX = x;
            }

            if (x > maxX) {
                maxX = x;
            }
        }

        // Draw jumper
        for (Jumper jumper : jumpers) {
            int intensity = (int)Math.min(Math.log(jumper.getCoins()) / Math.log(1_000_000.0f) * 255, 255);
            intensity = Math.max(intensity, 0);

            Color color = new Color(255 - intensity, intensity, 0);
            g.setColor(color);

            double x = jumper.getX();
            x = (x - minX) / (maxX - minX) * 550 + 25;

            g.drawOval((int)x - 10, (int)jumper.getY() - 20, 20, 20);
        }

        // Sync screen
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
