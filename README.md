# Sudoku Sample Solution
sample solution for Sudoku problem from programming club

It currently supports 

* playing the Sudoku on a simple console interface
* solving it using backtracking
* ranking a Sudoku
* finding the Sudoku with the highest rank from a directory of Sudoku files

You can find some sample Sudoku game files to work with under
```
src/main/resources/games
```

to build:
```
$ ./gradlew build
```

to run:

```
$ java -jar build/libs/sudoku-<version>.jar [<path_to_gamefile> | <path_to_dir_with_game_files]
```

* Specify a game file to start the interactive mode for the given game.
* Specify a directory with game files to rank all games in the directory and find the one with the highest ranking.
