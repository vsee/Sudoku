package sudoku;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

class Main {

    //TODO argument error checking in all methods

    private enum Mode { GENERATE, RANK, GAME; }

    private static void playGame(Sudoku game, Path gamePath) {
        if (gamePath == null || Files.isDirectory(gamePath)) {
            System.out.println("Starting with empty Sudoku");
            Game.gameLoop(game);
        } else {
            System.out.println("Starting with game file: " + gamePath);
            game.parseFromFile(gamePath);
            Game.gameLoop(game);
        }
    }

    private static void rankGames(Sudoku game, Path gamePath) {
        if (gamePath == null) 
            throw new IllegalArgumentException("No game file/directory specified for ranker!");

        if (Files.isDirectory(gamePath)) {
            System.out.println("Ranking game files in: " + gamePath);
            Ranker.rankGames(gamePath); 
        } else {
            System.out.println("Ranking game: " + gamePath);
            game.parseFromFile(gamePath);
            System.out.println("Rank: " + Ranker.rankSudoku(game, true)); 
        }
    }

    private static void generateGames(Sudoku game, Path gamePath) {
        if (gamePath == null || Files.isDirectory(gamePath))
            throw new IllegalArgumentException("Please specify a game file for the generator.");

        System.out.println("Generating based on: " + gamePath);        
        game.parseFromFile(gamePath);
        Sudoku genGame = Generator.run(game);
        System.out.println("Game generation complete:\n" + genGame);
    }

    /**
     * args:
     *   0. GENERATE, RANK or GAME
     *   1. optional path to sudoku file or directory with sudoku files
     */
    public static void main(String[] args) {
        Sudoku game = new Sudoku();

        if (args.length < 1) {
            System.err.println("Please provide GENERATE, RANK or GAME as first argument.");
            System.exit(1);
        }

        Mode mode = Mode.valueOf(args[0]);
        Path gamePath = null;
        if (args.length >= 2)
            gamePath = Paths.get(args[1]);

        switch(mode) {
            case GAME:
                playGame(game, gamePath);
                break;
            case GENERATE:
                generateGames(game, gamePath);
                break;
            case RANK:
                rankGames(game, gamePath);
                break;
            default:
                System.err.println("Unknown game mode: " + mode);
                System.exit(1);

        }
        System.out.println("Bye Bye");
   }

}
