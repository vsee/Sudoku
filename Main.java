import java.nio.file.Paths;
import java.nio.file.Path;

class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No game file specified!");
            return;
        }

        Path gameFile = Paths.get(args[0]);
        
        Sudoku game = new Sudoku();
        System.out.println("Parsing game file: " + gameFile);
        game.parseFromFile(gameFile);

        System.out.println(game);
    }

}
