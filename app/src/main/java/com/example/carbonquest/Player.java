package com.example.carbonquest;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final String name;
    private int carbonFootprint;
    private int ecoCash;
    private int position;

    // Predefined set of positions that grant special cards
    private static final Set<Integer> SPECIAL_CARD_POSITIONS = new HashSet<>();

    static {
        // Add positions that grant special cards
        SPECIAL_CARD_POSITIONS.add(3);
        SPECIAL_CARD_POSITIONS.add(7);
        SPECIAL_CARD_POSITIONS.add(15);
        SPECIAL_CARD_POSITIONS.add(22);
        SPECIAL_CARD_POSITIONS.add(30);
    }

    public Player(String name, int carbonFootprint, int ecoCash) {
        this.name = name;
        this.carbonFootprint = carbonFootprint;
        this.ecoCash = ecoCash;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public int getCarbonFootprint() {
        return carbonFootprint;
    }

    public void reduceCarbon(int amount) {
        carbonFootprint = Math.max(0, carbonFootprint - amount);
    }

    public int getEcoCash() {
        return ecoCash;
    }

    public void spendEcoCash(int amount) {
        ecoCash -= amount;
    }

    public void move(int steps) {
        position += steps;
        position %= 20; // Example board size
    }

    public void increaseCarbon(int amount) {
        carbonFootprint += amount;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Checks if the player's current position corresponds to a special card position.
     *
     * @return true if the player's position is a special card position, false otherwise.
     */
    public boolean isOnSpecialCardPosition() {
        return SPECIAL_CARD_POSITIONS.contains(position);
    }
}
