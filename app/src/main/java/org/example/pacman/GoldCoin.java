package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    private int x;
    private int y;


    private boolean isTaken = false;

    public GoldCoin(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void snatch(){
        this.isTaken = true;
    }
    public boolean isTaken() {
        return isTaken;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GoldCoin() {
    }
}
