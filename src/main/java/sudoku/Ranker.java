package sudoku;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;

class Ranker {

    public static float rankSudoku(Sudoku game, boolean verbose) {
        if (game == null) throw new IllegalArgumentException("Given game must not be null");
        Sudoku gameToSolve = new Sudoku(game);
        
        // free fields need to be calculated from game
        // since the solver would change it
        int freeFields = game.countFreeFields();
        int sol = gameToSolve.solve(true, false);

        if (verbose)
            System.out.println(freeFields + " free field(s) and " + sol + " solution(s)");
        return calculateRank(sol, freeFields);

    }   

    private static float calculateRank(int solutions, int freeFields) {
        if (solutions < 0 || freeFields < 0)  
            throw new IllegalArgumentException("Given parameters must be >= 0");

        if (solutions == 0) return Float.MAX_VALUE;

        return solutions + (1 - (freeFields * (1f / 81)));
    } 

    public static void rankGames(Path gameDir) {
        float highestRank = Float.MAX_VALUE;
        Path highestRankPath = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(gameDir, "*.sd")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Sudoku game = new Sudoku();
                    game.parseFromFile(entry);
                    float rank = rankSudoku(game, true);
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

 

}
