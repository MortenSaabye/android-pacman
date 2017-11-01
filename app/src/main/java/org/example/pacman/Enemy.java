package org.example.pacman;

import android.util.Log;

import java.util.Random;

/**
 * Created by MortenSaabye on 27/10/2017.
 */

public class Enemy {
    private int direction = 0;
    private int speed = 1;
    private int x;
    private int y;
    private Bounds outerBounds;
    private Bounds innerBounds;
    private int size;
    private int distance;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Enemy(int x, int y, int size, int distance, Bounds outerBounds, Bounds innerBounds) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.distance = distance;
        this.outerBounds = outerBounds;
        this.innerBounds = innerBounds;
    }

    public void move(int pacCenterX, int pacCenterY) {
        int enemyCenterX = this.x + (size / 2);
        int enemyCenterY = this.y + (size / 2);
        boolean yNegative = false;
        boolean xNegative = false;

        int distanceY = enemyCenterY - pacCenterY;
        if(distanceY < 0) {
            distanceY = -distanceY;
            yNegative = true;
        }

        int distanceX = enemyCenterX - pacCenterX;

        if(distanceX < 0) {
            distanceX = -distanceX;
            xNegative = true;
        }

        if(distanceX > distanceY) {
            if(xNegative) {
                moveRight();
            } else {
                moveLeft();
            }
        } else {
            if(yNegative) {
                moveDown();
            } else {
                moveUp();
            }
        }
    }

    private void moveRight() {
        int pos = findPos();
        switch (pos) {
            case 1 :
            case 2 :
            case 7 :
            case 6 :
                this.x = this.x + this.distance;
                break;
            case 3 :
            case 4 :
            case 5 :
                if(this.x + this.distance < this.outerBounds.getX() + this.outerBounds.getWidth()) {
                    this.x = this.x + this.distance;
                }
                break;
            case 8 :
                if(this.x + this.distance < this.innerBounds.getX()) {
                    this.x = this.x + this.distance;
                } else {
                    moveUp();
                }
                break;
            default:
                Log.d("moveError", "Moving right, pos: " + pos);

        }
        this.direction = 2;
    }

    private void moveLeft() {
        int pos = findPos();
        switch (pos) {
            case 2 :
            case 3 :
            case 5 :
            case 6 :
                this.x = this.x - this.distance;
                break;
            case 1 :
            case 8 :
            case 7 :
                if(this.x - this.distance > this.outerBounds.getX()) {
                    this.x = this.x - this.distance;
                }
                break;
            case 4 :
                if(this.x - this.distance > this.innerBounds.getX() + this.innerBounds.getWidth()) {
                    this.x = this.x - this.distance;
                } else {
                    moveDown();
                }
                break;
            default:
                Log.d("moveError", "Moving left, pos: " + pos);

        }
        this.direction = 1;
    }
    private void moveDown() {
        int pos = findPos();
        switch (pos) {
            case 1:
            case 8:
            case 3:
            case 4:
                this.y = this.y + this.distance;
                break;
            case 5:
            case 6:
            case 7:
               if(this.y + this.distance < this.outerBounds.getY() + this.outerBounds.getHeight()) {
                   this.y = this.y + this.distance;
               }
               break;
            case 2:
                if (this.y + this.distance < this.innerBounds.getY()) {
                    this.y = this.y + this.distance;
                } else {
                    moveRight();
                }
                break;
            default:
                Log.d("moveError", "Moving down, pos: " + pos);
        }
    }
    private void moveUp() {
        int pos = findPos();
        switch (pos) {
            case 7:
            case 8:
            case 4:
            case 5:
                this.y = this.y - this.distance;
                break;
            case 1:
            case 2:
            case 3:
                if(this.y - this.distance > this.outerBounds.getY()) {
                    this.y = this.y - this.distance;
                }
                break;
            case 6:
                if (this.y - this.distance > this.innerBounds.getY() + this.innerBounds.getHeight()) {
                    this.y = this.y - this.distance;
                } else {
                    moveLeft();
                }
                break;
            default:
                Log.d("moveError", "Moving up, pos: " + pos);
        }
    }

    public int findPos() {
        boolean above = false;
        boolean below = false;
        boolean left = false;
        boolean right = false;
        if(this.y < this.innerBounds.getY()) {
            //above the inner bounds
            above = true;
        }
        if(this.y > this.innerBounds.getY() + this.innerBounds.getHeight()) {
            //below inner bounds
            below = true;
        }
        if(this.x < this.innerBounds.getX()) {
            // on the left side of the inner bounds
            left = true;
        }
        if(this.x > this.innerBounds.getX() + this.innerBounds.getWidth()) {
            //on the right side of the inner bounds
            right = true;
        }

        if(above) {
            if(left){
                return 1;
            } else if(right){
                return 3;
            } else {
                return 2;
            }
        } else if (below) {
            if(left){
                return 7;
            } else if(right){
                return 5;
            } else {
                return 6;
            }
        } else {
            //not above or below, so in the middle...
            if(left){
                return 8;
            } else if(right){
                return 4;
            } else {
                return 0;
            }
        }
    }
}
