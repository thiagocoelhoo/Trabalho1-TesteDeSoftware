package org.example;

import java.util.Random;

public class Jumper {
    private final int FLOOR_HEIGHT = 300;

    private double x;
    private double y;

    private double velX;
    private double velY;

    private int coins;

    public Jumper(double x, double y) {
        this.x = x;
        this.y = y;
        this.coins = 1_000_000;
    }

    public void jump() {
        Random r = new Random();
        velX = (r.nextInt(0, 2) * 2.0f - 1) * (coins / 1_000_000) * 5;
        velY = -2.0f;
    }

    public boolean isJumping() {
        return y < FLOOR_HEIGHT;
    }

    public void update() {
        x += velX;
        y += velY;

        // Calcular colisão com horizonte
        if (y >= FLOOR_HEIGHT) {
            y = FLOOR_HEIGHT;
            velY = 0;
            velX = 0;
        } else {
            velY += 0.098f;
        }

        // Calcular colisão com bordas da tela
        if (x + 20 >= 600) {
            x = 600 - 20;
            velX = -velX;
        } else if (x <= 0) {
            x = 0;
            velX = -velX;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void steal(Jumper other) {
        this.coins += (int)Math.ceil(other.getCoins() / 2.0f);
        other.setCoins((int)Math.floor(other.getCoins() / 2.0f));
    }
}
