import java.util.ArrayList;
import java.util.List;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 *
 * Scoring for endgame positions is decided by the following: any group of adjacent empty spaces
 * adjacent to only board edges and a single stone color adds the number of empty spaces in the
 * group to the score of that color.
 */

public class GoAI
{
	static int n;
	static int turn;

	static Timer timer;

	public static void main(String[] args)
	{
		//TODO Take input from javascript go page.

		//input string representation of game board state.
		String input = "11x"+"eebeebeeebbbbbbbbewwwewbweeewwbeweebbewewwewewewe";

		//Determines whether the AI is being asked for 0: the best next move, or 1: an endgame evaluation.
		int op = Integer.parseInt(input.substring(0, 1));

		//Determines whether 0: black plays next or 1: white plays next.
		turn = Integer.parseInt(input.substring(1, 2));

		//string representation of the board and its stones.
		String pos = input.substring(3);

		//Timer to determine how long to "think" before making a move.
		timer = new Timer();

		//Length of each side of the board.
		n = (int)(Math.sqrt((pos.length())));


		//2D matrix representation of board, 0: empty, 1: black, 2: white.
		int[][] board = genBoard(pos);
		printBoard(board);

		System.out.println("turn: player "+(turn+1)+"\n");

		String result;

		if(op == 0) result = nextMove(pos, turn);
		else result = evalEnd(pos);

		System.out.println("\n"+result);
		
		//TODO Return result to javascript go page.
	}

	public static String nextMove(String pos, int turn)		//Returns an intelligent next move for the player
	{
		timer.reset();

		int patience = 10000;	//This is the amount of milliseconds that the MCTS algorithm searches for before moving.

		//Before each loop of MCTS, make sure that the time limit has not been exceeded.
		while(timer.elapsed() < patience)
		{

			//Optimally (using UCT) choose a node in the game tree.

			//Expand the chosen node by picking a random child state.

			//Use evaluated random play from that new node to evaluate it.

			//Add 2*M, M being some value representing depth into the game to randomly play, to the turn value.

			//Make random moves, stone color depending on whether the turn count is odd or even.

			//Apply capturing logic, decrease the turn count by one each move.

			//When turn count reaches 0, break.

			//Evaluate position using endgame evaluation method, add running count of
			//captured pieces, and use result to update values of each node in the monte carlo tree.

			//Use evaluation of new node to update values of each ancestor node.

		}

		//When all node adjustments have been made and time has run out, choose the depth:1 option with the best value.

		return pos+"yyy"+turn;
	}

	public static String evalEnd(String pos)		//Returns an evaluation of the final board position.
	{
		int blackTerr = 0;
		int whiteTerr = 0;

		//Add all empty tiles into an array.
		List<Integer> empties = new ArrayList<Integer>();

		for(int i = 0; i < pos.length(); i++)
		{
			if(pos.charAt(i) == 'e') empties.add(i);
		}

		while(empties.size() > 0) {

			//Group empty tiles into groups of adjacent tiles.

			List<Integer> adj = new ArrayList<Integer>();
			genAdj(adj, empties);

			System.out.println("x: "+adj.toString());

			//If a tile group is only adjacent to one type of stone,
			//add number of empty tiles in group to score of stone type.

			int adjColor = 0;	//0 if no adjacent stones so far, 1 if black is found, 2 if white is found, 3 if both.

			for(int i = 0; i < adj.size(); i++) {
				int temp = adj.get(i);

				if (temp - n >= 0) {
					switch (pos.charAt(temp - n)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if (temp + n < n * n) {
					switch (pos.charAt(temp + n)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if ((temp > 0) && (temp - 1) % n != n-1) {
					switch (pos.charAt(temp - 1)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
				if ((temp < n*n) && (temp + 1) % n != 0) {
					switch (pos.charAt(temp + 1)) {
						case 'b':
							if (adjColor == 0) adjColor = 1;
							else if (adjColor == 2) adjColor = 3;
							break;
						case 'w':
							if (adjColor == 0) adjColor = 2;
							else if (adjColor == 1) adjColor = 3;
							break;
						default:
							break;
					}
				}
			}

			if(adjColor == 1) blackTerr += adj.size();
			else if(adjColor == 2) whiteTerr += adj.size();
		}

		return blackTerr+"x"+whiteTerr;
	}

	public static void genAdj(List<Integer> adj, List<Integer> empties)
	{
		int current = empties.remove(0);
		System.out.println("y: "+current);
		if(!adj.contains(current))
		{
			adj.add(current);

			if (empties.contains(current - 7)) {

				empties.add(0, empties.remove(empties.indexOf(current - 7)));
				genAdj(adj, empties);
			}
			if (empties.contains(current + 7)) {

				empties.add(0, empties.remove(empties.indexOf(current + 7)));
				genAdj(adj, empties);
			}
			if ((current + 1 % n != 0) && empties.contains(current + 1)) {

				empties.add(0, empties.remove(empties.indexOf(current + 1)));
				genAdj(adj, empties);
			}
			if ((current - 1 % n != n-1) && empties.contains(current - 1)) {

				empties.add(0, empties.remove(empties.indexOf(current - 1)));
				genAdj(adj, empties);
			}
		}
	}

	public static int[][] genBoard(String pos)	//TODO Remove.
	{
		int[][] board = new int[n][n];

		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				char stone = pos.charAt((i*7)+j);

				switch(stone)
				{
					case 'e':
						board[i][j] = 0;
						break;
					case 'b':
						board[i][j] = 1;
						break;
					case 'w':
						board[i][j] = 2;
						break;
				}
			}
		}

		return board;
	}
	public static void printBoard(int[][] board)	//Prints the board matrix, 0:empty, 1:black, 2:white. TODO Remove.
	{
		for(int i=0; i < board.length; i++)
		{
			for(int j=0; j < board[0].length; j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}
