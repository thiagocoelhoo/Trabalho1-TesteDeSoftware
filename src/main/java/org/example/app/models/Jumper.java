package org.example.app.models;

import java.util.Random;

public class Jumper {
    public final static int INITIAL_COIN_AMOUNT = 1_000_000;
    private final static int JUMP_FORCE = 1000;

    private final static Random randomGenerator = new Random();

    private int coins;
    private double x;
    private double y;
    private double velocityY;
    private double velocityX;
    private double targetX;
    public JumperType type = JumperType.CRIATURA;

    public Jumper(double position) {
        this.x = position;
        this.y = 0;
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

    public void setPosition(double x) {
        this.x = x;
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

    public void jumpTo(double target) {
        this.targetX = target;
        this.velocityX = target - this.x;
        this.velocityY = -JUMP_FORCE / 2.0;
    }

    public void jump() {
        float r = randomGenerator.nextFloat(-1, 1);
        double target = calcJump(this.x, r, this.coins);
        jumpTo(target);
    }

    public void stopJumping() {
        velocityX = 0;
        targetX = x;
    }

    public boolean isMoving() {
        return velocityY != 0 || velocityX != 0;
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

        if (this.y > 0) {
            this.y = 0;
            this.velocityY = 0;
        } else {
            this.velocityY += JUMP_FORCE * deltaTime;
        }

        if ((velocityX > 0 && x >= targetX) || (velocityX < 0 && x <= targetX)) {
            this.x = targetX;
            this.velocityX = 0;
        }
    }
}
