package com.example.colto.cjohnsonfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Maze maze;
    Cakes cakes;
    Ashman ashman;
    Ghosts ghosts;
    Button gameToggle;
    TextView totalCakes;
    TextView level;
    boolean rotate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maze = findViewById(R.id.maze);
        cakes = findViewById(R.id.cakes);
        ashman = findViewById(R.id.ashman);
        ghosts = findViewById(R.id.ghosts);
        gameToggle = findViewById(R.id.gameToggle);
        ImageView rightArrow = findViewById(R.id.rightArrow);
        ImageView upArrow = findViewById(R.id.upArrow);
        ImageView leftArrow = findViewById(R.id.leftArrow);
        ImageView downArrow = findViewById(R.id.downArrow);
        totalCakes = findViewById(R.id.cakeTotal);
        level = findViewById(R.id.level);

        if (savedInstanceState != null) {
            rotate = true;
            ashman.setCircles((ArrayList<Circle>) savedInstanceState.getSerializable("ashmanList"));
            ghosts.setCircles((ArrayList<Circle>) savedInstanceState.getSerializable("ghostsList"));
            ashman.setState(savedInstanceState.getBoolean("ashmanRunning"));
            ghosts.setState(savedInstanceState.getBoolean("ghostsRunning"));
            ashman.setAlive(savedInstanceState.getBoolean("ashmanAlive"));
            ashman.setCount(savedInstanceState.getInt("ashmanCycle"));
            ghosts.setNumGhosts(savedInstanceState.getInt("numGhosts"));
            ghosts.setConstant(savedInstanceState.getInt("ghostConstant"));
            cakes.setEmpty(savedInstanceState.getBoolean("cakesEmpty"));
            cakes.setMaze((boolean[][]) savedInstanceState.getSerializable("cakeArray"));
            maze.setLevel(savedInstanceState.getInt("level"));
            maze.setDefaultGhosts(savedInstanceState.getInt("defaultGhosts"));
            maze.setToAdd(savedInstanceState.getInt("ghostsToAdd"));
            maze.setMaze(savedInstanceState.getString("maze"));

            if (ashman.isAlive()) {
                if (cakes.isEmpty()) {
                    gameToggle.setText(R.string.buttonNext);
                }
                else {
                    if (ashman.getState()) {
                        gameToggle.setText(R.string.buttonPause);
                    } else {
                        gameToggle.setText(R.string.buttonStart);
                    }
                }
            }
            else {
                gameToggle.setText(R.string.buttonRestart);
            }
        }
        else {
            rotate = false;

            ghosts.createGhosts(maze.getDefaultGhosts());
        }
        cakes.getObjects(ashman, ghosts, gameToggle);
        ghosts.setAshman(ashman);
        ashman.setCakes(cakes);
        String text = getString(R.string.cakeTotalText);
        totalCakes.setText(text + cakes.getTotalCakes());
        String levelText = getString(R.string.levelText);
        level.setText(levelText + maze.getLevel());
        cakes.getTotalDisplay(totalCakes, level, text, levelText);
        ghosts.setGameToggle(gameToggle);

        gameToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ashman.isAlive()) {
                    if (cakes.isEmpty()) {
                        maze.restartGame(Maze.WIN, ashman, ghosts, cakes, gameToggle, totalCakes, level);
                    }
                    else {
                        if (ashman.getState()) {
                            ashman.pause();
                            gameToggle.setText(R.string.buttonStart);
                        } else {
                            ashman.resume();
                            gameToggle.setText(R.string.buttonPause);
                        }
                        if (ghosts.getState()) {
                            ghosts.pause();
                        } else {
                            ghosts.resume();
                        }
                    }
                }
                else {
                    maze.restartGame (Maze.LOSE, ashman, ghosts, cakes, gameToggle, totalCakes, level);
                }
            }
        });
        gameToggle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                boolean[][] cheatArray = new boolean[16][16];
                cheatArray[7][1] = true;
                cakes.setMaze(cheatArray);
                cakes.invalidate();
                return true;
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Circle circle : ashman.getCircles()) {
                    circle.setDirection(Character.RIGHT);
                }
            }
        });
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Circle circle : ashman.getCircles()) {
                    circle.setDirection(Character.UP);
                }
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Circle circle : ashman.getCircles()) {
                    circle.setDirection(Character.LEFT);
                }
            }
        });
        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Circle circle : ashman.getCircles()) {
                    circle.setDirection(Character.DOWN);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int startingGhosts = Integer.parseInt(prefs.getString("startingGhosts", "2"));
        int ghostsToAdd = Integer.parseInt(prefs.getString("ghostsToAdd", "2"));
        String mazeString = prefs.getString("maze", "Maze 1");

        if (maze.getDefaultGhosts() == 0) {
            maze.setDefaultGhosts(startingGhosts);
            maze.setToAdd(ghostsToAdd);
            maze.setMaze(mazeString);
            ashman.setMaze(maze.getMaze());
            ghosts.setMaze(maze.getMaze());
            cakes.setMaze(maze.getMaze());

            maze.restartGame(Maze.LOSE, ashman, ghosts, cakes, gameToggle, totalCakes, level);
        }
        else if (maze.getMazeString().compareTo(mazeString) != 0) {
            maze.setToAdd(ghostsToAdd);
            maze.setDefaultGhosts(startingGhosts);
            maze.setMaze(mazeString);
            ashman.setMaze(maze.getMaze());
            ghosts.setMaze(maze.getMaze());
            cakes.setMaze(maze.getMaze());

            maze.restartGame(Maze.CHANGE, ashman, ghosts, cakes, gameToggle, totalCakes, level);
        }
        else if (maze.getToAdd() != ghostsToAdd && maze.getLevel() != 1) {
            maze.setToAdd(ghostsToAdd);
            maze.setDefaultGhosts(startingGhosts);
            maze.setMaze(mazeString);
            ashman.setMaze(maze.getMaze());
            ghosts.setMaze(maze.getMaze());
            cakes.setMaze(maze.getMaze());
            maze.restartGame(Maze.CHANGE, ashman, ghosts, cakes, gameToggle, totalCakes, level);
        }
        else {
            maze.setToAdd(ghostsToAdd);
            maze.setDefaultGhosts(startingGhosts);
            maze.setMaze(mazeString);
            ashman.setMaze(maze.getMaze());
            ghosts.setMaze(maze.getMaze());

        }
    }

    @Override
    public void onPause () {
        super.onPause();
        ashman.pause();
        ghosts.pause();
    }

    @Override
    public void onSaveInstanceState (Bundle bundle) {
        bundle.putSerializable("ashmanList", ashman.getCircles());
        bundle.putSerializable("ghostsList", ghosts.getCircles());
        bundle.putBoolean("ashmanRunning", ashman.getState());
        bundle.putBoolean("ghostsRunning", ghosts.getState());
        bundle.putBoolean("ashmanAlive", ashman.isAlive());
        bundle.putInt("ashmanCycle", ashman.getCount());
        bundle.putInt("numGhosts", ghosts.getNumGhosts());
        bundle.putInt("ghostConstant", ghosts.getConstant());
        bundle.putBoolean("cakesEmpty", cakes.isEmpty());
        bundle.putSerializable("cakeArray", cakes.getMaze());
        bundle.putInt("level", maze.getLevel());
        bundle.putInt("defaultGhosts", maze.getDefaultGhosts());
        bundle.putInt("ghostsToAdd", maze.getToAdd());
        bundle.putString("maze", maze.getMazeString());

        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Final, Winter 2018, Colton A. Johnson",
                    Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
