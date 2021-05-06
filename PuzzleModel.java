import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;

public class PuzzleModel {
	public static enum MOVES{UP, DOWN, RIGHT, LEFT};
	public static enum SPEED{SLOW, MEDIUM, FAST};
	private int timerSpeed = 500;
	public static final int[] GOAL = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	private int[] current = {1, 2, 3, 4, 5, 6, 7, 8, 0};
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

    public void setTimerSpeed(SPEED speed){
        switch(speed){
            case SLOW:
                this.timerSpeed = 700;
                break;
            case MEDIUM:
                this.timerSpeed = 300;
                break;
            case FAST:
                this.timerSpeed = 100;
                break;
        }
    }

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

	public boolean isSolved() {
		return Arrays.equals(this.current, this.GOAL);
	}

	public void resetBoard() {
		for(int i = 0; i < current.length-1 ; ++i) current[i] = (int)(i+1);
		current[current.length - 1] = 0;
	}

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

	public static int getBlankIndex(int[] board) {
		for(int i = 0; i < board.length; ++i){
			if(board[i] == 0){
				return i;
			}
		}

		return -1;
	}

	public static void swap(int[] board, int i, int j){
		try{
			int iv = board[i];
			int jv = board[j];
			board[i] = jv;
			board[j] = iv;
		}catch(ArrayIndexOutOfBoundsException ex){
		}
	}

	public void readInputFile(){
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

	public void writeOutputFile(){
		try{//writting
			Writer writer = null;
			writer = new BufferedWriter(new FileWriter("puzzle.out"));
			writer.write("Lord Help Me"+"\n");
			writer.close();
		} catch(FileNotFoundException e) {
			System.out.println("File input.txt not found");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void solve(GUI gui, Solver.SOLVE_METHOD method) {
		Map<String, int[]> parent = null;

		this.solving = true;
		long time = System.nanoTime();

		parent = Solver.bfs(getCurrentBoard().clone());

		time = (System.nanoTime() - time) / 1000000;

		Stack<int[]> nextBoard = new Stack<>();
		nextBoard.add(GOAL.clone());
		while(!Arrays.equals(nextBoard.peek(), this.current)){
			nextBoard.add(parent.get(make(nextBoard.peek())));
		}
		nextBoard.pop();

		String status = String.format("<html>%d moves<br/>%d expanded nodes</html>", nextBoard.size(), Solver.times);
		gui.setStatus(status);

		new Timer(this.timerSpeed, new ActionListener(){
            private Stack<int[]> boards;
            public PuzzleModel bc;
            
            //gives the timer the stack of states, the gui and the board controller
            //and disables the whole GUI untill finished
            public ActionListener me(Stack<int[]> stk, PuzzleModel _bc){
                this.boards = stk;
                this.bc = _bc;
                return this;
            }
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //if the stack is empty, close enable the GUI and stop the timer
                if(boards.empty() || isSolved()){
                    PuzzleModel.this.solving = false;
                    ((Timer)e.getSource()).stop();
                    return;
                }
                
                //set the current board to the given state and update the GUI
                bc.setCurrentBoard(boards.pop());
                gui.drawBoard();
            }
        }.me(nextBoard, this)).start();    //start the timer right away
	}

	private String make(int[] arr) {
		String str = "";

		for(int i = 0; i< arr.length; ++i){
			str+= String.valueOf(arr[i]);
		}

		return str;
	}
}