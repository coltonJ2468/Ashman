package com.example.colto.cjohnsonfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;

public class Ghosts extends Character implements Serializable{
    private RectF ghost;
    private Paint paint;
    private Random rand;
    private Ashman ashman;
    private Button gameToggle;
    private int numGhosts;
    private int speedConstant;

    private Handler ghostHandler;
    private Runnable ghostTimer;
    private boolean isRunning;

    public Ghosts(Context context) {
        super(context);
        initialize(context);
    }

    public Ghosts(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public Ghosts(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @Override
    public void initialize(Context context) {
        super.initialize(context);
        numGhosts = 2;
        rand = new Random();
        ghost = new RectF ();
        paint = new Paint ();
        paint.setARGB (255, 252, 228, 236);
        isRunning = false;
        speedConstant = 2;

        ghostHandler = new Handler ();
        ghostTimer = new Runnable() {
            @Override
            public void run() {
                onTimer();
                ghostHandler.postDelayed(ghostTimer, Character.TIMER_M_SEC * speedConstant);
            }
        };
    }

    public void setAshman (Ashman ashman) {
        this.ashman = ashman;
    }

    public int getNumGhosts () {
        return this.numGhosts;
    }

    public int getConstant () {
        return this.speedConstant;
    }

    public void setConstant (int num) {
        this.speedConstant = num;
    }

    public void setNumGhosts (int numGhosts) {
        this.numGhosts = numGhosts;
    }

    public void setGameToggle (Button gameToggle) {
        this.gameToggle = gameToggle;
    }

    public void endGame () {
        if (ashman.isAlive()) {
            MediaPlayer levelFailed = MediaPlayer.create(getContext().getApplicationContext(), R.raw.ghost);
            levelFailed.start();
            while (!levelFailed.isPlaying()) {
                levelFailed.release();
            }
            gameToggle.setText(R.string.buttonRestart);
            Toast.makeText(getContext(), "You Lose", Toast.LENGTH_SHORT).show();
            ashman.isDead();
            ashman.pause();
            for (Circle circle : ashman.getCircles()) {
                circle.setDirection(Character.STILL);
            }
            pause();
            for (Circle circle : this.getCircles()) {
                circle.setDirection(Character.STILL);
            }
        }
    }

    @Override
    public void onTimer () {
        super.onTimer();
        for (Circle ashCircle : ashman.getCircles()) {
            float locX = ashCircle.getLocX();
            float locY = ashCircle.getLocY();
            float rightLocX = ashCircle.getLocX() + 10;
            float botLocY = ashCircle.getLocY() + 10;
            for (Circle circle : getCircles()) {
                //Checks right side of ghost for ash man
                if (locX == circle.getLocX() + 10 && locY == circle.getLocY()) {
                    endGame();
                }
                //Checks above ghost for ash man
                else if (botLocY == circle.getLocY() && locX == circle.getLocX()) {
                    endGame();
                }
                //Checks left side of ghost for ash man
                else if (rightLocX == circle.getLocX() && locY == circle.getLocY()) {
                    endGame();
                }
                //Checks below ghost for ash man
                else if (locY == circle.getLocY() + 10 && locX == circle.getLocX()) {
                    endGame();
                }

                if (!circle.getIfMoved() && circle.getDirection() != Character.STILL) {
                    circle.setDirection(rand.nextInt(4));
                }
            }
        }
        invalidate();
    }

    public void increaseSpeed () {
        speedConstant = 1;
    }

    public void resume () {
        isRunning = true;
        ghostHandler.postDelayed(ghostTimer, Character.TIMER_M_SEC * speedConstant);
    }

    //pauses the animation
    public void pause () {
        isRunning = false;
        ghostHandler.removeCallbacks(ghostTimer);
    }

    public boolean getState () {
        return this.isRunning;
    }

    public void setState (boolean state) {
        this.isRunning = state;
    }

    @Override
    public void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        float gridWidth = 10;
        for (Circle circle : getCircles()) {
            float locX = circle.getLocX ();
            float locY = circle.getLocY ();
            this.ghost.set(locX + 1, locY + 1, (locX + gridWidth) - 1, (locY + gridWidth) - 1);
            canvas.drawOval(this.ghost, paint);
        }
    }
}
