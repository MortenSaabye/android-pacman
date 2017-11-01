package org.example.pacman;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by MortenSaabye on 01/11/2017.
 */
public class EnemyTest {

    @Test
    public void findPos() throws Exception {

        Enemy enemy = this.createEnemy();
        int pos = enemy.findPos();

        assertEquals(pos, 1);
    }

    @Test
    public void move() throws Exception {
        Enemy enemy = this.createEnemy();
        enemy.move(200, 300);

        assertEquals(202, enemy.getY());
    }

    @Test
    public void notMove() throws Exception {
        Enemy enemy = this.createEnemy();
        enemy.move(200, 100);

        assertEquals(200, enemy.getY());
    }

    private Enemy createEnemy(){
        int boundX = 200;
        int boundY = 200;
        int boundH = 300;
        int boundW = 300;
        Bounds outerBounds = new Bounds(boundX, boundY, boundW, boundH);
        Bounds innerBounds = new Bounds(boundX + 400, boundY + 400,  0, 0);
        int y = outerBounds.getY();
        int x = outerBounds.getX();
        Enemy enemy = new Enemy(x,y, 100, 2, outerBounds, innerBounds);
        return enemy;
    }

}