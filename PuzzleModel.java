import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;

public class PuzzleModel {
	public static enum MOVES{UP, DOWN, RIGHT, LEFT};
	public static final int[] GOAL = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	private int[] current = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	private LinkedList<int[]> solvepath = new LinkedList<int[]>();
	private boolean solving = false;
	JFileChooser chooser = new JFileChooser();

	public boolean isSolving() {
		return this.solving;
	}

	public int[] getCurrentBoard() {
		return current.clone();
	}

	public void setCurrentBoard(int[] board) {
		this.current = board;
	}

	/*---FUNCTION TO GET PREV BOARD---
	iterates over solution path and gets the board after
	the current board
	*/
	public void getNextBoard(){
		for(int num = 0; num < this.solvepath.size(); num++){
			if(Arrays.equals(solvepath.get(num), this.current) && num-1 != -1){
				current = solvepath.get(num-1);
			}
		}
	}

	/*---FUNCTION TO GET PREV BOARD---
	iterates over solution path and gets the board before
	the current board
	*/
	public void getPrevBoard(){
		for(int j = 0; j < this.solvepath.size(); j++){
			if(Arrays.equals(solvepath.get(j), this.current) && j+1 < this.solvepath.size()){
				current = solvepath.get(j+1);
				break;	
			}
		}
	}
	/*---FUNCTION TO DETERMINE WHAT TILE WAS CLICKED---
	checks if tile clicked can be moved and then
	calls the move function
	*/
	public void tilePressed(int btn) {
		int blank = getBlankIndex(current);

		if(btn == blank-1){
			move(current, MOVES.LEFT);
		}else if(btn == blank+1){
			move(current, MOVES.RIGHT);
		}else if(btn == blank+3){
			move(current, MOVES.DOWN);
		}else if(btn == blank-3){
			move(current, MOVES.UP);
		}
	}
	/*---FUNCTION TO DETERMINE WHAT TILES TO MOVE---
	calls swap to actually swap the tiles
	*/
	public static void move(int[] board, MOVES toMove) {
		int blank = getBlankIndex(board);

		if(blank == -1) return;
		switch(toMove) {
			case UP:
				if(blank/3 != 0) swap(board, blank, blank-3);
				break;
			case DOWN:
				if(blank/3 != 2) swap(board, blank, blank+3);
				break;
			case RIGHT:
				if(blank%3 != 2) swap(board, blank, blank+1);
				break;
			case LEFT:
				if(blank%3 != 0) swap(board, blank, blank-1);
				break;
		}
	}

	/*---FUNCTION THAT SWAPS TILES WHEN CLICKED---*/
	public static void swap(int[] board, int i, int j){
		try{
			int iv = board[i];
			int jv = board[j];
			board[i] = jv;
			board[j] = iv;
		}catch(ArrayIndexOutOfBoundsException ex){
		}
	}

	/*---FUNCTION TO CHECK IF REACH GOAL BOARD---*/
	public boolean isSolved() {
		return Arrays.equals(this.current, this.GOAL);
	}
	/*---FUNCTION TO RESET BOARD---*/
	public void resetBoard() {
		for(int i = 0; i < current.length-1 ; ++i) current[i] = (int)(i+1);
		current[current.length - 1] = 0;

		PuzzleModel.this.solving = false; //enables board again
	}
	/*---FUNCTION TO CHECK WHETHER BOARD CAN BE SOLVED---*/
	public boolean isSolvable(int board[]) {
		int inv = 0;
		for(int i = 0; i < board.length; ++i){
			if(board[i] == 0) continue;
			if(board[0] == 0) continue;
			for(int j = i+1; j < board.length; ++j){
				if(board[j] != 0 && board[i] > board[j]) ++inv;
			}
		}

		return (inv % 2 == 0);
	}
	/*---FUNCTION TO FIND WHERE THE 0 IS ON BOARD---*/
	public static int getBlankIndex(int[] board) {
		for(int i = 0; i < board.length; ++i){
			if(board[i] == 0){
				return i;
			}
		}

		return -1;
	}

