import java.nio.file.Files;
import java.nio.file.Path;

import java.lang.StringBuilder;
import java.util.Arrays;

import java.util.Scanner;

import java.io.UncheckedIOException;
import java.io.IOException;

class Sudoku {

    public static final int GRID_DIM = 9;
    public static final int SUBGRID_DIM = GRID_DIM / 3;

    /** first dimension for columns second for rows 
     * only ints from 0 - 9 are allowed where 0 means empty*/
    private final int[][] grid;

    public Sudoku() {
        grid = new int[GRID_DIM][GRID_DIM];
    }

    public int getField(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );

        return grid[y][x];
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
            grid[y][x] = value;
            return true;
        }
        return false;
    }

    public void clearField(int x, int y) {
        if(x < 0 || x >= GRID_DIM || y < 0 || y >= GRID_DIM)
            throw new IllegalArgumentException("Invalid field coordinates: " + x + "x" + y );
        grid[y][x] = 0; 
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
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int y = 0; y < GRID_DIM; y++)
            bld.append(Arrays.toString(grid[y])).append("\n");

        return bld.toString();
    }
}
