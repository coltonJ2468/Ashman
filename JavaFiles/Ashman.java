package com.example.colto.cjohnsonfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.io.Serializable;

public class Ashman extends Character implements GestureDetector.OnGestureListener, Serializable{

    private RectF ashman;
    private Bitmap ashPic;
    private Paint paint;
    private Cakes cakes;

    private boolean isRunning;
    private boolean alive;
    private int count;

    private GestureDetectorCompat mDetector;
    private Handler ashHandler;
    private Runnable ashTimer;

    public Ashman(Context context) {
        super(context);
        initialize(context);
    }

    public Ashman(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public Ashman(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @Override
    public void initialize (Context context) {
        super.initialize(context);

        mDetector = new GestureDetectorCompat(context, this);
        getCircles().add(new Circle(70, 70, Character.RIGHT));

        ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_full);
        ashman = new RectF ();
        paint = new Paint();
        paint.setARGB(255, 255, 255, 141);

        isRunning = false;
        alive = true;
        count = 12;

        ashHandler = new Handler ();
        ashTimer = new Runnable() {
            @Override
            public void run() {
                onTimer();
                ashHandler.postDelayed(this, Character.TIMER_M_SEC);
            }
        };
    }

    public void setCakes(Cakes cakes) {
        this.cakes = cakes;
    }

    public int getCount () {
        return this.count;
    }

    public void setCount (int num) {
        this.count = num;
    }

    public boolean getState () {
        return isRunning;
    }

    public void setState (boolean state) {
        this.isRunning = state;
    }

    public boolean isAlive () {
        return alive;
    }

    public void setAlive (boolean state) {
        this.alive = state;
    }

    public void isDead() {
        this.alive = false;
    }

    @Override
    public void onTimer () {
        super.onTimer();
        for (Circle circle : getCircles()) {
            int posX;
            int posY;
            if (circle.getDirection() == Character.RIGHT ||
                    circle.getDirection() == Character.DOWN) {
                posX = getPosX(circle.getLocX());
                posY = getPosY(circle.getLocY());
                cakes.updateCakes(posX, posY);
            } else if (circle.getDirection() == Character.LEFT ||
                    circle.getDirection() == Character.UP) {
                posX = getRightPosX(circle.getLocX());
                posY = getBotPosY(circle.getLocY());
                cakes.updateCakes(posX, posY);
            }

            count = (count + 1) % 12;
            int full = 0;
            int p11 = 2;
            int p21 = 4;
            int p3 = 6;
            int p22 = 8;
            int p12 = 10;
            if (circle.getDirection() == Character.RIGHT) {
                if (count == full) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_full);
                }
                if (count == p11 || count == p12) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_right1);
                }
                if (count == p21 || count == p22) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_right2);
                }
                if (count == p3) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_right3);
                }
            }
            if (circle.getDirection() == Character.UP) {
                if (count == full) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_full);
                }
                if (count == p11 || count == p12) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_up1);
                }
                if (count == p21 || count == p22) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_up2);
                }
                if (count == p3) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_up3);
                }
            }
            if (circle.getDirection() == Character.LEFT) {
                if (count == full) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_full);
                }
                if (count == p11 || count == p12) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_left1);
                }
                if (count == p21 || count == p22) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_left2);
                }
                if (count == p3) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_left3);
                }
            }
            if (circle.getDirection() == Character.DOWN) {
                if (count == full) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_full);
                }
                if (count == p11 || count == p12) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_down1);
                }
                if (count == p21 || count == p22) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_down2);
                }
                if (count == p3) {
                    ashPic = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_down3);
                }
            }
        }
        invalidate();
    }

    //starts the animation
    public void resume () {
        isRunning = true;
        ashHandler.postDelayed(ashTimer, Character.TIMER_M_SEC);
    }

    //pauses the animation
    public void pause () {
        isRunning = false;
        ashHandler.removeCallbacks(ashTimer);
    }

    @Override
    public void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        for (Circle circle : getCircles()) {
            float gridWidth = 10;
            float yCord = circle.getLocY();
            float xCord = circle.getLocX();

            ashman.set(xCord + 1, yCord + 1,
                    (xCord + gridWidth) - 1, (yCord + gridWidth) - 1);
            canvas.drawBitmap(ashPic, null, ashman, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        for (Circle circle : getCircles()) {
            float error = 100;
            if (motionEvent1.getX() - motionEvent.getX() > error && Math.abs(motionEvent.getY() - motionEvent1.getY()) < error) {
                circle.setDirection(Character.RIGHT);
                return true;
            }
            else if (motionEvent.getY() - motionEvent1.getY() > error && Math.abs(motionEvent.getX() - motionEvent1.getX()) < error) {
                circle.setDirection(Character.UP);
                return true;
            }
            else if (motionEvent.getX() - motionEvent1.getX() > error && Math.abs(motionEvent.getY() - motionEvent1.getY()) < error) {
                circle.setDirection(Character.LEFT);
                return true;
            }
            else if (motionEvent1.getY() - motionEvent.getY() > error && Math.abs(motionEvent.getX() - motionEvent1.getX()) < error) {
                circle.setDirection(Character.DOWN);
                return true;
            }
        }
        return false;
    }
}
