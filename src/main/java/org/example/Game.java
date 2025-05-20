package org.example;

import java.util.Iterator;

public class Game {
    private CircularLinkedList<Jumper> jumpers;
    private Iterator<Jumper> jumperIterator;
    private Jumper currentJumper = null;

    public Game() {
    }
    
    public CircularLinkedList<Jumper> getJumpers() {
        return jumpers;
    }

    public void createJumpers(int amount) {
        jumpers = new CircularLinkedList<Jumper>();
        double LEFT = -100_000.0;
        double RIGHT = 100_000.0;

        if (amount <= 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < amount; i++) {
            double x = (RIGHT - LEFT) / amount * i;
            System.out.println(x);
            Jumper j = new Jumper(LEFT + x , 300);
            jumpers.add(j);
        }

        jumperIterator = jumpers.iterator();
        currentJumper = jumperIterator.next();
    }

    private Jumper findNearestJumper(Jumper jumper) {
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

    public void update(double deltaTime) {
        if (!currentJumper.isMoving()) {
            currentJumper = jumperIterator.next();
            currentJumper.jump();

            Jumper nearestJumper = findNearestJumper(currentJumper);
            currentJumper.steal(nearestJumper);

            if (nearestJumper.getCoins() == 0) {
                jumpers.remove(nearestJumper);
            }
        }

        for (Jumper j: jumpers.toList()) {
            j.update(deltaTime);
        }
    }
}
