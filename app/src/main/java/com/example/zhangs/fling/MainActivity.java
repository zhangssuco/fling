package com.example.zhangs.fling;

/*
* Requirements
* 1. Change background color
* 2. right and down button event handler
* 3. swipe left and swipe up
* 4. Tilt left and tilt right
* 5. Add Swipe sound
* 6. Align grids
* 7. This app integrates several sources, due to the imposed time constraints to mainly illustrate a couple features
* that can be taken advantages by mobile app. In a future course, if possible, I will refactor it with you!
* */


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.view.GestureDetector;
import android.widget.Toast;
import android.view.GestureDetector.OnGestureListener;

import java.util.Random;


public class MainActivity extends Activity
        implements OnGestureListener,SensorEventListener {

    GestureDetector detector;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private RelativeLayout relativeLayout;
    private ImageView pig;
    private LayoutInflater layoutInflater;
    private float xPos;
    private float yPos;
    private MazeCanvas maze;

    private int cellId;

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float X, float Y) {


        if(motionEvent2.getY() - motionEvent1.getY() > 50){
           Toast.makeText(MainActivity.this , " Swipe Down g" , Toast.LENGTH_SHORT).show();
            goDown(null);
            return true;
        }
        if(motionEvent2.getX() - motionEvent1.getX() > 50) {
            Toast.makeText(MainActivity.this, " Swipe Right g", Toast.LENGTH_SHORT).show();
            goRight(null);
            return true;
        }
        else {
            return true ;
        }
    }

    @Override
    public void onLongPress(MotionEvent arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {

        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {

        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // TODO Auto-generated method stub

        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {

        // TODO Auto-generated method stub

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // CONSTRUCT THE MAZE AND ADD IT TO THE RELATIVE LAYOUT
        maze = new MazeCanvas(this);
        gestureDetector = new GestureDetector(MainActivity.this, MainActivity.this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        cellId =(new Random()).nextInt(maze.board.length) ;
        maze.setCurrentCellId(cellId);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.addView(maze, 0);

        // SET THE BACKGROUND OF THE IMAGEVIEW TO THE PIG ANIMATION
        pig=(ImageView)findViewById(R.id.animation);
        pig.setBackgroundResource(R.drawable.pig_animation);

        // CREATE AN ANIMATION DRAWABLE OBJECT BASED ON THIS BACKGROUND
        AnimationDrawable manAnimate = (AnimationDrawable) pig.getBackground();
        manAnimate.start();

        initListeners();

    }


    public void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST); // _GAME
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSensorManager.unregisterListener(this);

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        initListeners();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    float[] mGravity;
    float  last_x=0, last_y=0, last_z=0;
    long lastUpdate=System.currentTimeMillis();
    private static final int SHAKE_THRESHOLD = 2000;

    boolean detectshake()
    {
           boolean flag=false;
           float x=0, y=0, z=0;
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 200) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x =  mGravity[0];
                y = mGravity[1];
                z = mGravity[2];

                float speed = Math.abs(x- last_x)+Math.abs(y - last_y)+Math.abs( z- last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                    randomize(null);
                    flag=true;
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        return flag;
    }

    boolean detecttilt()
    {
        float x = mGravity[0];
        float y = mGravity[1];
        float TILTTHRESHOLD=10;
        boolean flag=false;

        if (x > (-TILTTHRESHOLD) && x < (TILTTHRESHOLD) && y > (-TILTTHRESHOLD) && y < (TILTTHRESHOLD)) {
            //change is too small
            //Toast.makeText(getApplicationContext(), "no tilt detected", Toast.LENGTH_SHORT).show();
            ;
        }
        else if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                //Toast.makeText(getApplicationContext(), "right tilt detected", Toast.LENGTH_SHORT).show();
                goRight(null);
                flag=true;
            }
            if (x > 0) {
                //Toast.makeText(getApplicationContext(), "left tilt detected", Toast.LENGTH_SHORT).show();
                goLeft(null);
                flag=true;
            }

        }

        return flag;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        //If type is accelerometer only assign values to global property mGravity
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mGravity = event.values;
            if(!detectshake());
                detecttilt();
        }

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public void goUp(View view) {
        if (maze.board[cellId].north == false){
            cellId -= maze.COLS;
            maze.setCurrentCellId(cellId);
            scalepig();
            maze.invalidate();
        }
    }

    public void goLeft(View view) {
        if (maze.board[cellId].west == false){
            cellId--;
            maze.setCurrentCellId(cellId);
            scalepig();
            maze.invalidate();
        }
    }

    public void goRight(View view) {
        if (maze.board[cellId].east == false){
            cellId++;
            maze.setCurrentCellId(cellId);

            scalepig();
            maze.invalidate();
        }
    }

    public void goDown(View view) {
        if (maze.board[cellId].south == false){
            scalepig();
            cellId += maze.COLS;
            maze.setCurrentCellId(cellId);
            maze.invalidate();
        }
    }

    void scalepig()
    {
        float ratio=(maze.SIZE*1.0f)/pig.getWidth();
        scaleView(pig, 0f, ratio);
        //pig.setScaleX(.15f);
        //pig.setScaleY(.15f);
        //pig.setScaleX(ratio);
        //pig.setScaleY(ratio);
    }
    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    Random r= new Random();
    public void randomize(View view) {
        cellId=r.nextInt(maze.board.length);
        maze.setCurrentCellId(cellId);
        maze.invalidate();
    }



}
