package org.example;

import java.util.Random;

public class Jumper {
    public final int INITIAL_COIN_AMOUNT = 100_000;
    private final int FLOOR_HEIGHT = 300;
    private final int JUMP_FORCE = 1000;

    private final Random randomGenerator = new Random();

    private int coins;
    private double x;
    private double y;
    private double velocityY;
    private double velocityX;
    private double targetX;

    public Jumper(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityY = 0.0;
        this.velocityX = 0.0;
        this.targetX = x;
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

    public boolean isMoving() {
        return velocityX != 0;
    }

    public void jump() {
        float r = randomGenerator.nextFloat(-1, 1);
        this.targetX = calcJump(this.x, r, this.coins);
        this.velocityX = targetX - x;
        this.velocityY = -JUMP_FORCE / 2;
    }

    public void steal(Jumper other) {
        if (other == null) {
            return;
        }

        int steal = (int)Math.ceil(other.getCoins() / 2.0f);
        other.setCoins(other.getCoins() - steal);
        this.coins += steal;
    }

    public void update(double deltaTime) {
        this.x += velocityX * deltaTime;
        this.y += velocityY * deltaTime;

        if (this.y > FLOOR_HEIGHT) {
            this.y = FLOOR_HEIGHT;
            this.velocityY = 0;
        } else {
            this.velocityY += JUMP_FORCE * deltaTime;
        }

        if ((velocityX > 0 && x >= targetX) || (velocityX < 0 && x <= targetX)) {
            this.x = targetX;
            this.y = FLOOR_HEIGHT;
            this.velocityX = 0;
            this.velocityY = 0;
        }
    }
}
