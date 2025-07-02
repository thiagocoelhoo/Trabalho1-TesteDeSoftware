package org.example;

import org.example.Creatures.Guardian;
import org.example.Creatures.Jumper;

import java.util.Iterator;

/**
 * Classe com a parte lógica da simulação
 */
public class Game {
    public static final float BORDER_LEFT = -4_000_000;
    public static final float BORDER_RIGHT = 4_000_000;

    private CircularLinkedList<Jumper> jumpers;
    private Iterator<Jumper> jumperIterator;
    private Jumper currentJumper = null;

    public static Guardian guardian;

    public Game() {
    }
    
    public CircularLinkedList<Jumper> getJumpers() {
        return jumpers;
    }

    public Jumper getCurrentJumper() {
        return currentJumper;
    }

    public void setCurrentJumper(Jumper currentJumper) {
        this.currentJumper = currentJumper;
    }

    public void createJumpers(int amount) {
        jumpers = new CircularLinkedList<>();

        if (amount <= 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < amount; i++) {
            double x = (BORDER_RIGHT - BORDER_LEFT) / amount * i;
            Jumper j = new Jumper(BORDER_LEFT + x );
            jumpers.add(j);
        }
        guardian = new Guardian(BORDER_LEFT + (BORDER_RIGHT - BORDER_LEFT) / amount * amount);
        jumpers.add(guardian);
        jumperIterator = jumpers.iterator();
    }

    public Jumper findNearestJumper(Jumper jumper) {
        //TODO cluster de criaturas ao se aproximarem
        Double minDistance = null;
        Jumper nearestJumper = null;

        for (Jumper j : jumpers.toList()) {
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

    public void handleCurrentJumper() {
        if (currentJumper == null) {
            selectNextJumper();
            return;
        }

        if (!currentJumper.isMoving()) {
            handleStealAndRemoval();
            currentJumper = null;
        }
    }

    public void selectNextJumper() {
        currentJumper = jumperIterator.next();
        currentJumper.jump();
    }

    public void handleStealAndRemoval() {
        Jumper nearest = findNearestJumper(currentJumper);

        if (nearest == null) {
            return;
        }

        // ninguém rouba nem mata o guardião
        if (nearest.isGuardian()){
            return;
        }

        // guardian latrocina cluster
        if (currentJumper.isGuardian && nearest.isCluster){
            currentJumper.setCoins(currentJumper.getCoins() + nearest.getCoins());
            jumpers.remove(nearest);
        }

        // clusters comem normais
        if (currentJumper.isCluster && !nearest.isCluster && !nearest.isGuardian){
            currentJumper.steal(nearest); // cluster engrandece
            jumpers.remove(nearest);     // mata jumper menor
        }

        else if (!currentJumper.isCluster && !nearest.isCluster && !nearest.isGuardian && !currentJumper.isGuardian){
            currentJumper.setCluster();
            currentJumper.setCoins(currentJumper.getCoins() + nearest.getCoins());
            //TODO generate new nearest for cluster to steal from
        }
        else{
            currentJumper.steal(nearest);
            if (nearest.getCoins() == 0) {
               jumpers.remove(nearest);
            }
        }
    }

    public void updateJumpersPhysics(double deltaTime) {
        for (Jumper j : jumpers.toList()) {
            j.update(deltaTime);

            if (j.getX() < BORDER_LEFT) {
                j.setPosition(BORDER_LEFT);
                j.stopJumping();
            } else if (j.getX() > BORDER_RIGHT) {
                j.setPosition(BORDER_RIGHT);
                j.stopJumping();
            }
        }
    }

    public void update(double deltaTime) throws IllegalArgumentException {
        if (deltaTime < 0.0) {
            throw new IllegalArgumentException();
        }
        handleCurrentJumper();
        updateJumpersPhysics(deltaTime);
    }
}
