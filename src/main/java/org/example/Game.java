package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener, KeyListener {
    private final int DELAY = 8;

    private final int quantidade = 50;
    private ArrayList<Jumper> jumpers;

    private Timer timer;
    private int currentJumperIndex = 0;
    private double minX = 0;
    private double maxX = 600;

    public Game() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        jumpers = new ArrayList<Jumper>();

        for (int i = 0; i < quantidade; i++) {
            jumpers.add(new Jumper(600.0f / quantidade * i , 50));
        }

        timer = new Timer(DELAY, this);
        timer.setActionCommand("update");
        timer.start();
    }

    private Integer findNearestJumper(int jumperIndex) {
        Jumper currentJumper = jumpers.get(jumperIndex);

        Integer nearestJumperIndex = null;
        Double minDistance = null;

        for (int i = 0; i < jumpers.size(); i++) {
            if (i == jumperIndex) {
                continue;
            }

            Jumper jumper = jumpers.get(i);
            double distance = Math.abs(jumper.getX() - currentJumper.getX());

            if (minDistance == null || distance < minDistance) {
                minDistance = distance;
                nearestJumperIndex = i;
            }
        }

        return nearestJumperIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        // System.out.println(action);

        if (action != null && action.equals("start")) {
            if (timer.isRunning()) {
                timer.stop();
            } else {
                timer.start();
            }
        }

        Jumper currentJumper = jumpers.get(currentJumperIndex);

        if (!currentJumper.isJumping()) {
            Integer nearestJumperIndex = findNearestJumper(currentJumperIndex);
            Jumper nearestJumper = jumpers.get(nearestJumperIndex);
            currentJumper.steal(nearestJumper);

            if (nearestJumper.getCoins() == 0) {
                jumpers.remove(nearestJumper);
                System.out.println("Dead: " + nearestJumperIndex + " Coins: " + nearestJumper.getCoins());
            }

            currentJumperIndex++;
            currentJumperIndex %= jumpers.size();
            jumpers.get(currentJumperIndex).jump();
        }

        for (Jumper jumper : jumpers) {
            jumper.update();
        }

        // Calcular área renderizada
        minX = 0;
        maxX = 0;

        for (Jumper jumper : jumpers) {
            double x = jumper.getX();
            if (x < minX) {
                minX = x;
            }

            if (x > maxX) {
                maxX = x;
            }
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the horizon
        g.setColor(Color.WHITE);
        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);

        // Draw jumper

        for (Jumper jumper : jumpers) {
            int intensity = (int)Math.min(Math.log(jumper.getCoins()) / Math.log(1_000_000.0f) * 255, 255);
            intensity = Math.max(intensity, 0);

            Color color = new Color(255 - intensity, intensity, 0);
            g.setColor(color);

            double x = jumper.getX();
            x = (x - minX) / (maxX - minX) * 600;

            g.drawOval((int)x - 10, (int)jumper.getY() - 20, 20, 20);
        }

        // Sync screen
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Jumper jumper : jumpers) {
            jumper.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
