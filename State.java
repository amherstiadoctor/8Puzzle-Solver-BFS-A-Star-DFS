import java.util.*;

public class State implements Comparable<State> {
	private final int board[];
	private final int cost;
	private final int weight;

	public State(int b[], int _cost){
		this.board = b;
		cost = _cost;
		weight = cost + heuristic();
	}

	public int[] getBoard() {
		return this.board;
	}

	public int getCost() {
		return this.cost;
	}

	private int heuristic() {
		int h = 0;
		for(int i = 0; i < board.length; ++i){
			if(board[i] == 0) continue;
			int dr = Math.abs(i/3 - (board[i]-1/3));
			int dc = Math.abs(i%3 - (board[i]-1)%3);
			h += dr + dc;
		}
		return h;
	}

	/*---FUNCTION TO CREATE NODES---
	expands the possible moves of a certain board
	*/
	public ArrayList<State> getNextStates() {
		ArrayList<State> states = new ArrayList<>();

		for(PuzzleModel.MOVES move : PuzzleModel.MOVES.values()) {
			int newBoard[] = this.board.clone();
			PuzzleModel.move(newBoard, move);
			if(!Arrays.equals(this.board, newBoard)){
				states.add(new State(newBoard, this.cost + 1));
			}
		}

		return states;
	}

	@Override
	public int compareTo(State o){
		return this.weight - o.weight;
	}
}