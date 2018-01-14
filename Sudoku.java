import java.nio.file.Files;
import java.nio.file.Path;

import java.lang.StringBuilder;
import java.util.Arrays;

import java.util.Scanner;

import java.io.UncheckedIOException;
import java.io.IOException;

class Sudoku {

    private class Field {
        
        public int value = 0;
        /** indicates whether this value was set from the start of the game */
        public boolean initial = false;
    }


    public static final int GRID_DIM = 9;
    public static final int SUBGRID_DIM = GRID_DIM / 3;

    /** first dimension for columns second for rows 
     * only ints from 0 - 9 are allowed where 0 means empty*/
    private final Field[][] grid;

    public Sudoku() {
        grid = new Field[GRID_DIM][GRID_DIM];
        for(int x = 0; x < GRID_DIM; x++) {
            for(int y = 0; y < GRID_DIM; y++) {
                grid[y][x] = new Field();
            }
        }
    }

    public int getField(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );

        return grid[y][x].value;
    }

    /** 
     * Sets the grid at the given coordinate to the given value.
     * If the value is not allowed according to Sudoku rules, 
     * the field is not set and false is returned.
     * If setting was successful, true is returned. 
     */
    public boolean setField(int x, int y, int value) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        if(value < 1 || value > 9)
            throw new IllegalArgumentException("Invalid field value: " + value );

        if(isValid(x,y,value)) {
            grid[y][x].value = value;
            return true;
        }
        return false;
    }

    private boolean isSet(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        return grid[y][x].value != 0; 
    }

    private boolean isInitial(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        return grid[y][x].initial; 
    }

    public void clearField(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        grid[y][x].value = 0; 
    }

    public boolean isValid(int x, int y, int value) {
         if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
         if(value < 1 || value > 9)
            throw new IllegalArgumentException("Invalid field value: " + value );

        return checkGrid(x,y,value) && checkRow(x, y, value) && checkColumn(x,y,value); 
    }

    private boolean checkRow(int x, int y, int value) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        if(value < 1 || value > 9)
            throw new IllegalArgumentException("Invalid field value: " + value );
       
        for(int xx = 0; xx < GRID_DIM; xx++) {
            if (xx != x && getField(xx,y) == value) return false;
        } 
        return true;
    }

    private boolean checkColumn(int x, int y, int value) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        if(value < 1 || value > 9)
            throw new IllegalArgumentException("Invalid field value: " + value );
       
        for(int yy = 0; yy < GRID_DIM; yy++) {
            if (yy != y && getField(x,yy) == value) return false;
        } 
        return true;
    }

    private boolean checkGrid(int x, int y, int value) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        if(value < 1 || value > 9)
            throw new IllegalArgumentException("Invalid field value: " + value );
       
        // calculate sub 3x4 grid start values
        int x_s = x / SUBGRID_DIM * SUBGRID_DIM;
        int y_s = y / SUBGRID_DIM * SUBGRID_DIM;

        for(int xx = x_s; xx < x_s + SUBGRID_DIM; xx++) {
            for(int yy = y_s; yy < y_s + SUBGRID_DIM; yy++) {
                if (xx != x && yy != y && getField(xx,yy) == value) return false;
            }
        } 
        return true;
    }

    public void parseFromFile(Path fileName) {
        if (fileName == null || !Files.exists(fileName))
            throw new IllegalArgumentException("Given file does not exist: " + fileName);

        Scanner in; 
        try {     
            in = new Scanner(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        for(int y = 0; y < GRID_DIM; y++) {
            for(int x = 0; x < GRID_DIM; x++) {
                if(!in.hasNextInt())
                    throw new RuntimeException("Given Sudoku file has invalid format: " + fileName);

                int value = in.nextInt();
                if(value == 0) clearField(x,y);
                else {
                    if(!setField(x,y,value))
                       throw new RuntimeException("Given Sudoku file has invalid "
                               + "playing field at: " + x + "x" + y);
                    else
                        grid[y][x].initial = true;
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int y = 0; y < GRID_DIM; y++) {
            for(int x = 0; x < GRID_DIM; x++) {
                bld.append(grid[y][x].value);
                if(x % SUBGRID_DIM == 2) bld.append(" ");
                bld.append(" ");
            }

            if(y % SUBGRID_DIM == 2) bld.append("\n");
            bld.append("\n");
        }

        return bld.toString();
    }


    /** solve given sudoku using backtracking 
     * return true if a solution was found, false otherwise*/
    public boolean solve() {
       int x = 0;
       int y = 0;
       int steps = 0;

       boolean goBack = false;
       // while able to go back or forth through the grid
       while(canMove(x,y,goBack)) {
           steps++;
           if(!isInitial(x,y)) {

               goBack = false; // go forward
               if(!tryIncrease(x,y)) {
                   clearField(x,y);
                   goBack = true;
               }

           } 
           
           // move through grid
           if(goBack) {
               x--;
               if(x < 0) {
                   x = GRID_DIM - 1;
                   y--;
               }
           } else {
              x++;
              if(x >= GRID_DIM) {
                  x = 0;
                  y++;
              }
           }
      }

      System.out.println("Solver steps: " + steps);
      if (x == GRID_DIM - 1 && y == -1) return false;
      else if(x == 0 && y == GRID_DIM) return true;
      else {
          throw new RuntimeException("Unexpected algorithm outcome: " + x + "x" + y);
      }
    }

    private boolean canMove(int x, int y, boolean goBack) {
        if(goBack) {
            return !(x == GRID_DIM - 1 && y == -1);
        } else {
            return !(x == 0 && y == GRID_DIM);
        }
    }

    private boolean tryIncrease(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        int val = getField(x,y);
        while(true) {
            val += 1;
            if(val > 9) return false;
            if(setField(x,y,val)) return true;
        } 
    }

}
