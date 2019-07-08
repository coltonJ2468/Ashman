package com.example.colto.cjohnsonfinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Maze extends View {

    private float height;
    private float width;
    private float ratio;

    private boolean[][] curMaze;

    private RectF square;
    private Paint space;
    private Paint wall;

    private String mazeString;
    private int curLevel;
    private int defaultGhosts;
    private int toAdd;

    public final static int LOSE = 0;
    public final static int WIN = 1;
    public final static int CHANGE = 2;


    public Maze(Context context) {
        super(context);
        initialize();
    }

    public Maze(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public Maze(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize () {
        height = 160;
        width = 160;
        ratio = 1;

        curLevel = 1;

        square = new RectF();
        space = new Paint();
        space.setARGB(255, 98, 0, 234);
        wall = new Paint();
        wall.setARGB(255, 49, 27, 146);
    }

    public String getMazeString () {
        return this.mazeString;
    }

    public boolean[][] getMaze() {
        return this.curMaze;
    }

    public void setMaze (String mazeString) {
        this.mazeString = mazeString;
        if (mazeString.compareTo("Maze 1") == 0) {
            curMaze = MAZE1;
        }
        else if (mazeString.compareTo("Maze 2") == 0) {
            curMaze = MAZE2;
        }
    }

    public void setToAdd (int num) {
        this.toAdd = num;
    }

    public int getToAdd () {
        return this.toAdd;
    }

    public void setDefaultGhosts (int num) {
        this.defaultGhosts = num;
    }

    public int getDefaultGhosts () {
        return this.defaultGhosts;
    }

    public int getLevel () {
        return this.curLevel;
    }

    public void setLevel (int level) {
        this.curLevel = level;
    }

    @SuppressLint("SetTextI18n")
    public void restartGame (int state, Ashman ashman, Ghosts ghosts, Cakes cakes, Button gameToggle, TextView totalCakes, TextView level) {
        ashman.pause();
        ghosts.pause();
        ashman.getCircles().clear();
        ghosts.getCircles().clear();
        ashman.invalidate();
        ghosts.invalidate();

        ashman.initialize(getContext());

        cakes.initialize();

        ashman.setMaze(curMaze);
        cakes.setMaze(curMaze);

        gameToggle.setText(R.string.buttonStart);
        String text = getContext().getString(R.string.cakeTotalText);
        totalCakes.setText(text + cakes.getTotalCakes());

        if (state == WIN) {
            ghosts.initialize(getContext());
            ghosts.increaseSpeed();
            ghosts.setNumGhosts(defaultGhosts + (toAdd * curLevel));
            curLevel ++;
        }
        else if (state == LOSE){
            curLevel = 1;
            initialize();
            ghosts.setNumGhosts(defaultGhosts);
        }
        else if (state == CHANGE) {
            ghosts.initialize(getContext());
            ghosts.setNumGhosts(defaultGhosts + (toAdd * (curLevel - 1)));
        }
        String levelText = getContext().getString(R.string.levelText);
        level.setText(levelText + curLevel);

        ghosts.setMaze(curMaze);
        ghosts.getCircles().clear();
        ghosts.createGhosts(ghosts.getNumGhosts());
        ghosts.invalidate();
        cakes.invalidate();
        ashman.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.scale(canvas.getWidth() / width, canvas.getHeight() / height);

        //draws the maze
        float gridWidth = width / 16;
        for (int ix = 0; ix < 16; ix ++)
            for (int i = 0; i < 16; i++) {
                square.set(ix * gridWidth, i * gridWidth, (ix * gridWidth) + gridWidth, (i * gridWidth) + gridWidth);
                if (curMaze[i][ix]) canvas.drawRect(square, space);
                else canvas.drawRect(square, wall);
            }
    }

    //Fits the maze to screen
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

    //default start position is [7] [7]
    /*
    public final static boolean[][] EMPTY_MAZE = {{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                         {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                  {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}};
    */

    public final static boolean[][] MAZE1 = {{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                                                    {false, true, true, true, true, true, true, false, true, true, true, true, true, true, true, false},
                                                    {false, true, false, true, false, false, true, false, true, false, true, false, true, false, true, false},
                                                    {false, true, false, true, false, false, true, true, true, false, true, false, true, false, true, false},
                                                    {false, true, false, true, false, true, true, false, true, false, true, false, true, false, true, false},
                                                    {false, true, false, true, true, true, false, false, true, false, true, true, true, false, true, false},
                                                    {false, true, false, false, true, false, false, false, true, false, false, true, false, false, true, false},
                                                    {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                                                    {false, true, false, false, true, false, false, true, false, false, false, true, false, false, true, false},
                                                    {false, true, false, true, true, true, false, true, false, false, true, true, true, false, true, false},
                                                    {false, true, false, true, false, true, false, true, false, false, true, false, true, false, true, false},
                                                    {false, true, false, true, false, true, false, true, false, true, true, false, true, false, true, false},
                                                    {false, true, false, true, false, true, false, true, true, true, false, false, true, false, true, false},
                                                    {false, true, false, true, false, true, false, true, false, true, false, false, true, false, true, false},
                                                    {false, true, true, true, true, true, true, true, false, true, true, true, true, true, true, false},
                                             {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}};

    public final static boolean[][] MAZE2 = {{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                                                    {false, false, false, true, true, true, false, false, false, false, false, true, true, true, true, false},
                                                    {false, true, true, true, false, true, false, false, false, true, true, true, false, false, true, false},
                                                    {false, true, false, true, false, true, false, false, false, true, false, true, false, false, true, false},
                                                    {false, true, false, true, false, true, false, false, false, true, false, true, false, false, true, false},
                                                    {false, true, false, true, true, true, true, true, true, true, false, true, true, false, true, false},
                                                    {false, true, false, false, false, false, false, false, false, true, false, false, true, false, true, false},
                                                    {false, true, true, true, false, true, true, true, true, true, true, true, true, true, true, false},
                                                    {false, true, false, true, false, true, false, false, false, true, false, false, false, false, true, false},
                                                    {false, true, false, true, true, true, false, false, false, true, true, false, false, false, true, false},
                                                    {false, true, false, true, false, false, false, false, false, false, true, true, true, true, true, false},
                                                    {false, true, false, true, false, false, false, true, true, true, false, true, false, false, true, false},
                                                    {false, true, false, true, true, true, false, true, false, true, false, true, false, false, true, false},
                                                    {false, true, false, true, false, true, false, true, false, true, false, true, false, false, true, false},
                                                    {false, true, true, true, false, true, true, true, false, true, true, true, true, true, true, false},
                                            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}};
}
