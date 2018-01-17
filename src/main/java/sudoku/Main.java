package sudoku;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner; 

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.InputMismatchException;

class Main {

    private static Scanner in; 

    private static void printMenu() {
        System.out.print("\n" +
                "1. Set field\n" +
                "2. Clear field\n" +
                "3. Find single solution\n" +
                "4. Find all solutions\n" +
                "5. Print game\n" +
                "6. Exit\n\n" +
                "Select an action [1-6]: ");
    }

    private static int requestInt(String msg, int min, int max) {
        if (msg == null) throw new IllegalArgumentException("Message must not be null");

        while(true) {
            System.out.print("Please provide " + msg + ": ");
            int input = parseInput();
            if (input >= min && input <= max) return input;
            else {
                System.out.println("Invalid input. Must be between " + min + " and " + max);
            }
        }
    }

    private static int parseInput() {
        try {
            return in.nextInt();
        } catch (InputMismatchException missE) {
            in.next(); // discard invalid input
            return -1;
        }
    }

    private static void gameLoop(Sudoku game) {
        if (game == null) throw new IllegalArgumentException("Given game must not be null!");
        boolean exit = false;
        System.out.println(game);

        while(!exit) {
            printMenu();
            int action = parseInput();
            int x, y, val, sol;
            Sudoku gameToSolve;

            switch(action) {
                case 1: 
                   x = requestInt("x coordinate", 0, 8);
                   y = requestInt("y coordinate", 0, 8);
                   val = requestInt("field value", 1, 9);
                   if(!game.setField(x,y,val)) {
                       System.out.println(val + " is not allowed at " + x + "x" + y);
                   }
                   System.out.println(game);
                   break;
                case 2:
                   x = requestInt("x coordinate", 0, 8);
                   y = requestInt("y coordinate", 0, 8);
                   game.clearField(x,y);
                   System.out.println(game);  
                   break;
                case 3:
                   gameToSolve = new Sudoku(game);
                   sol = gameToSolve.solve(false);
                   if(sol == 0) System.out.println("No solution found!");
                   else System.out.println(sol + " solution(s) found!");
                   break;
                case 4:
                   gameToSolve = new Sudoku(game);
                   sol = gameToSolve.solve(true);
                   if(sol == 0) System.out.println("No solution found!");
                   else System.out.println(sol + " solution(s) found!");
                   break;
                case 5:
                   System.out.println(game);
                   break;
                case 6: 
                   exit = true;
                   break;
                default: 
                   System.out.println("Invalid input! Try again.");
                   break;
            }
        }
 
    }

    public static void main(String[] args) {
        Sudoku game = new Sudoku();
        if (args.length < 1) {
            System.out.println("Starting with empty Sudoku");
        } else {
            Path gameFile = Paths.get(args[0]);
            System.out.println("Starting with game file: " + gameFile);
            game.parseFromFile(gameFile);
        }

        in = new Scanner(System.in);

        gameLoop(game);
        System.out.println("Bye Bye");

   }

}
