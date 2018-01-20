# Sudoku Sample Solution
sample solution for Sudoku problem from programming club

It currently supports 

* playing the Sudoku on a simple console interface
* solving it using backtracking
* ranking a Sudoku
* finding the Sudoku with the highest rank from a directory of Sudoku files
* generating a Sudoku game

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
$ java -jar build/libs/sudoku-<version>.jar GAME|GENERATE|RANK [<path_to_gamefile> | <path_to_dir_with_game_files] [generator steps]
```

* select one of three execution modes GAME, GENERATE or RANK
* Specify a game file or a directory containing sudoku games
* GAME provides an interactiv console interface to play a sudoku game
* RANK allowes you to specify a directory with games, ranks all games in that directory and returns the one with the highest ranking
* GENERATE generates a random Sudoku game based on a given game file and an optional step specification
