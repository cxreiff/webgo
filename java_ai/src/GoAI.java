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

	static String[] kolist;

	static Timer timer;

	public static void main(String[] args)
	{
		//TODO Take input from javascript go page.

		//input string representation of game board state.
		String input = "11x"+"eebeebeeebbbbbbbbewwwewbweeewwbeweebbewewwewewewe";
		kolist = new String[6];
		kolist[0] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";
		kolist[1] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";
		kolist[2] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";
		kolist[3] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";
		kolist[4] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";
		kolist[5] = "ebebewewbwebewbwebebwwwbeeeeebbebebwbbbwebeeewbbb";

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


		//2D matrix representation of board, 0: empty, 1: black, 2: white. TODO Remove.
		int[][] board = genBoard(pos);
		printBoard(board);

		System.out.println("\n"+"player "+(turn+1)+"'s turn."+"\n");	//TODO Remove.

		String result = (op == 0) ? nextMove(pos, turn) : evalEnd(pos);


		System.out.println(result);

		//TODO Return result to javascript go page.
	}

	public static String nextMove(String pos, int turn)		//Returns an intelligent next move for the player
	{
		timer.reset();

		GameNode root = new GameNode(turn, pos, kolist, null);

		ArrayList<GameNode> open = new ArrayList<GameNode>();
		open.add(root);

		int patience = 10000;	//Time limit in ms to calculate each move.

		//Before each loop of MCTS, make sure that the time limit has not been exceeded.
		while(timer.elapsed() < patience)
		{

			//Optimally (using UCT) choose a node in the game tree.
			//Expand the chosen node by picking a random child state.
			GameNode current = open.get((int) (Math.random() * open.size())).expand(); //TODO Change from random to UCT.

			//Add new node to pool of nodes that can be expanded.
			open.add(current);

			//Use evaluated random play from that new node to evaluate it.
			current.evaluate();
		}

		//When all node adjustments have been made and time has run out, choose the depth:1 option with the best value.
		return root.bestChild().getPos();
	}

	public static String evalEnd(String pos)		//Returns an evaluation of the final board position.
	{
		return GameNode.countTerritory(pos);
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
