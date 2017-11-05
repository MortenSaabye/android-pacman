package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    GameView gameView;
    Game game;
    private Timer moveTimer;
    private Timer gameTimer;
    private Integer seconds;
    private int difficulty = 30;
    private SeekBar difficultySeekbar;
    private int numCoins = 0;
    private MainActivity activity;
    private int numEnemies = 0;
    TextView secondsView;
    private Button startPauseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            difficulty = savedInstanceState.getInt("difficulty");
        }
        seconds = 60;
        activity = this;
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gameView =  findViewById(R.id.gameView);
        difficultySeekbar = findViewById(R.id.slider);
        difficultySeekbar.setProgress(difficulty);
        final TextView points = findViewById(R.id.points);
        secondsView = findViewById(R.id.seconds);
        startPauseBtn = findViewById(R.id.startBtn);
        secondsView.setText(seconds.toString());

        setupGame(points);

        moveTimer = new Timer();
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(game.getRunning()) {
                    game.TimerMethod();
                }
            }
        }, 0, 5);

        setupSwipeListener();
        setupSlider(difficultySeekbar);
    }
    public void startStop(View view) {
        game.setRunning(!game.getRunning());

        Button btn = (Button) view;
        if(game.getRunning()) {
            btn.setText("Pause");
            makeGameTimer();
        } else {
            gameTimer.cancel();
            btn.setText("Continue");
        }
    }
    public void resetGame(View view) {
        moveTimer.cancel();
        if(game.getRunning()) {
            gameTimer.cancel();
        }
        this.startPauseBtn.setText("Start");
        Bundle bundle = new Bundle();
        bundle.putInt("difficulty", difficulty);
        onCreate(bundle);
    }

    private void setupSlider(SeekBar difficultySeekbar) {
        difficultySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                difficulty = seekBar.getProgress();
                resetGame(seekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    private void setupGame(TextView points) {
        numEnemies = difficulty / 7;
        numCoins = difficulty / 4;
        game = new Game(this ,points, numCoins, numEnemies);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();
    }

    private void timeOut() {
        onCreate(new Bundle());
        game.setRunning(false);
        moveTimer.cancel();
        gameTimer.cancel();
        this.startPauseBtn.setText("Start");
        CharSequence text = "You are slow!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
        seconds = 60;
    }

    private Runnable incrementSeconds = new Runnable() {
        public void run(){
            seconds--;
            secondsView.setText(seconds.toString());
            if(seconds == 0) {
                activity.timeOut();
            }
        }
    };

    public void gameWon() {
        gameTimer.cancel();
        moveTimer.cancel();
        startPauseBtn.setText("Start");
        CharSequence text = "You win!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
        game.setRunning(false);
    }
    public void gameLost() {
        startPauseBtn.setText("Start");
        gameTimer.cancel();
        moveTimer.cancel();
        CharSequence text = "You Lose!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
        game.setRunning(false);
    }
    private void makeGameTimer() {
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(activity.incrementSeconds);
            }
        }, 0, 1000);
    }

    private void setupSwipeListener(){
        gameView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight () {
                game.setDirection(1);
            }
            @Override
            public void onSwipeLeft () {
                game.setDirection(2);
            }
            @Override
            public void onSwipeBottom () {
                game.setDirection(3);
            }
            @Override
            public void onSwipeTop () {
                game.setDirection(4);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
