package sudoku;

import java.util.Random;

class Generator {

    public static final int DEFAULT_STEPS = 300;
    private static final double ACCEPTANCE_PROBABILITY = 0.1;
    private static final Random rnd = new Random();

    public static Sudoku run(Sudoku game, int steps) {
        // solve to have a base to start with
        if(game.solve(false, false) <= 0) {
            System.err.println("Given base game must be solvable!");
            System.exit(1);
        }

        game.makeAllInitial(); // this is important for all following solving algorithms
            
        float prevScore = Ranker.rankSudoku(game, false);

        for(int i = 0; i < steps; i++) {
            //System.out.println("Step " + i);
            Sudoku modGame = new Sudoku(game);
            generatorStep(modGame);
            float modScore = Ranker.rankSudoku(modGame, false);
            
            if(keepModifications(prevScore, modScore)) {
                prevScore = modScore;
                game = modGame;
                
                //System.out.println("rank: " + prevScore);
                //System.out.println(game);
            }
        }

        return game;
    }

    private static void generatorStep(Sudoku modGame) {
        int x = rnd.nextInt(9);
        int y = rnd.nextInt(9);
        
        if(modGame.isSet(x,y)) {
            modGame.clearField(x,y);
        } else {
            int val = rnd.nextInt(9) + 1;
            int count = 20;
            while (count > 0 && !modGame.setField(x,y,val)) {
                val = rnd.nextInt(9) + 1;
                count--;
            }
        }

        modGame.makeAllInitial();
    }

    private static boolean keepModifications(float prevScore, float modScore) {
        if (modScore <= prevScore) { // keep if new score is better
            return true;
        } else { // maybe keep if new score is worse
            if (modScore != Float.MAX_VALUE && rnd.nextDouble() <= ACCEPTANCE_PROBABILITY)
                return true;
            else
                return false;
        }
    }
    
}
