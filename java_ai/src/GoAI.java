import java.util.ArrayList;
import java.util.List;

/**
 * COSC 331 - Programming Project - jreiff17
 *
 * Java AI for Web Go game. Decides computer's moves and analyzes endgame positions.
 */

public class GoAI
{
	static int n;
	static int turn;

	public static void main(String[] args)
	{
		//TODO Take input from javascript go page.
		String input = "11xeebweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeb";	//input string representation of game board state.

		int op = Integer.parseInt(input.substring(0, 1));	//Determines whether the AI is being asked for 0: the best next move, or 1: an endgame evaluation.
		turn = Integer.parseInt(input.substring(1, 2));	//Determines whether 0: black plays next or 1: white plays next.
		String pos = input.substring(3);

		n = (int)(Math.sqrt((pos.length())));			//Length of each side of the board.

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

		System.out.println("turn: player "+(turn+1)+"\n");

		printBoard(board);

		String result;

		if(op == 0) result = nextMove(pos, turn);
		else result = evalEnd(pos);

		System.out.println("\n"+result);
		//TODO Return result to javascript go page.
	}

	public static String nextMove(String pos, int turn)		//Returns an intelligent next move for the player
	{
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

			//If a tile group is only adjacent to one type of stone, add number of empty tiles in group to score of stone type.

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
				if ((temp > 0) && (temp - 1) % n != 6) {
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

			//Remove evaluated group from array of empty tiles.

			System.out.println(empties.toString());
			System.out.println(adj.toString());

			for(int i = 0; i < adj.size(); i++) empties.remove(empties.indexOf(adj.get(i)));
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
			if (empties.contains(current + 1)) {

				empties.add(0, empties.remove(empties.indexOf(current + 1)));
				genAdj(adj, empties);
			}
			if (empties.contains(current - 1)) {

				empties.add(0, empties.remove(empties.indexOf(current - 1)));
				genAdj(adj, empties);
			}
		}
	}

	public static void printBoard(int[][] board)	//Prints the board matrix, 0:empty, 1:black, 2:white.
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
