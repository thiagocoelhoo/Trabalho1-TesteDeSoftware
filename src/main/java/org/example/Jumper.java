package org.example;

import java.util.Random;

public class Jumper {
    public final int INITIAL_COIN_AMOUNT = 1_000_000;
    private final Random randomGenerator = new Random();
    private final int FLOOR_HEIGHT = 300;

    private double x;
    private double y;

    private int coins;

    public Jumper(double x, double y) {
        this.x = x;
        this.y = y;
        this.coins = INITIAL_COIN_AMOUNT;
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
        int maxGoldTolerance = Integer.MAX_VALUE - this.coins;
        if ((this.coins + (other.getCoins()/2)) > maxGoldTolerance) {
            return;
        }

        int steal = (int)Math.ceil(other.getCoins() / 2.0f);
        other.setCoins(other.getCoins() - steal);
        this.coins += steal;
    }

}
