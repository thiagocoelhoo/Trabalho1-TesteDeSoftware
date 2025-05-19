package org.example;

import java.util.Random;

public class Jumper {
    private final Random randomGenerator = new Random();
    private final int FLOOR_HEIGHT = 300;

    private double x;
    private double y;

    private int coins;

    public Jumper(double x, double y) {
        this.x = x;
        this.y = y;
        this.coins = 1_000_000;
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

    public double calcJump(double x, double random, int coins) {
        return x + random * coins;
    }

    public void jump() {
        float r = randomGenerator.nextFloat(-1, 1);
        this.x = calcJump(this.x, r, this.coins);
    }

    public void steal(Jumper other) {
        this.coins += (int)Math.ceil(other.getCoins() / 2.0f);
        other.setCoins((int)Math.floor(other.getCoins() / 2.0f));
    }

    public void update() {
    }
}
