package sudoku;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.util.Scanner; 

import java.io.File;
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
                "5. Rank Sudoku\n" +
                "6. Print game\n" +
                "7. Exit\n\n" +
                "Select an action [1-7]: ");
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

        in = new Scanner(System.in);

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
                   sol = gameToSolve.solve(false, true);
                   if(sol == 0) System.out.println("No solution found!");
                   else System.out.println(sol + " solution(s) found!");
                   break;
                case 4:
                   gameToSolve = new Sudoku(game);
                   sol = gameToSolve.solve(true, true);
                   if(sol == 0) System.out.println("No solution found!");
                   else System.out.println(sol + " solution(s) found!");
                   break;
                case 5:
                   System.out.println("Rank: " + rankSudoku(game));
                   break;                   
                case 6:
                   System.out.println(game);
                   break;
                case 7: 
                   exit = true;
                   break;
                default: 
                   System.out.println("Invalid input! Try again.");
                   break;
            }
        }
 
    }

    private static float rankSudoku(Sudoku game) {
        if (game == null) throw new IllegalArgumentException("Given game must not be null");
        Sudoku gameToSolve = new Sudoku(game);
        
        // free fields need to be calculated from game
        // since the solver would change it
        int freeFields = game.countFreeFields();
        int sol = gameToSolve.solve(true, false);
        return calculateRank(sol, freeFields);

    }

    private static float calculateRank(int solutions, int freeFields) {
        if (solutions < 0 || freeFields < 0) 
            throw new IllegalArgumentException("Given parameters must be >= 0");

        if (solutions == 0) return Float.MAX_VALUE;

        return solutions + (1 - (freeFields * (1f / 81)));
    }

    private static void rankGames(Path gameDir) {
        float highestRank = Float.MAX_VALUE;
        Path highestRankPath = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(gameDir, "*.sd")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Sudoku game = new Sudoku();
                    game.parseFromFile(entry);
                    float rank = rankSudoku(game);
                    System.out.println(rank + " " + entry);

                    if(rank < highestRank) {
                        highestRank = rank;
                        highestRankPath = entry;
                    } 
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading game directory: " + gameDir + "\n " + e);
        }

        if(highestRankPath != null)
            System.out.println("Highest ranking Sudoku: " + highestRank + " : " + highestRankPath);
    }

    public static void main(String[] args) {
        Sudoku game = new Sudoku();
        Path gamePath = null;
        if (args.length >= 1)
            gamePath = Paths.get(args[0]);

        if (gamePath == null) {
            System.out.println("Starting with empty Sudoku");
            gameLoop(game);
        } else {
            if (Files.isDirectory(gamePath)) {
                System.out.println("Ranking game files in: " + gamePath);
                rankGames(gamePath); 
            } else {
                System.out.println("Starting with game file: " + gamePath);
                game.parseFromFile(gamePath);
                gameLoop(game);
            }
        }

        System.out.println("Bye Bye");
   }

}
