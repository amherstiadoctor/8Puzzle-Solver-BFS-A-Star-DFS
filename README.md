# CMSC 170 8 Puzzle BFS Solver with GUI

compile with
`javac -d bin *.java`

run with
`java -cp bin EightPuzzle`

puzzle.in and puzzle3.in are solvable boards
<br/>puzzle2.in is an unsolvable board

Functionalities:

* reads input file `puzzle.in` for board face/configuration
* when a solve button is clicked, disables board and previous and next board button show the solution path
* outputs `puzzle.out` file that contains solution path moves (open this file on notepad)

Default board configuration: <br />
![What it looks like when ran](uponRunning.png)

When Solve button and next button is clicked: <br />
![What it looks like when solve button clicked](whenSolveClicked.gif)
