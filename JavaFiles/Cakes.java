package com.example.colto.cjohnsonfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Cakes extends View {

    private float height;
    private float width;
    private float ratio;

    private boolean[][] curMaze;
    private int totalCakes;
    private boolean empty;

    private RectF circle;
    private Paint paint;
    private MediaPlayer mp;
    Ashman ashman;
    Ghosts ghosts;
    Button gameToggle;
    TextView totalDisplay;
    String displayText;
    TextView levelDisplay;
    String levelText;

    public Cakes(Context context) {
        super(context);
        initialize();
    }

    public Cakes(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public Cakes(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize () {
        height = 160;
        width = 160;
        ratio = 1;
        empty = false;

        circle = new RectF();
        paint = new Paint();
        paint.setARGB (200, 104, 239, 173);



        curMaze = new boolean[16][16];
    }

    public int getTotalCakes () {
        return totalCakes;
    }

    public boolean[][] getMaze () {
        return this.curMaze;
    }

    public boolean isEmpty () {
        return this.empty;
    }

    public void setEmpty (boolean state) {
        this.empty = state;
    }

    //Gets TextView that displays the total cakes left and the String that says "Cakes Left:"
    public void getTotalDisplay(TextView totalDisplay, TextView levelDisplay, String displayText, String levelText) {
        this.totalDisplay = totalDisplay;
        this.displayText = displayText;
        this.levelDisplay = levelDisplay;
        this.levelText = levelText;
    }

    //Updates the curMaze array to reflect the remaining cakes when ash man eats a cake
    public void updateCakes (int x, int y) {
        if (curMaze[y][x]) {
            if (ashman.getState()) {
                stopCakes();
                mp = MediaPlayer.create(getContext().getApplicationContext(), R.raw.coin10);
                mp.start();

            }
            curMaze[y][x] = false;
            totalCakes --;
            String cakeCount = displayText + totalCakes;
            totalDisplay.setText(cakeCount);
            invalidate();
            if (totalCakes == 0) {
                endLevel();
            }
        }
    }

    public void stopCakes () {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public void endLevel () {
        if (ashman.isAlive()) {
            MediaPlayer levelFinished = MediaPlayer.create(getContext().getApplicationContext(), R.raw.magical_3);
            levelFinished.start();
            while (!levelFinished.isPlaying()) {
                levelFinished.release();
            }
            gameToggle.setText(R.string.buttonNext);
            Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT).show();
            ashman.pause();
            for (Circle circle : ashman.getCircles()) {
                circle.setDirection(Character.STILL);
            }
            ghosts.pause();
            for (Circle circle : ghosts.getCircles()) {
                circle.setDirection(Character.STILL);
            }
            empty = true;

        }
    }

    //Takes the current maze and creates a deep copy that will be used to keep track of where there are cakes
    public void setMaze (boolean[][] maze) {
        boolean[][] cakeMaze = new boolean[16][16];
        totalCakes = 0;
        for (int ix = 0; ix < 16; ix ++) {
            for (int i = 0; i < 16; i ++) {
                if (maze[ix][i]) {
                    cakeMaze[ix][i] = true;
                    totalCakes ++;
                }
            }
        }
        curMaze = cakeMaze;
    }

    public void getObjects (Ashman ashman, Ghosts ghosts, Button gameToggle) {
        this.ashman = ashman;
        this.ghosts = ghosts;
        this.gameToggle = gameToggle;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.scale(canvas.getWidth() / width, canvas.getHeight() / height);

        //draws the maze
        float gridWidth = width / 16;
        float gridHeight = width / 16;
        for (int ix = 0; ix < 16; ix ++)
            for (int i = 0; i < 16; i++) {
                circle.set((ix * gridWidth) + 3, (i * gridHeight) + 3,
                        ((ix * gridWidth) + gridWidth) - 3, ((i * gridHeight) + gridHeight) - 3);
                if (curMaze[i][ix]) canvas.drawOval(circle, paint);
            }
    }

    //Fits the cakes to screen
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
