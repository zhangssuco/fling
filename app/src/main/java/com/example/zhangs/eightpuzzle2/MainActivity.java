package com.example.zhangs.eightpuzzle2;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {
    private RelativeLayout relativeLayout;
    private ImageView pig;
    private LayoutInflater layoutInflater;
    private float xPos;
    private float yPos;
    private MazeCanvas maze;

    private int cellId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        xPos = 10;
        yPos = 10;
        cellId = 22;


        // CONSTRUCT THE MAZE AND ADD IT TO THE RELATIVE LAYOUT
        maze = new MazeCanvas(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.addView(maze, 0);

        // CREATE A LAYOUT INFLATER
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // SET THE BACKGROUND OF THE IMAGEVIEW TO THE PIG ANIMATION
        pig = (ImageView) layoutInflater.inflate(R.layout.pig_view, null);
        pig.setBackgroundResource(R.drawable.pig_animation);

        // CREATE AN ANIMATION DRAWABLE OBJECT BASED ON THIS BACKGROUND
        AnimationDrawable manAnimate = (AnimationDrawable) pig.getBackground();
        manAnimate.start();

        pig.setX(xPos);
        pig.setY(yPos);
        pig.setScaleX(.15f);
        pig.setScaleY(.15f);
        relativeLayout.addView(pig, 1);

    }

    public void goUp(View view) {
        if (maze.board[cellId].north == false){
            yPos -= 100;
            pig.setY(yPos);
            cellId -= maze.COLS;
        }
    }

    public void goLeft(View view) {
        if (maze.board[cellId].west == false){
            xPos -= 100;
            pig.setX(xPos);
            cellId--;
        }
    }

    public void goRight(View view) {
        if (maze.board[cellId].east == false){
            xPos += 100;
            pig.setX(xPos);
            cellId++;
        }
    }

    public void goDown(View view) {
        if (maze.board[cellId].south == false){
            yPos += 100;
            pig.setY(yPos);
            cellId += maze.COLS;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
