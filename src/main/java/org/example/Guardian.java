package org.example;

import java.util.Random;

public class Guardian {
    private final static int JUMP_FORCE = 1000;

    private final static Random randomGenerator = new Random();

    private int coins;
    private double x;
    private double y;
    private double velocityY;
    private double velocityX;
    private double targetX;

    public Guardian(double position){
        this.x = position;
        this.y = 0;
        this.velocityY = 0.0;
        this.velocityX = 0.0;
        this.targetX = x;
        this.coins = 0;
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

    //TODO  "se o novo lugar computado para o guardião já estiver ocupado por um cluster, tal cluster é eliminado do
    // horizonte e seu respectivo g é adicionado ao g do guardião"
    //  if findNearestCluster: steal(Cluster); killCluster(Cluster);

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
