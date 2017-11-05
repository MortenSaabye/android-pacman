package org.example.pacman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private MainActivity activity;
    //how points do we have
    private Integer points = 0;

    private int numOfCoins;
    private int numOfEnemies;
    //direction of the pacman, start with right
    private int direction = 1;

    //is the game running?
    private boolean running = false;

    //move this distance
    private int pacDistance = 3;
    private int enemyDistance = 2;

    //bitmap of the pacman
    private Bitmap currentPacBitmap;
    private Bitmap rightPacBitmap;
    private Bitmap leftPacBitmap;
    private Bitmap downPacBitmap;
    private Bitmap upPacBitmap;

    private Bitmap coinBitmap;

    private Bitmap rightEnemyBitmap;
    private Bitmap leftEnemyBitmap;

    //bitmap sizes
    private int coinSize = 80;
    private int pacSize = 150;
    private int enemySize = 100;

    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;

    private int pacCenterX;
    private int pacCenterY;

    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();

    //the list of enemies
    private ArrayList<Enemy> enemies = new ArrayList<>();

    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view, int coins, int enemies)
    {
        this.context = context;
        this.pointsView = view;
        this.activity = (MainActivity) context;
        this.numOfCoins = coins;
        this.numOfEnemies = enemies;
        addBitmaps();
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameView.invalidate(); //redraw screen

    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void moveLeft()
    {
        if (pacx-pacDistance>0) {
            pacx=pacx-pacDistance;
            currentPacBitmap = leftPacBitmap;
        }

    }
    public void moveUp()
    {
        if (pacy-pacDistance>0) {
            pacy = pacy - pacDistance;
            currentPacBitmap = upPacBitmap;
        }
    }

    public void moveDown()
    {
        if (pacy+pacDistance+downPacBitmap.getHeight()<h) {
            pacy=pacy+pacDistance;
            currentPacBitmap = downPacBitmap;
        }

    }

    public void moveRight()
    {
        if (pacx+pacDistance+rightPacBitmap.getWidth()<w) {
            pacx=pacx+pacDistance;
            currentPacBitmap = rightPacBitmap;
        }
    }
    public void stop() {
        this.direction = 0;
    }

    public void doCollisionCheck()
    {
        checkCoins();
        checkEnemies();
    }

    private void checkCoins() {
        int coinCenterX;
        int coinCenterY;
        for(GoldCoin coin : coins) {
            if(!coin.isTaken()) {
                coinCenterX = coin.getX() + (coinSize / 2);
                coinCenterY = coin.getY() + (coinSize / 2);
                double distance = Math.sqrt(Math.pow(coinCenterX - pacCenterX, 2) + Math.pow(coinCenterY - pacCenterY, 2));
                if(distance < 50) {
                    coin.snatch();
                    points++;
                    pointsView.setText("Points: " + points.toString());
                    checkWin();
                }
            }
        }
    }
    private void checkEnemies() {
        int enemyCenterX;
        int enemyCenterY;
        for(Enemy enemy : enemies) {
            enemyCenterX = enemy.getX() + (coinSize / 2);
            enemyCenterY = enemy.getY() + (coinSize / 2);
            double distance = Math.sqrt(Math.pow(enemyCenterX - pacCenterX, 2) + Math.pow(enemyCenterY - pacCenterY, 2));
            if(distance < 50) {
                activity.gameLost();
            }
        }
    }

    public void addCoins() {
        Random r = new Random();
        for(int i = 0; i < numOfCoins; i++) {
            int x = r.nextInt(w - coinBitmap.getWidth() - 30);
            int y = r.nextInt(h - coinBitmap.getHeight() - 30);
            GoldCoin coin = new GoldCoin(x,y);
            coins.add(coin);
        }
    }

    public void addEnemies() {
        //Splitting up the canvas for the enemies.
        int laneHeight = this.h / (numOfEnemies * 2 - 1);
        int laneWidth = this.w / (numOfEnemies * 2 - 1);
        for(int i = 0; i < numOfEnemies; i++) {
            int boundX = laneWidth * i;
            int boundY = laneHeight * i;
            int boundH = this.h - (2 * laneHeight * i);
            int boundW = this.w - (2 * laneWidth * i);
            Bounds outerBounds = new Bounds(boundX, boundY, boundW, boundH);
            Bounds innerBounds = new Bounds(boundX + laneWidth, boundY + laneHeight,
                    boundW - 2*laneWidth, boundH - 2*laneHeight);
            int y = outerBounds.getY() + outerBounds.getHeight() - 130;
            int x = outerBounds.getX();
            Enemy enemy = new Enemy(x,y, enemySize, enemyDistance, outerBounds, innerBounds);
            enemies.add(enemy);
        }
    }

    public void TimerMethod()
    {
        pacCenterX = pacx + (pacSize / 2);
        pacCenterY = pacy + (pacSize / 2);
        activity.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            moveEnemies();
            switch (direction) {
                case 1:
                    moveRight();
                    break;
                case 2:
                    moveLeft();
                    break;
                case 3:
                    moveDown();
                    break;
                case 4:
                    moveUp();
                    break;
                default:
                    stop();
                    break;
            }
            doCollisionCheck();
            gameView.invalidate();
        }
    };

    private void moveEnemies() {
        for(Enemy enemy : enemies) {
           enemy.move(pacCenterX, pacCenterY);
        }
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public Bitmap getCurrentPacBitmap() {
        return currentPacBitmap;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Bitmap getLeftEnemyBitmap() {
        return leftEnemyBitmap;
    }
    public Bitmap getRightEnemyBitmap() {
        return rightEnemyBitmap;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean getRunning() {
        return this.running;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }
    public ArrayList<Enemy> getEnemies() { return enemies; }

    public Bitmap getCoinBitmap()
    {
        return coinBitmap;
    }

    private void checkWin() {
        if(points >= coins.size()) {
            activity.gameWon();
        }
    }

    private void addBitmaps(){
        Matrix matrix = new Matrix();
        rightPacBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman), this.pacSize, this.pacSize, true);

        matrix.postRotate(90);
        downPacBitmap = Bitmap.createBitmap(rightPacBitmap, 0, 0, rightPacBitmap.getWidth(), rightPacBitmap.getHeight(), matrix, true );
        matrix.postRotate(-180);

        upPacBitmap = Bitmap.createBitmap(rightPacBitmap, 0, 0, rightPacBitmap.getWidth(), rightPacBitmap.getHeight(), matrix, true );

        coinBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.coin), this.coinSize, this.coinSize, true);

        leftEnemyBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy), this.enemySize, this.enemySize, true);
        //mirror the matrix for leftPacBitmap and rightEnemyBitmap
        matrix.postScale(-1, 1);

        matrix.postRotate(-90);
        leftPacBitmap = Bitmap.createBitmap(rightPacBitmap, 0, 0, rightPacBitmap.getWidth(), rightPacBitmap.getHeight(), matrix, true );
        rightEnemyBitmap = Bitmap.createBitmap(leftEnemyBitmap, 0, 0, leftEnemyBitmap.getWidth(), leftEnemyBitmap.getHeight(), matrix, true );

        currentPacBitmap = rightPacBitmap;
    }

}
