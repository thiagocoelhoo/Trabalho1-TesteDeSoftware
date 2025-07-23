package org.example.app.controllers;

import org.example.app.models.Jumper;
import org.example.app.models.JumperType;
import org.example.utils.CircularLinkedList;

import javax.swing.*;
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

    private boolean finishSimulation = false;

    public GameController() {

    }

    public boolean getSimulationState(){
        return finishSimulation;
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

        amount += 1; // Considerar a criação do guardião

        // Criar criaturas
        for (int i = 0; i < amount; i++) {
            double x = (BORDER_RIGHT - BORDER_LEFT) / amount * i;
            Jumper j = new Jumper(BORDER_LEFT + x );
            jumpers.add(j);
        }

        // Criar guardião
        Jumper guardian = jumpers.get(jumpers.size() - 1);
        guardian.type = JumperType.GUARDIAO;
        guardian.setCoins(0);

        jumperIterator = jumpers.iterator();
    }

    public Jumper findNearestJumper(Jumper jumper) {
        Double minDistance = null;
        Jumper nearestJumper = null;

        for (Jumper j : jumpers.toList()) {
            // Não considerar o guardião
            if (j != jumper && j.type != JumperType.GUARDIAO) {
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

        // condição de parada aqui?
        if (jumpers.size() < 3){
            if (jumpers.get(0).type != JumperType.CLUSTER) {
                finishSimulation();
            }
        }
    }

    public void selectNextJumper() {
        currentJumper = jumperIterator.next();
        currentJumper.jump();
    }

    public boolean checkCollision(Jumper j1, Jumper j2) {
        double distance = Math.abs(j1.getX() - j2.getX());
        final int collision_threshold = 150000;
        return (distance < collision_threshold);
    }

    public void handleCollision(Jumper j1, Jumper j2) {
        if (
            (j1.type == JumperType.CRIATURA || j1.type == JumperType.CLUSTER ) &&
            j2.type == JumperType.CRIATURA
        ) {
            j1.type = JumperType.CLUSTER;
            j1.setCoins(j1.getCoins() + j2.getCoins());
            jumpers.remove(j2);
        } else if (j1.type == JumperType.GUARDIAO && j2.type == JumperType.CLUSTER) {
            j1.setCoins(j1.getCoins() + j2.getCoins());
            jumpers.remove(j2);
        }
    }

    public void handleStealAndRemoval() {
        Jumper nearest = findNearestJumper(currentJumper);

        if (nearest == null) {
            return;
        }

        // Colisão
        if (checkCollision(currentJumper, nearest)) {
            handleCollision(currentJumper, nearest);
            nearest = findNearestJumper(currentJumper);
        }

        // Assalto
        if (currentJumper.type == JumperType.GUARDIAO) {
            // O Guardião não deve assaltar ninguém.
            return;
        }

        if (nearest == null) {
            // Se não houver outra criatura/cluster retorne.
            return;
        }

        currentJumper.steal(nearest);
        if (nearest.getCoins() == 0) {
            jumpers.remove(nearest);
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

    public void finishSimulation() {
        finishSimulation = true;
    }
}