	/*---FUNCTION TO READ FILE FOR BOARD CONFIGURATION---*/
	public void readInputFile(){
		//PuzzleModel.this.solving = false; //enables board again
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		chooser.showOpenDialog(null);
		File f = chooser.getSelectedFile();
		String line;
		char[] chars = new char[3];
		int[] temp = new int[current.length];

		try {
			Scanner scanner = new Scanner(f);

			int k = 0;

			for(int j=0;scanner.hasNextLine(); j++){
				line = scanner.nextLine();
				String lineTrimmed = line.replaceAll("\\s", "");
				chars = lineTrimmed.toCharArray();

				for(int i = 0;i < 3 && i < chars.length; i++){
					temp[k] = Character.getNumericValue(chars[i]);
					k++;
				}
			}

			if(!isSolvable(temp)){
				JOptionPane.showMessageDialog(null, "Not Solvable!", "Warning", JOptionPane.ERROR_MESSAGE);
			}else{
				current = temp;
			}

		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "File Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*-----FUNCTION TO WRITE TO OUTPUT FILE-----
	takes char array movelist and reverses it
	and then writes it to a file
	*/
	public void writeOutputFile(char[] movelist){
		//char array to store the reverse of movelist
		char[] temp = new char[movelist.length];
		try(BufferedWriter writer = new BufferedWriter(new FileWriter("puzzle.out"))){
			int j = 0;
			//reverses the char array
			//because the steps are stored last to first
			for(int i = movelist.length-2; i >= 0; i--){
				temp[j] = movelist[i];
				j++;
			}
			String str = String.valueOf(temp);//makes char array temp into string
			writer.write(str);
			writer.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


	/*---FUNCTION THAT CALLS BFS SOLVER---*/
	public void solve(GUI gui, Solver.SOLVE_METHOD method) {
		Map<String, int[]> parent = null;

		switch(method){
            case A_STAR:
                parent = Solver.aStar(getCurrentBoard().clone());
                break;
            case DFS:
                parent = Solver.dfs(getCurrentBoard().clone());
                break;
             case BFS :
				parent = Solver.bfs(getCurrentBoard().clone());
				break;
		}

		//holds the boards of solution path
		LinkedList<int[]> nextBoard = new LinkedList<>();

		//if solve button is clicked on when goal state is current state
		if(Arrays.equals(this.current, this.GOAL)){
			String status = String.format("<html>%d moves<br/>%d expanded nodes</html>", nextBoard.size(), Solver.times);
			gui.setStatus(status);
			this.resetBoard();
			return;
		}

		this.solving = true;//disables board

		nextBoard.add(GOAL.clone());
		for(int i = 0; !Arrays.equals(nextBoard.get(i), this.current); i++){
			nextBoard.add(parent.get(make((nextBoard.get(i)))));
		}
		/*this block of code declares a movelist that will store
		the moves done towards solution	by iterating through nextBoard
		and comparing a board and the board next to it to determine
		where the blank tile moved
		*/
		char[] movelist = new char[nextBoard.size()];
		int[] second = new int[9];

		for(int i = 0; i<nextBoard.size(); i++){
			int[] first = nextBoard.get(i);
			if(i < 4){
				second = nextBoard.get(i+1);

				int j = getBlankIndex(first);
				int k = getBlankIndex(second);

				if(k == j-1){
					movelist[i] = 'R'; 
				}else if(k == j+1){
					movelist[i] = 'L'; 
				}else if(k == j+3){
					movelist[i] = 'U'; 
				}else if(k == j-3){
					movelist[i] = 'D'; 
				}

			}
		}

		writeOutputFile(movelist);//calls to output the created movelist into a file

		this.solvepath = nextBoard;

		String status = String.format("<html>%d moves<br/>%d expanded nodes</html>", nextBoard.size()-1, Solver.times);
		gui.setStatus(status);
	}

	private String make(int[] arr) {
		String str = "";

		for(int i = 0; i< arr.length; ++i){
			str+= String.valueOf(arr[i]);
		}

		return str;
	}
}