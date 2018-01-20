package sudoku;

import java.util.Random;

class Generator {

    private static final double ACCEPTANCE_PROBABILITY = 0.1;
    private static final Random rnd = new Random();

    public static Sudoku run(Sudoku game) {
        // solve to have a base to start with
        if(game.solve(false, false) <= 0) {
            System.err.println("Given base game must be solvable!");
            System.exit(1);
        }
            
        int steps = 300;
        float prevScore = Ranker.rankSudoku(game, true);
        System.out.println("DEBUG: start rank: " + prevScore);

        for(int i = 0; i < steps; i++) {
            Sudoku modGame = new Sudoku(game);
            generatorStep(modGame);
            float modScore = Ranker.rankSudoku(modGame, true);
            System.out.println("DEBUG: modrank: " + modScore);
            
            if(keepModifications(prevScore, modScore)) {
                prevScore = modScore;
                game = modGame;
            }

            steps--;
        }

        return game;
    }

    private static void generatorStep(Sudoku modGame) {
        /* TODO
         * 
         * roll random x and y coordinates
         *
         * play with this probability
         * 50% rnd to decide whether to delete or set a field
         * (if not all fields are currently free or taken) 
         *
         *  roll number to set
         *    if number invalid according to rules, try again
         *  or
         *  clear field
         *
         **/
    }

    private static boolean keepModifications(float prevScore, float modScore) {
        if (modScore <= prevScore) { // keep if new score is better
            return true;
        } else { // maybe keep if new score is worse
            if (rnd.nextDouble() <= ACCEPTANCE_PROBABILITY)
                return true;
            else
                return false;
        }
    }
    
}
