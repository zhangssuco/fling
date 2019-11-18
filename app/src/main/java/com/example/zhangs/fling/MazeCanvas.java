package com.example.zhangs.fling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Stack;

public class MazeCanvas extends View {

    //MAZE DIMENSIONS
    public final int COLS = 10;
    public final int ROWS = 9;
    final int N_CELLS = COLS * ROWS;

    static int SIZE = 65;
    final int MARGIN = 60;
    int currentCellId=0;

    //ARRAY OF MAZE CELLS
    public MazeCell [] board;

    private Paint paint;

    Drawable imgpig =null;

    public MazeCanvas (Context context){

        super(context);

        //Toast.makeText(context, String.valueOf(this.getMeasuredWidth()),Toast.LENGTH_SHORT).show();
        //get half of the width and height as we are working with a circle

        try{
            imgpig=context.getResources().getDrawable(R.drawable.pig1, null);

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

//TASK 1: DECLARE A MAZE ARRAY OF SIZE N_CELLS TO HOLD THE CELLS
        board = new MazeCell[N_CELLS];

        //TASK 2: INSTANTIATE CELL OBJECTS FOR EACH CELL IN THE MAZE
        int cellId = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                //STEP 1: GENERATE A MAZE CELL WITH THE X, Y AND CELL ID
                int x = c * SIZE + MARGIN;
                int y = r * SIZE + MARGIN;
                MazeCell cell = new MazeCell(x, y, cellId);

                //STEP 2: PLACE THE CELL IN THE MAZE
                board[cellId] = cell;
                cellId++;
            }
        }

        //TASK 4: USE A BACKTRACKER METHOD TO BREAK DOWN THE WALLS
        backtrackMaze();


        //TASK 3: SET THE PAINT FOR THE MAZE
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);

    }

    public void	initialize() {
        //Called from layout when this view should assign a size and position to each of its children.
    if (SIZE==65) {
        SIZE = (this.getMeasuredWidth() - 2 * MARGIN) / COLS;
    }
        //TASK 2: INSTANTIATE CELL OBJECTS FOR EACH CELL IN THE MAZE
        int cellId = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                //STEP 1: GENERATE A MAZE CELL WITH THE X, Y AND CELL ID
                int x = c * SIZE + MARGIN;
                int y = r * SIZE + MARGIN;
                //MazeCell cell = new MazeCell(x, y, cellId);
                board[cellId].x=x;
                board[cellId].y=y;
                cellId++;
            }
        }

}

    public void setCurrentCellId(int where)
    {
        currentCellId=where;
    }

    Random r=new Random();

    String message="CSCI 268 HW 6";

    void setmessage(String message)
    {
        this.message=message;
    }
    public void onDraw(Canvas canvas) {
            initialize();

        //TASK 1: FILL THE CANVAS WITH WHITE PAINT

        canvas.drawRGB(0,  r.nextInt(256), r.nextInt(255));
        //TASK 2: SET THE LINES OF THE MAZE TO BLACK WITH A STROKE OF 2
        //Paint paint = new Paint();
        //paint.setColor(Color.BLACK);
        //paint.setStrokeWidth(2.0f);

        //TASK 3: DRAW THE LINES FOR EVERY CELL
        for (int i = 0; i < N_CELLS; i++) {
            int x = board[i].x;
            int y = board[i].y;

            if (board[i].north)
                canvas.drawLine(x, y, x + SIZE, y, paint);
            if (board[i].south)
                canvas.drawLine(x, y + SIZE, x + SIZE, y + SIZE, paint);
            if (board[i].east)
                canvas.drawLine(x + SIZE, y, x + SIZE, y + SIZE, paint);
            if (board[i].west)
                canvas.drawLine(x, y, x, y + SIZE, paint);
        }

        if (imgpig != null) {

            int y=(currentCellId/COLS)*SIZE+MARGIN;
            int x=(currentCellId%COLS)*SIZE+MARGIN;
            imgpig.setBounds(x, y, x + SIZE, y + SIZE);
            imgpig.draw(canvas);
        }
        canvas.drawText(message,10,10,new Paint(Color.RED));

    }

    public void backtrackMaze() {
        // TASK 1: CREATE THE BACKTRACKER VARIABLES AND INITIALIZE THEM
        Stack<Integer> stack = new Stack<Integer>();
        int top;

        // TASK 2: VISIT THE FIRST CELL AND PUSH IT ONTO THE STACK
        int visitedCells = 1; // COUNTS HOW MANY CELLS HAVE BEEN VISITED
        int cellID = 0; // THE FIRST CELL IN THE MAZE
        board[cellID].visited = true;
        stack.push(cellID);

        // TASK 3: BACKTRACK UNTIL ALL THE CELLS HAVE BEEN VISITED
        while (visitedCells < N_CELLS) {
            //STEP 1: WHICH WALLS CAN BE TAKEN DOWN FOR A GIVEN CELL?
            String possibleWalls = "";
            if (board[cellID].north == true && cellID >= COLS) {
                if (!board[cellID - COLS].visited) {
                    possibleWalls += "N";
                }
            }
            if (board[cellID].west == true && cellID % COLS != 0) {
                if (!board[cellID - 1].visited) {
                    possibleWalls += "W";
                }
            }
            if (board[cellID].east == true && cellID % COLS != COLS - 1) {
                if (!board[cellID + 1].visited) {
                    possibleWalls += "E";
                }
            }
            if (board[cellID].south == true && cellID < COLS * ROWS - COLS) {
                if (!board[cellID + COLS].visited) {
                    possibleWalls += "S";
                }
            }

            //STEP 2: RANDOMLY SELECT A RANDOM WALL FROM THE AVAILABLE WALLS
            if (possibleWalls.length() > 0) {
                int index = Math.round((int)(Math.random() *possibleWalls.length()));
                char randomWall = possibleWalls.charAt(index);

                switch (randomWall) {
                    case 'N':
                        board[cellID].north = false;
                        board[cellID - COLS].south = false;
                        cellID -= COLS;
                        break;
                    case 'S':
                        board[cellID].south = false;
                        board[cellID + COLS].north = false;
                        cellID += COLS;
                        break;
                    case 'E':
                        board[cellID].east = false;
                        board[cellID + 1].west = false;
                        cellID++;
                        break;
                    case 'W':
                        board[cellID].west = false;
                        board[cellID - 1].east = false;
                        cellID--;
                }
                board[cellID].visited = true;
                stack.push(cellID);
                visitedCells++;

            }
            //IF THERE ARE NO WALLS TO BUST DOWN, BACKTRACK BY GRABBING THE TOP OF THE STACK
            else {
                top = stack.pop();
                if (top == cellID){
                    cellID = stack.pop();
                    stack.push(cellID);
                }
            }
        }

    }



}
