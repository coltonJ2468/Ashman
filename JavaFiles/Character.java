package com.example.colto.cjohnsonfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class Character extends View {
    private float height;
    private float width;
    private float ratio;
    private ArrayList<Circle> circles;
    private Random rand;

    public final static int RIGHT = 0;
    public final static int UP = 1;
    public final static int LEFT = 2;
    public final static int DOWN = 3;
    public final static int STILL = 4;
    public final static int TIMER_M_SEC = 50;

    private boolean[][] curMaze;

    public Character(Context context) {
        super(context);
    }

    public Character(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Character(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize (Context context) {
        height = 160;
        width = 160;
        ratio = 1;

        rand = new Random ();
        circles = new ArrayList<> ();
        curMaze = new boolean[16][16];
    }

    public void createGhosts (int numGhosts) {
        numGhosts = circles.size() + numGhosts;
        while (circles.size() < numGhosts) {

            int posX = rand.nextInt(14) + 1;
            int posY = rand.nextInt(14) + 1;
            float locX = posX * 10;
            float locY = posY * 10;
            while ((locX < 100 && locX > 40) || (locY < 100 && locY > 40) || !curMaze[posY][posX]) {
                posX = rand.nextInt(14) + 1;
                posY = rand.nextInt(14) + 1;
                locX = posX * 10;
                locY = posY * 10;
            }
            int direction = rand.nextInt(4);
            circles.add(new Circle (locX, locY, direction));
        }
    }

    public ArrayList<Circle> getCircles () {
        return this.circles;
    }

    public void setCircles (ArrayList<Circle> circles) {
        this.circles = circles;
    }

    public void setMaze (boolean[][] maze) {
        this.curMaze = maze;
    }

    public int getPosX (float locX) {
        return (int) ((locX + 0.7) / 10);
    }

    public int getPosY (float locY) {
        return (int) ((locY + 0.7) / 10);
    }

    public int getRightPosX (float locX) {
        return (int) ((locX + 9.3) / 10);
    }

    public int getBotPosY(float locY) {
        return (int) ((locY + 9.3) / 10);
    }

    public void onTimer () {
        for (Circle circle : circles) {
            int posX = getPosX(circle.getLocX());
            int posY = getPosY(circle.getLocY());
            int rightPosX = getRightPosX(circle.getLocX());
            int botPosY = getBotPosY(circle.getLocY());
            int direction = circle.getDirection();
            int step = 1;
            circle.setIfMoved (false);

            if (direction == RIGHT) {
                if (curMaze[posY][posX + 1] && curMaze[botPosY][posX + 1]) {
                    circle.setLocX(circle.getLocX() + step);
                    circle.setIfMoved(true);
                }
            } else if (direction == UP) {
                if (curMaze[botPosY - 1][rightPosX] && curMaze[botPosY - 1][posX]) {
                    circle.setLocY(circle.getLocY() - step);
                    circle.setIfMoved(true);
                }
            } else if (direction == LEFT) {
                if (curMaze[botPosY][rightPosX - 1] && curMaze[posY][rightPosX - 1]) {
                    circle.setLocX(circle.getLocX() - step);
                    circle.setIfMoved(true);
                }
            } else if (direction == DOWN) {
                if (curMaze[posY + 1][posX] && curMaze[posY + 1][rightPosX]) {
                    circle.setLocY(circle.getLocY() + step);
                    circle.setIfMoved(true);
                }
            }
        }
    }

    @Override
    public void onDraw (Canvas canvas) {
        canvas.scale(canvas.getWidth() / width, canvas.getHeight() / height);
    }

    //Fits the characters to screen
    @Override
    public void onMeasure(int widthMeasure, int heightMeasure) {
        int tempHeight, tempWidth;
        int height1 = MeasureSpec.getSize(heightMeasure);
        int width1 = MeasureSpec.getSize(widthMeasure);
        int widthCalc = (int) (height1 * ratio);
        int heightCalc = (int) (width1 / ratio);
        if (heightCalc > height1) {
            tempHeight = height1;
            tempWidth = widthCalc;
        }
        else {
            tempHeight = heightCalc;
            tempWidth = width1;
        }
        setMeasuredDimension(tempWidth, tempHeight);
    }
}
