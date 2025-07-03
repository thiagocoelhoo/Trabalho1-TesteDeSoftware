package org.example.app.controllers;

import org.example.app.models.Jumper;
import org.example.app.models.JumperType;
import org.example.utils.CircularLinkedList;

import java.util.Iterator;

/**
 * Classe com a parte lógica da simulação
 */
public class GameController {
    public static final float BORDER_LEFT = -4_000_000;
    public static final float BORDER_RIGHT = 4_000_000;

    private CircularLinkedList<Jumper> jumpers;
    private Iterator<Jumper> jumperIterator;
    private Jumper currentJumper = null;

    public GameController() {
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
        jumpers = new CircularLinkedList<Jumper>();

        if (amount <= 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < amount; i++) {
            double x = (BORDER_RIGHT - BORDER_LEFT) / amount * i;
            Jumper j = new Jumper(BORDER_LEFT + x );
            jumpers.add(j);
        }

        jumperIterator = jumpers.iterator();
    }

    public Jumper findNearestJumper(Jumper jumper) {
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

        Double distance = Math.abs(currentJumper.getX() - nearest.getX());

        if (distance < 100000) {
            if (
                    (currentJumper.type == JumperType.CRIATURA && nearest.type == JumperType.CRIATURA) ||
                    (currentJumper.type == JumperType.CRIATURA && nearest.type == JumperType.CLUSTER) ||
                    (currentJumper.type == JumperType.CLUSTER && nearest.type == JumperType.CRIATURA) ||
                    (currentJumper.type == JumperType.CLUSTER && nearest.type == JumperType.CLUSTER)
            ) {
                currentJumper.type = JumperType.CLUSTER;
                currentJumper.setCoins(currentJumper.getCoins() + nearest.getCoins());
                jumpers.remove(nearest);
            } else if (currentJumper.type == JumperType.CLUSTER) {

            } else if (currentJumper.type == JumperType.GUARDIAO && nearest.type == JumperType.CLUSTER) {
                currentJumper.setCoins(currentJumper.getCoins() + nearest.getCoins());
                jumpers.remove(nearest);
            }
        } else {
            // Assalto
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
