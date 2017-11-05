package org.example.pacman;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by MortenSaabye on 01/11/2017.
 */
public class EnemyTest {
    private Enemy enemy;

    @Test
    public void findPos() throws Exception {
        int pos = enemy.findPos();

        assertEquals(pos, 1);
    }

    @Test
    public void move() throws Exception {
        enemy.move(200, 300);
        assertEquals(202, enemy.getY());
    }
    @Test
    public void notMove() throws Exception {
        enemy.move(200, 100);

        assertEquals(200, enemy.getY());
    }

    @Before
    public void setUp() {
        int boundX = 200;
        int boundY = 200;
        int boundH = 300;
        int boundW = 300;
        Bounds outerBounds = new Bounds(boundX, boundY, boundW, boundH);
        Bounds innerBounds = new Bounds(boundX + 400, boundY + 400,  0, 0);
        int y = outerBounds.getY();
        int x = outerBounds.getX();
        this.enemy = new Enemy(x,y, 100, 2, outerBounds, innerBounds);;
    }



}